package com.github.lucasgois.tcc.sqlite;

import org.junit.jupiter.api.*;
import com.github.lucasgois.tcc.sqlite.modulo.Modulo;
import com.github.lucasgois.tcc.sqlite.modulo.ModuloDao;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ModuloTest {

    private ModuloDao dao;

    @BeforeEach
    void setUp() {
        dao = new ModuloDao();
    }

    @Disabled
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
