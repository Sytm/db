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

public class NamedPreparedStatement {

    private String originalSql, parsedSql;
    private PreparedStatement preparedStatement;
    private Map<String, int[]> indexMappings;

    public NamedPreparedStatement(Connection connection, String sql) throws SQLException {
        this.originalSql = sql;
        parseSQL();
        this.preparedStatement = connection.prepareStatement(parsedSql);
    }

    public PreparedStatement getPreparedStatement() {
        return preparedStatement;
    }

    public ResultSet executeQuery() throws SQLException {
        return preparedStatement.executeQuery();
    }

    public int executeUpdate() throws SQLException {
        return preparedStatement.executeUpdate();
    }

    public void setBoolean(String name, boolean x) throws SQLException {
        for (int index : getIndexes(name)) {
            preparedStatement.setBoolean(index, x);
        }
    }

    public void setByte(String name, byte x) throws SQLException {
        for (int index : getIndexes(name)) {
            preparedStatement.setByte(index, x);
        }
    }

    public void setShort(String name, short x) throws SQLException {
        for (int index : getIndexes(name)) {
            preparedStatement.setShort(index, x);
        }
    }

    public void setInt(String name, int x) throws SQLException {
        for (int index : getIndexes(name)) {
            preparedStatement.setInt(index, x);
        }
    }

    public void setLong(String name, long x) throws SQLException {
        for (int index : getIndexes(name)) {
            preparedStatement.setLong(index, x);
        }
    }

    public void setFloat(String name, float x) throws SQLException {
        for (int index : getIndexes(name)) {
            preparedStatement.setFloat(index, x);
        }
    }

    public void setDouble(String name, double x) throws SQLException {
        for (int index : getIndexes(name)) {
            preparedStatement.setDouble(index, x);
        }
    }

    public void setString(String name, String x) throws SQLException {
        for (int index : getIndexes(name)) {
            preparedStatement.setString(index, x);
        }
    }

    public int[] getIndexes(String name) {
        checkParameterName(name);
        return indexMappings.get(name);
    }

    private void checkParameterName(String name) {
        Preconditions.checkNotNull(name, "The name of the parameter to set cannot be null");
        Preconditions.checkArgument(indexMappings.containsKey(name), "The parameter name %s was provided, but is not registered", name);
    }

    private void parseSQL() {
        StringBuilder result = new StringBuilder(), parameterName = new StringBuilder();
        int parameterIndex = 1;
        Map<String, List<Integer>> indexCache = new HashMap<>();

        for (int index = 0; index < originalSql.length(); index++) {
            if (originalSql.charAt(index) == ':') {
                int parameterNameStart = index + 1;
                if (parameterNameStart < originalSql.length() &&
                        Character.isJavaIdentifierStart(originalSql.codePointAt(parameterNameStart))) {
                    index = parameterNameStart;

                    while (true) {
                        parameterName.append(originalSql.charAt(index));
                        int next = index + 1;
                        if (next < originalSql.length() && Character.isJavaIdentifierPart(originalSql.codePointAt(next))) {
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
                    result.append(originalSql.charAt(index));
                }
            } else {
                result.append(originalSql.charAt(index));
            }
        }

        indexMappings = new HashMap<>();
        indexCache.forEach((key, indexes) -> {
            int[] indexArray = new int[indexes.size()];
            for (int j = 0; j < indexes.size(); j++) {
                indexArray[j] = indexes.get(j);
            }
            indexMappings.put(key, indexArray);
        });

        parsedSql = result.toString();
    }
}
