package com.github.lucasgois.tcc.sqlite;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import com.github.lucasgois.tcc.sqlite.ambiente.Ambiente;
import com.github.lucasgois.tcc.sqlite.ambiente.AmbienteDao;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class AmbienteTest {

    private AmbienteDao dao;

    @BeforeEach
    void setUp() {
        dao = new AmbienteDao();
    }

    @Test
    void insert_find_delete() throws Exception {
        final Ambiente original = new Ambiente();
        original.setHash("123");
        original.setNome("123");

        dao.delete(original.getHash());

        dao.insert(original);

        final Optional<Ambiente> found = dao.findByHash(original.getHash());

        if (found.isEmpty()) {
            fail();
        }

        assertEquals(original.getHash(), found.get().getHash());
        assertEquals(original.getNome(), found.get().getNome());

        dao.delete(original.getHash());

        final Optional<Ambiente> deleted = dao.findByHash(original.getHash());

        if (deleted.isPresent()) {
            fail();
        }
    }
}
