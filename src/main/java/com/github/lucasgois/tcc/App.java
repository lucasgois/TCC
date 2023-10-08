package com.github.lucasgois.tcc;

import com.github.lucasgois.tcc.sqlite.Dao;
import com.github.lucasgois.tcc.sqlite.ambiente.Ambiente;
import com.github.lucasgois.tcc.sqlite.modulo.Modulo;
import com.github.lucasgois.tcc.sqlite.versao.Versao;
import com.github.lucasgois.tcc.sqlite.versao.VersaoDao;
import com.github.lucasgois.tcc.util.Util;
import javafx.util.Pair;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@Slf4j
public class App {

    public static void main(String[] args) throws Exception {
        final List<String> linhas = Files.readAllLines(Path.of("config.txt"));

        if ("in".equals(args[0])) {

            final Path caminho = Path.of(linhas.get(3));
            final List<Pair<String, String>> arquivos = Util.listFilesWithHashes(caminho);

            final Versao versao = new Versao();
            versao.setNome(linhas.get(1));
            versao.setModulo(Modulo.criar(linhas.get(0)));
            versao.setAmbiente(Ambiente.criar(linhas.get(2)));

            for (final Pair<String, String> arquivo : arquivos) {
                versao.getArquivos().add(Dao.arquivo(caminho + arquivo.getValue()));
            }

            VersaoDao.insert(versao);

        } else if ("out".equals(args[0])) {
            final Path caminho = Path.of(linhas.get(3));

            final Optional<String> optional = VersaoDao.buscarUuid(linhas.get(1), Modulo.criar(linhas.get(0)), Ambiente.criar(linhas.get(2)));

            if (optional.isPresent()) {
                VersaoDao.buscar(optional.get(), caminho);

            } else {
                log.error("Versão não encontrada");
            }
        }
    }
}
