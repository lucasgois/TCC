package com.github.lucasgois.tcc.sqlite;

import com.github.lucasgois.tcc.sqlite.arquivo.Arquivo;
import com.github.lucasgois.tcc.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;

@Slf4j
public class Dao {

    private Dao() {
    }

    @NotNull
    public static Arquivo arquivo(@NotNull final String caminho) throws IOException, NoSuchAlgorithmException {
        final Path caminhoCompleto = Path.of(caminho);

        if (Files.notExists(caminhoCompleto)) {
            log.error("{}", caminhoCompleto);
            log.error("{}", caminhoCompleto.toAbsolutePath());
            throw new NoSuchFileException("Arquivo não encontrado: " + caminhoCompleto);
        }

        final byte[] bytea = Files.readAllBytes(caminhoCompleto);

        final Arquivo arquivo = new Arquivo();

        arquivo.setCaminho(caminhoCompleto.subpath(1, caminhoCompleto.getNameCount()));
        arquivo.setHash(Util.calcularHash(bytea));
        arquivo.setBytea(bytea);

        return arquivo;
    }
}
