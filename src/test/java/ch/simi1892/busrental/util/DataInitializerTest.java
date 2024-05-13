package ch.simi1892.busrental.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.util.ReflectionTestUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

class DataInitializerTest {

    @Mock
    private DataSource dataSource;

    @Mock
    private Connection connection;

    @Mock
    private Statement statement;

    @InjectMocks
    private DataInitializer dataInitializer;

    @Mock
    private ApplicationContext applicationContext;

    @BeforeEach
    void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(dataInitializer, "environment", "DEV");
        Mockito.when(dataSource.getConnection()).thenReturn(connection);
        Mockito.when(connection.createStatement()).thenReturn(statement);
    }

    @Test
    void shouldExecuteSqlScriptOnDevEnvironment() throws SQLException {
        // Arrange
        ContextRefreshedEvent event = new ContextRefreshedEvent(applicationContext);
        ClassPathResource scriptResource = new ClassPathResource("data/data.sql");

        // Act
        dataInitializer.onApplicationEvent(event);

        // Assert
        Mockito.verify(connection, Mockito.times(1)).close();
        Mockito.verify(statement, Mockito.times(510)).execute(Mockito.anyString());
    }

    @Test
    void shouldNotExecuteSqlScriptOnNonDevEnvironment() throws SQLException {
        // Arrange
        ReflectionTestUtils.setField(dataInitializer, "environment", "PROD");
        ContextRefreshedEvent event = new ContextRefreshedEvent(applicationContext);

        // Act
        dataInitializer.onApplicationEvent(event);

        // Assert
        Mockito.verify(dataSource, Mockito.never()).getConnection(); // Ensure no connection was obtained
        Mockito.verify(connection, Mockito.never()).close(); // Ensure connection was never opened, hence not closed
        // Use Mockito to verify the static method was not called
        try (MockedStatic<ScriptUtils> mockedStatic = Mockito.mockStatic(ScriptUtils.class)) {
            dataInitializer.onApplicationEvent(event);
            mockedStatic.verify(() -> ScriptUtils.executeSqlScript(Mockito.eq(connection), Mockito.any(ClassPathResource.class)), Mockito.never());
        }
    }
}