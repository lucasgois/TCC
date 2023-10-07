package sqlite;

import com.github.lucasgois.tcc.util.Util;
import org.jetbrains.annotations.NotNull;
import sqlite.arquivo.Arquivo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;

public class Dao {

    private Dao() {
    }

    @NotNull
    public static Arquivo arquivo(final String caminho) throws IOException, NoSuchAlgorithmException {
        final Path path = Path.of(caminho);

        if (Files.notExists(path)) {
            throw new IllegalStateException();
        }

        final byte[] bytea = Files.readAllBytes(path);

        final Arquivo arquivo = new Arquivo();

        arquivo.setCaminho(path);
        arquivo.setHash(Util.calcularHash(bytea));
        arquivo.setBytea(bytea);

        return arquivo;
    }
}
