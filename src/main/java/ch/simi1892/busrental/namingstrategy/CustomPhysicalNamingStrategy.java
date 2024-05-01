package ch.simi1892.busrental.namingstrategy;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

/**
 * Custom physical naming strategy for the application.
 */
public class CustomPhysicalNamingStrategy implements PhysicalNamingStrategy {

    @Override
    public Identifier toPhysicalCatalogName(Identifier name, JdbcEnvironment context) {
        return name;
    }

    @Override
    public Identifier toPhysicalSchemaName(Identifier name, JdbcEnvironment context) {
        return name;
    }

    @Override
    public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment context) {
        return convertWithPrefix(name, "T_", "DBO");
    }

    @Override
    public Identifier toPhysicalSequenceName(Identifier name, JdbcEnvironment context) {
        return convertWithPrefix(name, "SEQ_");
    }

    @Override
    public Identifier toPhysicalColumnName(Identifier name, JdbcEnvironment context) {
        return convertToSnakeCase(name);
    }

    /**
     * Convert the identifier with a prefix, removing a specified suffix if present.
     * @param identifier The identifier to convert.
     * @param prefix The prefix to add.
     * @param suffixToRemove The suffix to remove if present.
     * @return The converted identifier.
     */
    private Identifier convertWithPrefix(Identifier identifier, String prefix, String suffixToRemove) {
        if (identifier == null) {
            return null;
        }
        String newName = identifier.getText();
        if (newName.toUpperCase().endsWith(suffixToRemove)) {
            newName = newName.substring(0, newName.length() - suffixToRemove.length());
        }
        newName = prefix + newName.toUpperCase();
        return Identifier.toIdentifier(newName);
    }

    /**
     * Convert the identifier with a prefix.
     * @param identifier The identifier to convert.
     * @param prefix The prefix to add.
     * @return The converted identifier.
     */
    private Identifier convertWithPrefix(Identifier identifier, String prefix) {
        if (identifier == null) {
            return null;
        }
        String newName = identifier.getText().toUpperCase();
        newName = prefix + newName;
        return Identifier.toIdentifier(newName);
    }

    /**
     * Convert the identifier to snake case.
     * @param identifier The identifier to convert.
     * @return The converted identifier.
     */
    private Identifier convertToSnakeCase(Identifier identifier) {
        if (identifier == null) {
            return null;
        }
        String regex = "([a-z])([A-Z]+)";
        String replacement = "$1_$2";
        String newName = identifier.getText().replaceAll(regex, replacement).toLowerCase();
        return Identifier.toIdentifier(newName);
    }
}
