package com.github.lucasgois.tcc.sqlite.modulo;

import com.github.lucasgois.tcc.util.Util;
import lombok.*;
import org.jetbrains.annotations.NotNull;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Modulo {

    private String hash;
    private String nome;

    @NotNull
    public static Modulo criar(@NotNull final String nome) {
        final Modulo modulo = new Modulo();
        modulo.hash = Util.calcularHash(nome.getBytes());
        modulo.nome = nome;
        return modulo;
    }
}