package ch.simi1892.busrental.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Component
public class DataInitializer implements ApplicationListener<ContextRefreshedEvent> {

    @Value("${spring.profiles.active}")
    private String environment;

    private final DataSource dataSource;

    public DataInitializer(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if ("DEV".equalsIgnoreCase(environment)) {
            try (Connection conn = dataSource.getConnection()) {
                ScriptUtils.executeSqlScript(conn, new ClassPathResource("data/data.sql"));
            } catch (SQLException ex) {
                throw new RuntimeException("Failed to execute SQL script", ex);
            }
        }
    }
}
