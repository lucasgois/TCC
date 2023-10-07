package com.github.lucasgois.tcc.sqlite.ambiente;

import com.github.lucasgois.tcc.exce.TccRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.sqlite.SQLiteException;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;

import static com.github.lucasgois.tcc.sqlite.SqliteConnection.getConn;

@Slf4j
public class AmbienteDao {

    private static final String QUERY_FIND_BY_HASH = "SELECT uuid_ambiente, nome FROM ambientes WHERE uuid_ambiente = ?";
    private static final String QUERY_INSERT = "INSERT INTO ambientes (uuid_ambiente, nome, criado_em, atualizado_em) VALUES (?, ?, ?, ?)";
    private static final String QUERY_DELETE = "DELETE FROM ambientes WHERE uuid_ambiente = ?";

    public Optional<Ambiente> findByHash(final String hash) throws SQLException, IOException {
        log.info("find by hash: {}", hash);

        try (final PreparedStatement statement = getConn().prepareStatement(QUERY_FIND_BY_HASH)) {
            statement.setString(1, hash);

            log.info("{}", statement);
            try (final ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    final Ambiente arquivo = new Ambiente();

                    arquivo.setHash(resultSet.getString("uuid_ambiente"));
                    arquivo.setNome(resultSet.getString("nome"));

                    return Optional.of(arquivo);

                } else {
                    return Optional.empty();
                }
            }
        }
    }

    public void insert(final Ambiente arquivo) {
        log.info("insert: {}", arquivo);

        final String dateTime = LocalDateTime.now().toString();

        try (final PreparedStatement statement = getConn().prepareStatement(QUERY_INSERT)) {
            statement.setString(1, arquivo.getHash());
            statement.setString(2, arquivo.getNome());
            statement.setString(3, dateTime);
            statement.setString(4, dateTime);

            log.info("{}", statement);
            statement.executeUpdate();

        } catch (final SQLiteException ex) {

            if (!"A UNIQUE constraint failed".equals(ex.getResultCode().message)) {
                throw new TccRuntimeException(ex);
            }

        } catch (final SQLException ex) {
            throw new TccRuntimeException(ex);
        }
    }

    public void delete(final String hash) throws SQLException, IOException {
        log.info("delete: {}", hash);

        try (final PreparedStatement statement = getConn().prepareStatement(QUERY_DELETE)) {
            statement.setString(1, hash);

            log.info("{}", statement);
            statement.executeUpdate();
        }
    }
}
