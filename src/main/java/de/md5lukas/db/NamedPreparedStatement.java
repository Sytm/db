package de.md5lukas.db;

import com.google.common.base.Preconditions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Wrapper class for {@link PreparedStatement} that allows one to use named parameters instead of having to use indexed parameters
 */
public class NamedPreparedStatement {

    private final PreparedStatement preparedStatement;
    private final Map<String, int[]> indexMappings;

    /**
     * Creates a new PreparedStatement using the provided connection and sql string. This is recommended for a one time use
     *
     * @param connection The connection to use
     * @param sql        The sql string with to use
     * @throws SQLException If a database error occurs or the connection has been closed
     */
    public NamedPreparedStatement(Connection connection, String sql) throws SQLException {
        Preconditions.checkNotNull(connection, "The connection to use cannot be null");
        NamedParameterParseResult nppr = parseSQLString(sql);
        this.indexMappings = nppr.getIndexMappings();
        this.preparedStatement = connection.prepareStatement(nppr.getSubstitutedSql());
    }

    private NamedPreparedStatement(Connection connection, NamedParameterParseResult nppr) throws SQLException {
        this.indexMappings = nppr.getIndexMappings();
        this.preparedStatement = connection.prepareStatement(nppr.getSubstitutedSql());
    }

    /**
     * @return The {@link PreparedStatement} that is used internally
     */
    public PreparedStatement getPreparedStatement() {
        return preparedStatement;
    }

    /**
     * Executes the {@link PreparedStatement} as a query
     *
     * @return The result set returned by the query
     * @throws SQLException             If a database error occurs or the PreparedStatement has been closed
     * @throws NullPointerException     If the name is null
     * @throws IllegalArgumentException If the name has not been registered
     * @see PreparedStatement#executeQuery()
     */
    public ResultSet executeQuery() throws SQLException {
        return preparedStatement.executeQuery();
    }

    /**
     * Executes the {@link PreparedStatement} as an update
     *
     * @return The number of affected rows
     * @throws SQLException             If a database error occurs or the PreparedStatement has been closed
     * @throws NullPointerException     If the name is null
     * @throws IllegalArgumentException If the name has not been registered
     * @see PreparedStatement#executeUpdate()
     */
    public int executeUpdate() throws SQLException {
        return preparedStatement.executeUpdate();
    }

    /**
     * Sets the boolean for the named parameter
     *
     * @param name The name of the parameter
     * @param x    The value to set
     * @throws SQLException             If a database error occurs or the PreparedStatement has been closed
     * @throws NullPointerException     If the name is null
     * @throws IllegalArgumentException If the name has not been registered
     * @see PreparedStatement#setBoolean(int, boolean)
     */
    public void setBoolean(String name, boolean x) throws SQLException {
        for (int index : getIndexes(name)) {
            preparedStatement.setBoolean(index, x);
        }
    }

    /**
     * Sets the byte for the named parameter
     *
     * @param name The name of the parameter
     * @param x    The value to set
     * @throws SQLException             If a database error occurs or the PreparedStatement has been closed
     * @throws NullPointerException     If the name is null
     * @throws IllegalArgumentException If the name has not been registered
     * @see PreparedStatement#setByte(int, byte)
     */
    public void setByte(String name, byte x) throws SQLException {
        for (int index : getIndexes(name)) {
            preparedStatement.setByte(index, x);
        }
    }

    /**
     * Sets the short for the named parameter
     *
     * @param name The name of the parameter
     * @param x    The value to set
     * @throws SQLException             If a database error occurs or the PreparedStatement has been closed
     * @throws NullPointerException     If the name is null
     * @throws IllegalArgumentException If the name has not been registered
     * @see PreparedStatement#setShort(int, short)
     */
    public void setShort(String name, short x) throws SQLException {
        for (int index : getIndexes(name)) {
            preparedStatement.setShort(index, x);
        }
    }

    /**
     * Sets the integer for the named parameter
     *
     * @param name The name of the parameter
     * @param x    The value to set
     * @throws SQLException             If a database error occurs or the PreparedStatement has been closed
     * @throws NullPointerException     If the name is null
     * @throws IllegalArgumentException If the name has not been registered
     * @see PreparedStatement#setInt(int, int)
     */
    public void setInt(String name, int x) throws SQLException {
        for (int index : getIndexes(name)) {
            preparedStatement.setInt(index, x);
        }
    }

    /**
     * Sets the long for the named parameter
     *
     * @param name The name of the parameter
     * @param x    The value to set
     * @throws SQLException             If a database error occurs or the PreparedStatement has been closed
     * @throws NullPointerException     If the name is null
     * @throws IllegalArgumentException If the name has not been registered
     * @see PreparedStatement#setLong(int, long)
     */
    public void setLong(String name, long x) throws SQLException {
        for (int index : getIndexes(name)) {
            preparedStatement.setLong(index, x);
        }
    }

