package com.github.lucasgois.tcc.sqlite.modulo;

import com.github.lucasgois.tcc.exceptions.TccRuntimeException;
import com.github.lucasgois.tcc.sqlite.SqliteConnection;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.sqlite.SQLiteException;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
public class ModuloDao {

    private static final String QUERY_FIND_BY_HASH = "SELECT uuid_modulo, nome FROM modulos WHERE uuid_modulo = ?";
    private static final String QUERY_INSERT = "INSERT INTO modulos (uuid_modulo, nome, criado_em, atualizado_em) VALUES (?, ?, ?, ?)";
    private static final String QUERY_DELETE = "DELETE FROM modulos WHERE uuid_modulo = ?";

    private final SqliteConnection sqliteConnection = SqliteConnection.getInstance();

    public Optional<Modulo> findByHash(final String hash) throws SQLException, IOException {
        log.info("find by hash: {}", hash);

        final Connection conn = sqliteConnection.getConnection();

        try (final PreparedStatement statement = conn.prepareStatement(QUERY_FIND_BY_HASH)) {
            statement.setString(1, hash);

            log.info("{}", statement);
            try (final ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    final Modulo modulo = new Modulo();
                    modulo.setHash(resultSet.getString("uuid_modulo"));
                    modulo.setNome(resultSet.getString("nome"));

                    return Optional.of(modulo);

                } else {
                    return Optional.empty();
                }
            }
        }
    }


    public void insert(@NotNull final Modulo arquivo) {
        final String dateTime = LocalDateTime.now().toString();

        final Connection conn = sqliteConnection.getConnection();

        try (final PreparedStatement statement = conn.prepareStatement(QUERY_INSERT)) {
            statement.setString(1, arquivo.getHash());
            statement.setString(2, arquivo.getNome());
            statement.setString(3, dateTime);
            statement.setString(4, dateTime);

            log.info("{}\n", statement);
            statement.executeUpdate();

        } catch (final SQLiteException ex) {

            if (!"A UNIQUE constraint failed".equals(ex.getResultCode().message)) {
                throw new TccRuntimeException(ex);
            }

        } catch (final SQLException ex) {
            throw new TccRuntimeException(ex);
        }
    }

    public void delete(final String id) throws SQLException, IOException {
        final Connection conn = sqliteConnection.getConnection();

        try (final PreparedStatement statement = conn.prepareStatement(QUERY_DELETE)) {
            statement.setString(1, id);

            log.info("{}", statement);
            statement.executeUpdate();
        }
    }
}
