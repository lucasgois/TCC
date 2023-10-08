package com.github.lucasgois.tcc.sqlite.versao;

import com.github.lucasgois.tcc.exce.TccRuntimeException;
import com.github.lucasgois.tcc.localizacoes.LocalizacoesDao;
import com.github.lucasgois.tcc.sqlite.ambiente.Ambiente;
import com.github.lucasgois.tcc.sqlite.ambiente.AmbienteDao;
import com.github.lucasgois.tcc.sqlite.arquivo.Arquivo;
import com.github.lucasgois.tcc.sqlite.arquivo.ArquivoDao;
import com.github.lucasgois.tcc.sqlite.mapeamento.MapeamentoDao;
import com.github.lucasgois.tcc.sqlite.modulo.Modulo;
import com.github.lucasgois.tcc.sqlite.modulo.ModuloDao;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.github.lucasgois.tcc.sqlite.SqliteConnection.getConn;

@Slf4j
public class VersaoDao {

    private VersaoDao() {
    }

    private static String insertVersao(@NotNull final Versao versao) {

        final Optional<String> optional = buscarUUID(versao.getNome());

        if (optional.isPresent()) {
            return optional.get();
        }

        final String criadoEm = LocalDateTime.now().toString();

        try (final PreparedStatement statement = getConn().prepareStatement("INSERT INTO versoes (uuid_versao, nome, uuid_ambiente, uuid_modulo, criado_em, atualizado_em) VALUES (?, ?, ?, ?, ?, ?)")) {
            final String uuid = UUID.randomUUID().toString();

            int i = 0;
            statement.setString(++i, uuid);
            statement.setString(++i, versao.getNome());
            statement.setString(++i, versao.getAmbiente().getHash());
            statement.setString(++i, versao.getModulo().getHash());
            statement.setString(++i, criadoEm);
            statement.setString(++i, criadoEm);

            log.info("{}", statement);
            statement.executeUpdate();

            return uuid;

        } catch (final SQLException ex) {
            throw new TccRuntimeException(ex);
        }
    }

    public static Optional<String> buscarUUID(@NotNull final String nome) {

        try (final PreparedStatement statement = getConn().prepareStatement("SELECT uuid_versao FROM versoes WHERE nome = ?")) {
            int i = 0;
            statement.setString(++i, nome);

            log.info("{}", statement);

            try (final ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return Optional.of(resultSet.getString("uuid_versao"));
                } else {
                    return Optional.empty();
                }
            }

        } catch (SQLException ex) {
            throw new TccRuntimeException(ex);
        }
    }

    @NotNull
    public static List<InfoVersao> listar() {
        final List<InfoVersao> versoes = new ArrayList<>();

        final String query = """
                SELECT versoes.uuid_versao AS uuid, versoes.nome AS versao, modulos.nome AS modulo, ambientes.nome AS ambiente \
                FROM versoes \
                LEFT JOIN modulos on versoes.uuid_modulo = modulos.uuid_modulo \
                LEFT JOIN ambientes on versoes.uuid_ambiente = ambientes.uuid_ambiente \
                ORDER BY modulos.nome
                """;

        try (final PreparedStatement statement = getConn().prepareStatement(query)) {

            log.info("{}", statement);

            try (final ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    final InfoVersao info = new InfoVersao();
                    info.setUuid(resultSet.getString("uuid"));
                    info.setVersao(resultSet.getString("versao"));
                    info.setModulo(resultSet.getString("modulo"));
                    info.setAmbiente(resultSet.getString("ambiente"));

                    versoes.add(info);
                }
            }

        } catch (SQLException ex) {
            throw new TccRuntimeException(ex);
        }

        return versoes;
    }

    public static Optional<String> buscarUuid(final String versao, @NotNull final Modulo modulo, @NotNull final Ambiente ambiente) {
        final String sql = """
                SELECT versoes.uuid_versao FROM versoes \
                WHERE versoes.nome = ? \
                AND versoes.uuid_modulo = ? \
                AND versoes.uuid_ambiente = ? \
                """;

        try (final PreparedStatement statement = getConn().prepareStatement(sql)) {
            int i = 0;
            statement.setString(++i, versao);
            statement.setString(++i, modulo.getHash());
            statement.setString(++i, ambiente.getHash());

            try (final ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return Optional.of(resultSet.getString("uuid_versao"));

                } else {
                    return Optional.empty();
                }
            }

        } catch (SQLException ex) {
            throw new TccRuntimeException(ex);
        }
    }

    public static void buscar(final String uuidVersao, final Path caminhoVersao) {
        final List<String> localizacoes = MapeamentoDao.listaPorUuidVersao(uuidVersao);

        for (final String localizacao : localizacoes) {
            final Arquivo arquivo = LocalizacoesDao.buscarArquivo(localizacao);

            try {
                final Path caminhoArquivo = caminhoVersao.resolve(arquivo.getCaminho());

                Files.createDirectories(caminhoArquivo.getParent());

                if (Files.notExists(caminhoArquivo)) {
                    Files.write(caminhoArquivo, arquivo.getBytea());
                }

            } catch (IOException ex) {
                throw new TccRuntimeException(ex);
            }
        }
    }

    public static void insert(@NotNull final Versao versao) {
        new AmbienteDao().insert(versao.getAmbiente());

        new ModuloDao().insert(versao.getModulo());

        final String uuidVersao = insertVersao(versao);

        for (final Arquivo arquivo : versao.getArquivos()) {
            final String uuidLocalizacao = new ArquivoDao().insert(arquivo);
            MapeamentoDao.insertMapeamento(uuidVersao, uuidLocalizacao);
        }
    }
}