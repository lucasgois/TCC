package com.github.lucasgois.tcc.sqlite.arquivo;

import com.github.lucasgois.tcc.exceptions.TccRuntimeException;
import com.github.lucasgois.tcc.localizacoes.LocalizacoesDao;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;

import static com.github.lucasgois.tcc.sqlite.SqliteConnection.getConn;

@Slf4j
public class ArquivoDao {

    private static final String QUERY_INSERT_ARQUIVO = "INSERT INTO arquivos (hash, bytea, criado_em, atualizado_em) VALUES (?, ?, ?, ?)";
    private static final String QUERY_DELETE = "DELETE FROM arquivos WHERE hash = ?";

    private ArquivoDao() {
    }

    private static void insertArquivo(@NotNull final Arquivo arquivo) {

        final boolean existe = existe(arquivo.getHash());

        if (existe) {
            return;
        }

        final String criadoEm = LocalDateTime.now().toString();

        try (final PreparedStatement statement = getConn().prepareStatement(QUERY_INSERT_ARQUIVO)) {
            statement.setString(1, arquivo.getHash());
            statement.setBytes(2, arquivo.getBytea());
            statement.setString(3, criadoEm);
            statement.setString(4, criadoEm);

            log.info("{}\n", statement);
            statement.executeUpdate();

        } catch (final SQLException ex) {
            throw new TccRuntimeException(ex);
        }
    }

    public static boolean existe(final String hash) {

        try (final PreparedStatement statement = getConn().prepareStatement("SELECT count(1) = 1 AS existe FROM arquivos WHERE hash = ?")) {
            statement.setString(1, hash);

            log.info("{}\n", statement);

            try (final ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return resultSet.getBoolean("existe");

                } else {
                    return false;
                }
            }

        } catch (final SQLException ex) {
            throw new TccRuntimeException(ex);
        }
    }

    public static Optional<Arquivo> buscarArquivo(final String hash) {

        try (final PreparedStatement statement = getConn().prepareStatement("SELECT hash, bytea FROM arquivos WHERE hash = ?")) {
            statement.setString(1, hash);

            log.info("{}\n", statement);

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

        } catch (final SQLException ex) {
            throw new TccRuntimeException(ex);
        }
    }

    public static String insert(final Arquivo arquivo) {
        insertArquivo(arquivo);

        return LocalizacoesDao.insert(arquivo.getHash(), arquivo.getCaminho().toString());
    }

    public static void delete(final String id) {

        try (final PreparedStatement statement = getConn().prepareStatement(QUERY_DELETE)) {
            statement.setString(1, id);

            log.info("{}\n", statement);
            statement.executeUpdate();

        } catch (final SQLException ex) {
            throw new TccRuntimeException(ex);
        }
    }
}
