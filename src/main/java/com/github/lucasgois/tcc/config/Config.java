package com.github.lucasgois.tcc.config;

import com.github.lucasgois.tcc.exceptions.TccRuntimeException;
import com.github.lucasgois.tcc.util.Util;
import lombok.Getter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Getter
public class Config {

    private final Path arquivoConfig = Path.of("config.txt");
    private final Path entrada = Path.of("entrada");
    private final Path saida = Path.of("saida");
    private String modulo;
    private String versao;
    private String ambiente;

    public Config() {
        criarOuLer();

        Util.criarPasta(entrada);

        Util.criarPasta(saida);
    }

    private void criarOuLer() {
        if (Files.notExists(arquivoConfig)) {
            criar();
        }
        ler();
    }

    private void ler() {
        final List<String> linhas;

        try {
            linhas = Files.readAllLines(arquivoConfig);
        } catch (IOException ex) {
            throw new TccRuntimeException(ex);
        }

        modulo = linhas.get(0);
        versao = linhas.get(1);
        ambiente = linhas.get(2);
    }

    private void criar() {
        final List<String> linhas = new ArrayList<>();

        linhas.add("Aplicação");
        linhas.add("1.0");
        linhas.add("Homologação");

        try {
            Files.write(arquivoConfig, linhas);
        } catch (IOException ex) {
            throw new TccRuntimeException(ex);
        }
    }
}
