package com.github.lucasgois.tcc.sqlite.ambiente;

import com.github.lucasgois.tcc.util.Util;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

import java.security.NoSuchAlgorithmException;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class Ambiente {
    private String hash;
    private String nome;

    @NotNull
    public static Ambiente criar(@NotNull final String nome) throws NoSuchAlgorithmException {
        final Ambiente ambiente = new Ambiente();
        ambiente.hash = Util.calcularHash(nome.getBytes());
        ambiente.nome = nome;
        return ambiente;
    }
}