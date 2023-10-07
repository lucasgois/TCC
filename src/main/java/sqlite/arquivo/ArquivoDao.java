package sqlite.arquivo;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.sqlite.SQLiteException;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static sqlite.SqliteConnection.getConn;

@Slf4j
public class ArquivoDao {

    private static final String QUERY_FIND_BY_HASH = "SELECT hash, bytea FROM arquivos WHERE hash = ?";
    private static final String QUERY_INSERT_ARQUIVO = "INSERT INTO arquivos (hash, bytea, criado_em, atualizado_em) VALUES (?, ?, ?, ?)";
    private static final String QUERY_INSERT_MAPEAMENTO = "INSERT INTO localizacoes (uuid_localizacao, hash_arquivo, caminho, criado_em, atualizado_em) VALUES (?, ?, ?, ?, ?)";
    private static final String QUERY_DELETE = "DELETE FROM arquivos WHERE hash = ?";

    private static void insertArquivo(@NotNull final Arquivo arquivo) throws SQLException, IOException {
        final String criadoEm = LocalDateTime.now().toString();

        try (final PreparedStatement statement = getConn().prepareStatement(QUERY_INSERT_ARQUIVO)) {
            statement.setString(1, arquivo.getHash());
            statement.setBytes(2, arquivo.getBytea());
            statement.setString(3, criadoEm);
            statement.setString(4, criadoEm);

            log.info("{}", statement);
            statement.executeUpdate();

        } catch (final SQLiteException ex) {

            if (!"A PRIMARY KEY constraint failed".equals(ex.getResultCode().message)) {
                throw ex;
            }
        }
    }

    private static void insertLocalizacao(@NotNull final Arquivo arquivo) throws SQLException, IOException {
        final String criadoEm = LocalDateTime.now().toString();

        try (final PreparedStatement statement = getConn().prepareStatement(QUERY_INSERT_MAPEAMENTO)) {

            int i = 0;
            statement.setString(++i, UUID.randomUUID().toString());
            statement.setString(++i, arquivo.getHash());
            statement.setString(++i, arquivo.getCaminho().toString());
            statement.setString(++i, criadoEm);
            statement.setString(++i, criadoEm);

            log.info("{}", statement);
            statement.executeUpdate();

        } catch (final SQLiteException ex) {

            if (!"A UNIQUE constraint failed".equals(ex.getResultCode().message)) {
                throw ex;
            }
        }
    }


    public Optional<Arquivo> findByHash(final String hash) throws SQLException, IOException {
        log.info("find by hash: {}", hash);

        try (final PreparedStatement statement = getConn().prepareStatement(QUERY_FIND_BY_HASH)) {
            statement.setString(1, hash);

            log.info("{}", statement);
            try (final ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    final Arquivo arquivo = new Arquivo();

                    arquivo.setHash(resultSet.getString("hash"));
                    arquivo.setBytea(resultSet.getBytes("bytea"));

                    return Optional.of(arquivo);

                } else {
                    return Optional.empty();
                }
            }
        }
    }

    public void insert(final Arquivo arquivo) throws SQLException, IOException {
        log.info("{}", arquivo);

        insertArquivo(arquivo);

        insertLocalizacao(arquivo);
    }

    public void delete(final String id) throws SQLException, IOException {

        try (final PreparedStatement statement = getConn().prepareStatement(QUERY_DELETE)) {
            statement.setString(1, id);

            log.info("{}", statement);
            statement.executeUpdate();
        }
    }
}
