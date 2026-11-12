package com.codequest.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class DBConnection {
    private static final Properties CONFIG = new Properties();

    static {
        try (InputStream stream = DBConnection.class.getClassLoader().getResourceAsStream("database.properties")) {
            if (stream == null) throw new IllegalStateException("Missing src/database.properties");
            CONFIG.load(stream);
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception exception) {
            throw new ExceptionInInitializerError(exception);
        }
    }

    private DBConnection() { }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(CONFIG.getProperty("db.url"),
                CONFIG.getProperty("db.username"), CONFIG.getProperty("db.password"));
    }
}
