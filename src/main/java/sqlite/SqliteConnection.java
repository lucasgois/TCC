package sqlite;

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
    private Connection connection;
    private boolean createDatabaseFlag = true;

    private SqliteConnection() {
    }

    public static SqliteConnection getInstance() {
        return INSTANCE;
    }


    public static Connection getConn() throws SQLException, IOException {
        return INSTANCE.getConnection();
    }

    public Connection getConnection() throws SQLException, IOException {

        if (!connected()) {
            connect();
        }
        return connection;
    }


    private boolean connected() throws SQLException {
        if (connection == null) return false;
        return connection.isValid(2);
    }

    private void connect() throws SQLException, IOException {
        Path url = Path.of(System.getProperty("user.home") + "\\tcc");

        if (Files.notExists(url)) {
            Files.createDirectories(url);
        }
        log.info("{}", url.toUri());

        url = url.resolve("tcc.db");

        connection = DriverManager.getConnection("jdbc:sqlite:" + url);

        if (createDatabaseFlag) {
            createDatabaseFlag = false;
            createDatabase();
        }
    }

    private void createDatabase() throws SQLException, IOException {

        try (final InputStream inputStream = getClass().getClassLoader().getResourceAsStream("database_scheme.sql")) {
            requireNonNull(inputStream, "Problema ao carregar esquema do banco de dados");

            final byte[] bytes = inputStream.readAllBytes();
            final String sqlContent = new String(bytes);

            try (final Statement statement = connection.createStatement()) {
                statement.executeUpdate(sqlContent);
            }
        }
    }
}