package com.github.lucasgois.tcc.sqlite;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import sqlite.modulo.Modulo;
import sqlite.modulo.ModuloDao;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ModuloTest {

    private ModuloDao dao;

    @BeforeEach
    void setUp() {
        dao = new ModuloDao();
    }

    @Test
    void insert_find_delete() throws Exception {
        final Modulo original = new Modulo();
        original.setHash("123");
        original.setNome("123");

        dao.delete(original.getHash());

        dao.insert(original);

        final Optional<Modulo> found = dao.findByHash(original.getHash());

        if (found.isEmpty()) {
            fail();
        }

        assertEquals(original.getHash(), found.get().getHash());
        assertEquals(original.getNome(), found.get().getNome());

        dao.delete(original.getHash());

        final Optional<Modulo> deleted = dao.findByHash(original.getHash());

        if (deleted.isPresent()) {
            fail();
        }
    }
}