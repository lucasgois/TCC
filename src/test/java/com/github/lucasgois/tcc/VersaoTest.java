package com.github.lucasgois.tcc;

import com.github.lucasgois.tcc.sqlite.Dao;
import com.github.lucasgois.tcc.sqlite.ambiente.Ambiente;
import com.github.lucasgois.tcc.sqlite.modulo.Modulo;
import com.github.lucasgois.tcc.sqlite.versao.InfoVersao;
import com.github.lucasgois.tcc.sqlite.versao.Versao;
import com.github.lucasgois.tcc.sqlite.versao.VersaoDao;
import com.github.lucasgois.tcc.util.Util;
import javafx.util.Pair;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.List;

@Slf4j
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class VersaoTest {

    @Disabled
    @Test
    void cadastrar_versao() throws Exception {
        final Modulo modulo = Modulo.criar("Aplicação A");

        final Ambiente ambiente = Ambiente.criar("Produção");

        final Path caminho = Path.of("src/test/resources/entrada");
        final List<Pair<String, String>> arquivos = Util.criarListaDeArquivoEHash(caminho);

        final Versao versao = new Versao();
        versao.setNome("2310.2");
        versao.setModulo(modulo);
        versao.setAmbiente(ambiente);

        for (final Pair<String, String> arquivo : arquivos) {
            log.info("{}", arquivo);
            versao.getArquivos().add(Dao.arquivo(caminho + arquivo.getValue()));
        }

        VersaoDao.insert(versao);
    }

    @Disabled
    @Test
    void listar_versoes() throws Exception {
        final Path saida = Path.of("src/test/resources/saida");

        final List<InfoVersao> versoes = VersaoDao.listar();

        for (final InfoVersao versao : versoes) {
            log.info("{}", versao);

            final Path caminhoVersao = saida.resolve(versao.getVersao());

            Util.criarPasta(caminhoVersao);

            VersaoDao.buscar(versao.getUuid(), caminhoVersao);
        }
    }
}
