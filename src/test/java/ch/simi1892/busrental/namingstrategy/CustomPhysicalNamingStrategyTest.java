package ch.simi1892.busrental.namingstrategy;

import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import org.hibernate.boot.model.naming.Identifier;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockitoAnnotations;

class CustomPhysicalNamingStrategyTest {
    @Mock
    private JdbcEnvironment jdbcEnvironment;

    private CustomPhysicalNamingStrategy namingStrategy;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        namingStrategy = new CustomPhysicalNamingStrategy();
    }

    @Test
    public void testToPhysicalTableNameWithoutSuffix() {
        Identifier original = Identifier.toIdentifier("myTable");
        Identifier transformed = namingStrategy.toPhysicalTableName(original, jdbcEnvironment);
        Assertions.assertEquals("T_MYTABLE", transformed.getText());
    }

    @Test
    public void testToPhysicalTableNameWithSuffix() {
        Identifier original = Identifier.toIdentifier("myTableDBO");
        Identifier transformed = namingStrategy.toPhysicalTableName(original, jdbcEnvironment);
        Assertions.assertEquals("T_MYTABLE", transformed.getText());
    }

    @Test
    public void testToPhysicalTableNameWithSuffixCaseInsensitive() {
        Identifier original = Identifier.toIdentifier("myTabledbo");
        Identifier transformed = namingStrategy.toPhysicalTableName(original, jdbcEnvironment);
        Assertions.assertEquals("T_MYTABLE", transformed.getText());
    }

    @Test
    public void testToPhysicalSequenceName() {
        Identifier original = Identifier.toIdentifier("mySequence");
        Identifier transformed = namingStrategy.toPhysicalSequenceName(original, jdbcEnvironment);
        Assertions.assertEquals("SEQ_MYSEQUENCE", transformed.getText());
    }

    @Test
    public void testToPhysicalColumnName() {
        Identifier original = Identifier.toIdentifier("streetNr");
        Identifier transformed = namingStrategy.toPhysicalColumnName(original, jdbcEnvironment);
        Assertions.assertEquals("street_nr", transformed.getText());
    }

    @Test
    public void testToPhysicalColumnNameWithComplexCamelCase() {
        Identifier original = Identifier.toIdentifier("myColumnData");
        Identifier transformed = namingStrategy.toPhysicalColumnName(original, jdbcEnvironment);
        Assertions.assertEquals("my_column_data", transformed.getText());
    }

    @Test
    public void testNullIdentifierReturnsNull() {
        Assertions.assertNull(namingStrategy.toPhysicalTableName(null, jdbcEnvironment));
        Assertions.assertNull(namingStrategy.toPhysicalSequenceName(null, jdbcEnvironment));
        Assertions.assertNull(namingStrategy.toPhysicalColumnName(null, jdbcEnvironment));
    }
}