    /**
     * Sets the float for the named parameter
     *
     * @param name The name of the parameter
     * @param x    The value to set
     * @throws SQLException             If a database error occurs or the PreparedStatement has been closed
     * @throws NullPointerException     If the name is null
     * @throws IllegalArgumentException If the name has not been registered
     * @see PreparedStatement#setFloat(int, float)
     */
    public void setFloat(String name, float x) throws SQLException {
        for (int index : getIndexes(name)) {
            preparedStatement.setFloat(index, x);
        }
    }

    /**
     * Sets the double for the named parameter
     *
     * @param name The name of the parameter
     * @param x    The value to set
     * @throws SQLException             If a database error occurs or the PreparedStatement has been closed
     * @throws NullPointerException     If the name is null
     * @throws IllegalArgumentException If the name has not been registered
     * @see PreparedStatement#setDouble(int, double)
     */
    public void setDouble(String name, double x) throws SQLException {
        for (int index : getIndexes(name)) {
            preparedStatement.setDouble(index, x);
        }
    }

    /**
     * Sets the string for the named parameter
     *
     * @param name The name of the parameter
     * @param x    The value to set
     * @throws SQLException             If a database error occurs or the PreparedStatement has been closed
     * @throws NullPointerException     If the name is null
     * @throws IllegalArgumentException If the name has not been registered
     * @see PreparedStatement#setString(int, String)
     */
    public void setString(String name, String x) throws SQLException {
        for (int index : getIndexes(name)) {
            preparedStatement.setString(index, x);
        }
    }

    /**
     * Returns the index mappings for the provided parameter name.
     *
     * @param name The name of the parameter
     * @return An array containing all indexes in the PreparedStatement
     * @throws NullPointerException     If the name is null
     * @throws IllegalArgumentException If the name has not been registered
     */
    public int[] getIndexes(String name) {
        checkParameterName(name);
        return indexMappings.get(name);
    }

    private void checkParameterName(String name) {
        Preconditions.checkNotNull(name, "The name of the parameter to set cannot be null");
        Preconditions.checkArgument(indexMappings.containsKey(name), "The parameter name %s was provided, but is not registered", name);
    }

    /**
     * Parses the provided SQL string for named parameters and replaces them with <code>"?"</code> and creates index mappings based on the named parameter,
     * so it can be used for PreparedStatements.
     * <br><br>
     * Named parameters start with a double colon and then must be followed directly with a character that is valid according to
     * {@link Character#isJavaIdentifierStart(char)}. The further characters of the named parameter must be valid according to
     * {@link Character#isJavaIdentifierPart(char)}.
     * <br><br>
     * Example:<br>
     * <code>INSERT INTO table (id, name) VALUES :id, :name</code><br>
     * Would return the following substituted SQL string:<br>
     * <code>INSERT INTO table (id, name) VALUES ?, ?</code><br>
     * And the following index mappings:
     * <ul>
     *     <li><pre><code>id   -{@literal >} [ 1 ]</code></pre></li>
     *     <li><pre><code>name -{@literal >} [ 2 ]</code></pre></li>
     * </ul>
     *
     * @param sql The SQL string to parse
     * @return The substituted SQL string and index mappings in a wrapper object
     * @throws NullPointerException If the provided SQL string is <code>null</code>
     */
    public static NamedParameterParseResult parseSQLString(String sql) {
        Preconditions.checkNotNull(sql, "The SQL string to parse cannot be null");
        StringBuilder result = new StringBuilder(), parameterName = new StringBuilder();
        int parameterIndex = 1;
        Map<String, List<Integer>> indexCache = new HashMap<>();

        for (int index = 0; index < sql.length(); index++) {
            if (sql.charAt(index) == ':') {
                int parameterNameStart = index + 1;
                if (parameterNameStart < sql.length() &&
                        Character.isJavaIdentifierStart(sql.codePointAt(parameterNameStart))) {
                    index = parameterNameStart;

                    while (true) {
                        parameterName.append(sql.charAt(index));
                        int next = index + 1;
                        if (next < sql.length() && Character.isJavaIdentifierPart(sql.codePointAt(next))) {
                            index = next;
                        } else {
                            break;
                        }
                    }

                    String parameterNameString = parameterName.toString();
                    parameterName.setLength(0);
                    indexCache.computeIfAbsent(parameterNameString, k -> new ArrayList<>());
                    indexCache.get(parameterNameString).add(parameterIndex++);
                    result.append('?');
                } else {
                    result.append(sql.charAt(index));
                }
            } else {
                result.append(sql.charAt(index));
            }
        }

        Map<String, int[]> indexMappings = new HashMap<>();
        indexCache.forEach((key, indexes) -> {
            int[] indexArray = new int[indexes.size()];
            for (int j = 0; j < indexes.size(); j++) {
                indexArray[j] = indexes.get(j);
            }
            indexMappings.put(key, indexArray);
        });

        return new NamedParameterParseResult(result.toString(), indexMappings);
    }

    /**
     * Class containing the result of a parsed SQL string with named parameters
     */
    public static class NamedParameterParseResult {

        private final String substitutedSql;
        private final Map<String, int[]> indexMappings;

        private NamedParameterParseResult(String substitutedSql, Map<String, int[]> indexMappings) {
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
}
