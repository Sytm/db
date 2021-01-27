package de.md5lukas.db;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Helper class for SQLite connections, which can optionally sub-classed
 */
public class SQLiteHelper {

    @Nullable
    private static Boolean available = null;

    /**
     * Checks if a SQLite driver is present without throwing an exception and then stores the result
     * in the case that it is requested again
     *
     * @return <code>true</code> if a driver is available, false otherwise
     */
    public static boolean checkAvailability() {
        if (available != null) {
            return available;
        }

        try {
            Class.forName("org.sqlite.JDBC");
            available = true;
        } catch (ClassNotFoundException cnfe) {
            available = false;
        }

        return available;
    }

    @NotNull
    private final File file;

    @NotNull
    private final Object connectionLock = new Object[0];

    @Nullable
    private Connection connection;

    /**
     * Creates a new instance of the SQLiteHelper with the given file. The SQLite database file will be the provided file
     *
     * @param file The file for the database
     * @throws IllegalStateException If a SQLite driver is not available
     */
    public SQLiteHelper(@NotNull File file) {
        if (!checkAvailability())
            throw new IllegalStateException("The SQLite driver is not available");

        Validator.checkNotNull(file, "The file for the SQLite database cannot be null");
        this.file = file;
    }

    /**
     * Returns a previously created Connection or a newly created Connection to the SQLite database
     *
     * @return A Connection to the SQLite database
     * @throws SQLException          If a database access error occurs
     */
    public final @NotNull Connection getConnection() throws SQLException {
        if (connection == null) {
            synchronized (connectionLock) {
                if (connection == null) {
                    connection = DriverManager.getConnection("jdbc:sqlite:" + file.getAbsolutePath());
                }
            }
        }

        return connection;
    }

    /**
     * Closes the connection to the database and then sets it to <code>null</code>
     */
    public void close() {
        try {
            if (connection == null || connection.isClosed()) {
                return;
            }
            synchronized (connectionLock) {
                if (connection == null || connection.isClosed()) {
                    return;
                }
                connection.close();
                connection = null;
            }

        } catch (SQLException ignored) {
        }
    }
}
