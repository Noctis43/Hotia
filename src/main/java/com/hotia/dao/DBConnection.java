package com.hotia.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DBConnection {

    private static final String URL = "lien de connection de votre base de donnees";
    private static final String USER = "nom d'utilisateur de votre base de donnees";
    private static final String PASSWORD = "mot de passe de votre base de donnees";

    private DBConnection() {}

    public static Connection getConnection() throws SQLException {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver MySQl introuvable !!", e);
        }

        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
