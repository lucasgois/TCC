package com.github.lucasgois.tcc.sqlite;

import com.github.lucasgois.tcc.exce.TccRuntimeException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static java.util.Objects.requireNonNull;

@Slf4j
public class SqliteConnection {

    private static final SqliteConnection INSTANCE = new SqliteConnection();
    private static final String DATABASE_SCHEME_FILE = "database_scheme.sql";
    private Connection connection;
    private boolean createDatabaseFlag = true;

    private SqliteConnection() {
    }

    public static SqliteConnection getInstance() {
        return INSTANCE;
    }


    public static Connection getConn() {
        return INSTANCE.getConnection();
    }

    public Connection getConnection() {

        if (!connected()) {
            connect();
        }
        return connection;
    }


    private boolean connected() {

        if (connection == null) return false;

        try {
            return connection.isValid(2);
        } catch (final SQLException ex) {
            throw new TccRuntimeException(ex);
        }
    }

    private void connect() {
        Path url = Path.of(System.getProperty("user.home") + "\\tcc");

        if (Files.notExists(url)) {
            try {
                Files.createDirectories(url);
            } catch (IOException ex) {
                throw new TccRuntimeException("Não foi possível criar o diretório: " + url, ex);
            }
        }
        log.info("{}", url.toUri());

        url = url.resolve("tcc.db");

        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + url);
        } catch (SQLException ex) {
            throw new TccRuntimeException("Não foi possível conectar ao banco: " + url, ex);
        }

        if (createDatabaseFlag) {
            createDatabaseFlag = false;
            createDatabase();
        }
    }

    private void createDatabase() {

        try (final InputStream inputStream = getClass().getClassLoader().getResourceAsStream(DATABASE_SCHEME_FILE)) {
            requireNonNull(inputStream, "Problema ao carregar esquema do banco de dados");

            final byte[] bytes = inputStream.readAllBytes();
            final String sqlContent = new String(bytes);

            try (final Statement statement = connection.createStatement()) {
                statement.executeUpdate(sqlContent);
            }

        } catch (Exception ex) {
            throw new TccRuntimeException(ex);
        }
    }
}