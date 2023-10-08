package com.github.lucasgois.tcc.sqlite;

import com.github.lucasgois.tcc.sqlite.arquivo.Arquivo;
import com.github.lucasgois.tcc.sqlite.arquivo.ArquivoDao;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ArquivoTest {

    @Disabled
    @ParameterizedTest
    @ValueSource(strings = {
            "src/test/resources/entrada/Arquivo1.txt",
            "src/test/resources/entrada/Arquivo2.txt",
            "src/test/resources/entrada/Arquivo3.txt",
            "src/test/resources/entrada/pasta/Arquivo1.txt"
    })
    void insert_find_delete(final String caminho) throws Exception {
        final Arquivo arquivoOriginal = Dao.arquivo(caminho);

        ArquivoDao.insert(arquivoOriginal);

        final Optional<Arquivo> arquivoRecuperado = ArquivoDao.buscarArquivo(arquivoOriginal.getHash());

        if (arquivoRecuperado.isEmpty()) {
            fail("Arquivo deve ser inserido no banco");
        }

        assertEquals(arquivoOriginal.getHash(), arquivoRecuperado.get().getHash());
        assertArrayEquals(arquivoOriginal.getBytea(), arquivoRecuperado.get().getBytea());

        ArquivoDao.delete(arquivoOriginal.getHash());

        final Optional<Arquivo> arquivoDelete = ArquivoDao.buscarArquivo(arquivoOriginal.getHash());

        if (arquivoDelete.isPresent()) {
            fail("Arquivo não deve existir no banco");
        }
    }
}
