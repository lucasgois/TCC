package com.github.lucasgois.tcc.sqlite.arquivo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.nio.file.Path;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class Arquivo {
    private String hash;
    private Path caminho;
    private byte[] bytea;
}