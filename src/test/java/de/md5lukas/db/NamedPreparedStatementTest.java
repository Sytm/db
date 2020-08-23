package de.md5lukas.db;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class NamedPreparedStatementTest {

    @Test
    @DisplayName("SQL not modified if no named parameters are present")
    public void sqlNotModified() {
        String sql = "SELECT * FROM table";

        NamedParameterParseResult nppr = NamedPreparedStatement.parseSQLString(sql);

        assertEquals(sql, nppr.getSubstitutedSql());
        assertEquals(0, nppr.getIndexMappings().size());
    }

    @Test
    @DisplayName("SQL not modified if only a single double colon is present")
    public void sqlNotModifiedSingleColon() {
        String sql = "SELECT * FROM table WHERE id = :";

        NamedParameterParseResult nppr = NamedPreparedStatement.parseSQLString(sql);

        assertEquals(sql, nppr.getSubstitutedSql());
        assertEquals(0, nppr.getIndexMappings().size());
    }

    @Test
    public void singleParameterName() {
        NamedParameterParseResult nppr = NamedPreparedStatement.parseSQLString("SELECT * FROM table WHERE id = :param");

        assertEquals("SELECT * FROM table WHERE id = ?", nppr.getSubstitutedSql());
        assertEquals(1, nppr.getIndexMappings().size());
        assertArrayEquals(new int[] { 1 }, nppr.getIndexMappings().get("param"));
    }

    @Test
    public void singleParameterNameOccursTwice() {
        NamedParameterParseResult nppr = NamedPreparedStatement.parseSQLString("SELECT * FROM table WHERE id = :param AND other = :param");

        assertEquals("SELECT * FROM table WHERE id = ? AND other = ?", nppr.getSubstitutedSql());
        assertEquals(1, nppr.getIndexMappings().size());
        assertArrayEquals(new int[] { 1, 2 }, nppr.getIndexMappings().get("param"));
    }

    @Test
    public void twoParameterNames() {
        NamedParameterParseResult nppr = NamedPreparedStatement.parseSQLString("SELECT * FROM table WHERE id = :param AND other = :other");

        assertEquals("SELECT * FROM table WHERE id = ? AND other = ?", nppr.getSubstitutedSql());
        assertEquals(2, nppr.getIndexMappings().size());
        assertArrayEquals(new int[] { 1 }, nppr.getIndexMappings().get("param"));
        assertArrayEquals(new int[] { 2 }, nppr.getIndexMappings().get("other"));
    }

    @Test
    public void twoParameterNamesOccursTwice() {
        NamedParameterParseResult nppr = NamedPreparedStatement
                .parseSQLString("SELECT * FROM table WHERE id = :param AND other = :other AND o2 = :param AND o3 = :other");

        assertEquals("SELECT * FROM table WHERE id = ? AND other = ? AND o2 = ? AND o3 = ?", nppr.getSubstitutedSql());
        assertEquals(2, nppr.getIndexMappings().size());
        assertArrayEquals(new int[] { 1, 3 }, nppr.getIndexMappings().get("param"));
        assertArrayEquals(new int[] { 2, 4 }, nppr.getIndexMappings().get("other"));
    }
}
