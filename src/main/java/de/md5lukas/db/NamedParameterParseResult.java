package de.md5lukas.db;

import com.google.common.base.Preconditions;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

/**
 * Class containing the result of a parsed SQL string with named parameters
 */
public class NamedParameterParseResult {

    private final String substitutedSql;
    private final Map<String, int[]> indexMappings;

    NamedParameterParseResult(String substitutedSql, Map<String, int[]> indexMappings) {
        this.substitutedSql = substitutedSql;
        this.indexMappings = indexMappings;
    }

    /**
     * In the returned SQL string the named parameters are replaced with <code>"?"</code> so they can be used with PreparedStatements
     *
     * @return The string that can be used to create a PreparedStatement
     */
    public String getSubstitutedSql() {
        return substitutedSql;
    }

    /**
     * The returned map contains translations from the named parameter to the associated indexes in the sql statement
     *
     * @return The index mappings
     */
    public Map<String, int[]> getIndexMappings() {
        return indexMappings;
    }

    /**
     * Creates a new NamedPreparedStatement using the already parsed SQL string
     *
     * @param connection The connection to create a PreparedStatement on
     * @return A new NamedPreparedStatement using this SQL string
     * @throws SQLException If a database error occurs or the connection has been closed
     */
    public NamedPreparedStatement createStatement(Connection connection) throws SQLException {
        Preconditions.checkNotNull(connection, "The connection to use cannot be null");
        return new NamedPreparedStatement(connection, this);
    }
}