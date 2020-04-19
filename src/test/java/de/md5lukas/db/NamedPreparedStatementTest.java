package de.md5lukas.db;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class NamedPreparedStatementTest {

    @Test
    @DisplayName("SQL not modified if no named parameters are present")
    public void sqlNotModified() throws SQLException {
        String sql = "SELECT * FROM table";

        Connection connection = mock(Connection.class);

        NamedPreparedStatement nps = new NamedPreparedStatement(connection, sql);

        verify(connection).prepareStatement(sql);
    }

    @Test
    @DisplayName("SQL not modified if only a single double colon is present")
    public void sqlNotModifiedSingleColon() throws SQLException {
        String sql = "SELECT * FROM table WHERE id = :";

        Connection connection = mock(Connection.class);

        NamedPreparedStatement nps = new NamedPreparedStatement(connection, sql);

        verify(connection).prepareStatement(sql);
    }

    @Test
    public void noNamedParametersShouldRemain1() throws SQLException {
        Connection connection = mock(Connection.class);

        NamedPreparedStatement nps = new NamedPreparedStatement(connection, "SELECT * FROM table WHERE id = :param");

        verify(connection).prepareStatement("SELECT * FROM table WHERE id = ?");
    }

    @Test
    public void noNamedParametersShouldRemain2() throws SQLException {
        Connection connection = mock(Connection.class);

        NamedPreparedStatement nps = new NamedPreparedStatement(connection, "SELECT * FROM table WHERE id = :param AND other = :other");

        verify(connection).prepareStatement("SELECT * FROM table WHERE id = ? AND other = ?");
    }

    @Test
    public void singleParameterName() throws SQLException {
        Connection connection = mock(Connection.class);

        NamedPreparedStatement nps = new NamedPreparedStatement(connection, "SELECT * FROM table WHERE id = :param");

        assertArrayEquals(new int[] { 1 }, nps.getIndexes("param"));
    }

    @Test
    public void singleParameterNameOccursTwice() throws SQLException {
        Connection connection = mock(Connection.class);

        NamedPreparedStatement nps = new NamedPreparedStatement(connection, "SELECT * FROM table WHERE id = :param AND other = :param");

        assertArrayEquals(new int[] { 1, 2 }, nps.getIndexes("param"));
    }

    @Test
    public void twoParameterNames() throws SQLException {
        Connection connection = mock(Connection.class);

        NamedPreparedStatement nps = new NamedPreparedStatement(connection, "SELECT * FROM table WHERE id = :param AND other = :other");

        assertArrayEquals(new int[] { 1 }, nps.getIndexes("param"));
        assertArrayEquals(new int[] { 2 }, nps.getIndexes("other"));
    }

    @Test
    public void twoParameterNamesOccursTwice() throws SQLException {
        Connection connection = mock(Connection.class);

        NamedPreparedStatement nps = new NamedPreparedStatement(connection,
                "SELECT * FROM table WHERE id = :param AND other = :other AND o2 = :param AND o3 = :other");

        assertArrayEquals(new int[] { 1, 3 }, nps.getIndexes("param"));
        assertArrayEquals(new int[] { 2, 4 }, nps.getIndexes("other"));
    }
}
