package com.github.lucasgois.tcc.sqlite.versao;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class InfoVersao {
    private String uuid;
    private String versao;
    private String modulo;
    private String ambiente;
}
