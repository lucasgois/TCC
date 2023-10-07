package com.github.lucasgois.tcc.sqlite.versao;

import com.github.lucasgois.tcc.sqlite.arquivo.Arquivo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import com.github.lucasgois.tcc.sqlite.ambiente.Ambiente;
import com.github.lucasgois.tcc.sqlite.modulo.Modulo;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Versao {

    private String nome;
    private Ambiente ambiente;
    private Modulo modulo;
    private List<Arquivo> arquivos = new ArrayList<>();
}