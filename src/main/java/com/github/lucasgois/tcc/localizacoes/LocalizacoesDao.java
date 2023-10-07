package com.github.lucasgois.tcc.localizacoes;

import com.github.lucasgois.tcc.exce.TccRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static com.github.lucasgois.tcc.sqlite.SqliteConnection.getConn;

@Slf4j
public class LocalizacoesDao {

    private LocalizacoesDao() {
    }

    @NotNull
    public static Optional<String> buscarLocalizacao(String hash, String caminho) {

        try (final PreparedStatement statement = getConn().prepareStatement("SELECT uuid_localizacao FROM localizacoes WHERE hash_arquivo = ? AND localizacoes.caminho = ?")) {
            int i = 0;
            statement.setString(++i, hash);
            statement.setString(++i, caminho);

            log.info("{}", statement);

            try (final ResultSet resultSet = statement.executeQuery()) {

                if (!resultSet.next()) {
                    return Optional.empty();
                }

                return Optional.of(resultSet.getString("uuid_localizacao"));
            }

        } catch (final SQLException ex) {
            throw new TccRuntimeException(ex);
        }
    }

    public static String insert(@NotNull final String hash, @NotNull final String caminho) {
        final Optional<String> optionalUuid = buscarLocalizacao(hash, caminho);

        if (optionalUuid.isPresent()) {
            return optionalUuid.get();
        }

        final String uuid = UUID.randomUUID().toString();
        final String criadoEm = LocalDateTime.now().toString();

        try (final PreparedStatement statement = getConn().prepareStatement("INSERT INTO localizacoes (uuid_localizacao, hash_arquivo, caminho, criado_em, atualizado_em) VALUES (?, ?, ?, ?, ?)")) {

            int i = 0;
            statement.setString(++i, uuid);
            statement.setString(++i, hash);
            statement.setString(++i, caminho);
            statement.setString(++i, criadoEm);
            statement.setString(++i, criadoEm);

            log.info("{}", statement);
            statement.executeUpdate();

        } catch (final SQLException ex) {
            throw new TccRuntimeException(ex);
        }

        return uuid;
    }
}