package com.github.lucasgois.tcc;

import com.github.lucasgois.tcc.config.Config;
import com.github.lucasgois.tcc.sqlite.Dao;
import com.github.lucasgois.tcc.sqlite.ambiente.Ambiente;
import com.github.lucasgois.tcc.sqlite.modulo.Modulo;
import com.github.lucasgois.tcc.sqlite.versao.Versao;
import com.github.lucasgois.tcc.sqlite.versao.VersaoDao;
import com.github.lucasgois.tcc.util.Util;
import javafx.util.Pair;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

@Slf4j
public class App {

    public static void main(String @NotNull [] args) throws Exception {
        final Config config = new Config();

        if ("entrada".equals(args[0])) {
            final List<Pair<String, String>> arquivos = Util.criarListaDeArquivoEHash(config.getEntrada());

            final Versao versao = new Versao();
            versao.setNome(config.getVersao());
            versao.setModulo(Modulo.criar(config.getModulo()));
            versao.setAmbiente(Ambiente.criar(config.getAmbiente()));

            for (final Pair<String, String> arquivo : arquivos) {
                versao.getArquivos().add(Dao.arquivo(config.getEntrada() + arquivo.getValue()));
            }

            VersaoDao.insert(versao);

        } else if ("saida".equals(args[0])) {
            final Optional<String> optional = VersaoDao.buscarUuidPoVersaoModuloAmbiente(config.getVersao(), Modulo.criar(config.getModulo()), Ambiente.criar(config.getAmbiente()));

            if (optional.isPresent()) {
                VersaoDao.buscar(optional.get(), config.getSaida());

            } else {
                log.error("Versão não encontrada");
            }
        }
    }
}
