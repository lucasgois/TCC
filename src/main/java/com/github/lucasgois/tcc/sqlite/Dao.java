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
    public static Arquivo arquivo(final String caminho) throws IOException, NoSuchAlgorithmException {
        final Path path = Path.of(caminho);

        if (Files.notExists(path)) {
            log.error("{}", path);
            log.error("{}", path.toAbsolutePath());
            throw new NoSuchFileException("Arquivo não encontrado: " + path);
        }

        final byte[] bytea = Files.readAllBytes(path);

        final Arquivo arquivo = new Arquivo();

        arquivo.setCaminho(path);
        arquivo.setHash(Util.calcularHash(bytea));
        arquivo.setBytea(bytea);

        return arquivo;
    }
}
