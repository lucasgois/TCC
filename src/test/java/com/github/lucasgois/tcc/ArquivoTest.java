package com.github.lucasgois.tcc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import sqlite.arquivo.Arquivo;
import sqlite.arquivo.ArquivoDao;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ArquivoTest {

    private ArquivoDao dao;

    @BeforeEach
    void setUp() {
        dao = new ArquivoDao();
    }

    @Test
    void insert_find_delete() throws Exception {
        final Arquivo arquivoOriginal = new Arquivo();
        arquivoOriginal.setHash("123");
        arquivoOriginal.setBytea("123".getBytes());

        dao.delete(arquivoOriginal.getHash());

        dao.insert(arquivoOriginal);

        final Optional<Arquivo> arquivoRecuperado = dao.findByHash(arquivoOriginal.getHash());

        if (arquivoRecuperado.isEmpty()) {
            fail("Arquivo deve ser inserido no banco");
        }

        assertEquals(arquivoOriginal.getHash(), arquivoRecuperado.get().getHash());
        assertArrayEquals(arquivoOriginal.getBytea(), arquivoRecuperado.get().getBytea());

        dao.delete(arquivoOriginal.getHash());

        final Optional<Arquivo> arquivoDelete = dao.findByHash(arquivoOriginal.getHash());

        if (arquivoDelete.isPresent()) {
            fail("Arquivo não deve existir no banco");
        }
    }
}
