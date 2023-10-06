package sqlite.arquivo;

import com.github.lucasgois.tcc.excep.TccException;
import lombok.extern.slf4j.Slf4j;
import sqlite.SqliteConnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
public class ArquivoDao {

    private static final String QUERY_FIND_BY_HASH = "SELECT file_registry_hash, file_registry_bytea FROM file_registries WHERE file_registry_hash = ?";
    private static final String QUERY_INSERT = "INSERT INTO file_registries (file_registry_hash, file_registry_bytea, file_registry_created_at, file_registry_updated_at) VALUES (?, ?, ?, ?)";
    private static final String QUERY_DELETE = "DELETE FROM file_registries WHERE file_registry_hash = ?";
    private final SqliteConnection sqliteConnection = SqliteConnection.getInstance();

    public Optional<Arquivo> findByHash(final String hash) throws SQLException, IOException, TccException {
        log.info("find by hash: {}", hash);

        final Connection conn = sqliteConnection.getConnection();

        try (final PreparedStatement statement = conn.prepareStatement(QUERY_FIND_BY_HASH)) {
            statement.setString(1, hash);

            log.info("{}", statement);
            try (final ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    final Arquivo arquivo = new Arquivo();

                    arquivo.setHash(resultSet.getString("file_registry_hash"));
                    arquivo.setBytea(resultSet.getBytes("file_registry_bytea"));

                    return Optional.of(arquivo);

                } else {
                    return Optional.empty();
                }
            }
        }
    }

    public void insert(final Arquivo arquivo) throws SQLException, IOException, TccException {
        log.info("insert: {}", arquivo);

        final String dateTime = LocalDateTime.now().toString();

        final Connection conn = sqliteConnection.getConnection();

        try (final PreparedStatement statement = conn.prepareStatement(QUERY_INSERT)) {
            statement.setString(1, arquivo.getHash());
            statement.setBytes(2, arquivo.getBytea());
            statement.setString(3, dateTime);
            statement.setString(4, dateTime);

            log.info("{}", statement);
            statement.executeUpdate();
        }
    }

    public void delete(final String hash) throws SQLException, IOException, TccException {
        log.info("delete: {}", hash);

        final Connection conn = sqliteConnection.getConnection();

        try (final PreparedStatement statement = conn.prepareStatement(QUERY_DELETE)) {
            statement.setString(1, hash);

            log.info("{}", statement);
            statement.executeUpdate();
        }
    }
}
