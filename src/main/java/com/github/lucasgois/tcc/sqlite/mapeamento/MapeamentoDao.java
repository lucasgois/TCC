package com.github.lucasgois.tcc.sqlite.mapeamento;

import com.github.lucasgois.tcc.exce.TccRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.UUID;

import static com.github.lucasgois.tcc.sqlite.SqliteConnection.getConn;

@Slf4j
public class MapeamentoDao {

    private MapeamentoDao() {
    }

    private static boolean existe(@NotNull final String uuidVersao, @NotNull final String uuidLocalizacao) {

        try (final PreparedStatement statement = getConn().prepareStatement("SELECT count(uuid_mapeamento) = 1 AS existe FROM mapeamento WHERE uuid_versao = ? AND uuid_localizacao = ?")) {
            int i = 0;
            statement.setString(++i, uuidVersao);
            statement.setString(++i, uuidLocalizacao);

            log.info("{}", statement);
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

    public static void insertMapeamento(@NotNull final String uuidVersao, @NotNull final String uuidLocalizacao) {

        if (existe(uuidVersao, uuidLocalizacao)) {
            return;
        }

        final String criadoEm = LocalDateTime.now().toString();

        try (final PreparedStatement statement = getConn().prepareStatement("INSERT INTO mapeamento (uuid_mapeamento, uuid_versao, uuid_localizacao, criado_em, atualizado_em) VALUES (?, ?, ?, ?, ?)")) {
            int i = 0;
            statement.setString(++i, UUID.randomUUID().toString());
            statement.setString(++i, uuidVersao);
            statement.setString(++i, uuidLocalizacao);
            statement.setString(++i, criadoEm);
            statement.setString(++i, criadoEm);

            log.info("{}", statement);
            statement.executeUpdate();

        } catch (final SQLException ex) {
            throw new TccRuntimeException(ex);
        }
    }
}
