package com.github.lucasgois.tcc.sqlite;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import com.github.lucasgois.tcc.sqlite.arquivo.Arquivo;
import com.github.lucasgois.tcc.sqlite.arquivo.ArquivoDao;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ArquivoTest {

    private ArquivoDao dao;

    @BeforeEach
    void setUp() {
        dao = new ArquivoDao();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "src/test/resources/entrada/Arquivo1.txt",
            "src/test/resources/entrada/Arquivo2.txt",
            "src/test/resources/entrada/Arquivo3.txt",
            "src/test/resources/entrada/pasta/Arquivo1.txt"
    })
    void insert_find_delete(final String caminho) throws Exception {
        final Arquivo arquivoOriginal = Dao.arquivo(caminho);

        dao.insert(arquivoOriginal);

        final Optional<Arquivo> arquivoRecuperado = dao.buscarArquivo(arquivoOriginal.getHash());

        if (arquivoRecuperado.isEmpty()) {
            fail("Arquivo deve ser inserido no banco");
        }

        assertEquals(arquivoOriginal.getHash(), arquivoRecuperado.get().getHash());
        assertArrayEquals(arquivoOriginal.getBytea(), arquivoRecuperado.get().getBytea());

        dao.delete(arquivoOriginal.getHash());

        final Optional<Arquivo> arquivoDelete = dao.buscarArquivo(arquivoOriginal.getHash());

        if (arquivoDelete.isPresent()) {
            fail("Arquivo não deve existir no banco");
        }
    }
}
