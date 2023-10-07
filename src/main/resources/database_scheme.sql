CREATE TABLE IF NOT EXISTS modulos
(
    uuid_modulo   TEXT NOT NULL,
    nome          TEXT NOT NULL,
    criado_em     TEXT,
    atualizado_em TEXT,
    PRIMARY KEY (uuid_modulo),
    UNIQUE (uuid_modulo),
    UNIQUE (nome)
);

CREATE TABLE IF NOT EXISTS ambientes
(
    uuid_ambiente TEXT NOT NULL,
    nome          TEXT NOT NULL,
    criado_em     TEXT,
    atualizado_em TEXT,
    PRIMARY KEY (uuid_ambiente),
    UNIQUE (uuid_ambiente),
    UNIQUE (nome)
);

CREATE TABLE IF NOT EXISTS arquivos
(
    hash          TEXT NOT NULL,
    bytea         TEXT NOT NULL,
    criado_em     TEXT,
    atualizado_em TEXT,
    PRIMARY KEY (hash),
    UNIQUE (hash)
);

CREATE TABLE IF NOT EXISTS localizacoes
(
    uuid_localizacao TEXT NOT NULL,
    hash_arquivo     TEXT NOT NULL,
    caminho          TEXT NOT NULL,
    criado_em        TEXT,
    atualizado_em    TEXT,
    PRIMARY KEY (uuid_localizacao),
    FOREIGN KEY (hash_arquivo) REFERENCES arquivos (hash),
    UNIQUE (uuid_localizacao),
    UNIQUE (hash_arquivo, caminho)
);

CREATE TABLE IF NOT EXISTS mapeamento
(
    uuid_mapeamento  TEXT NOT NULL,
    uuid_versao      TEXT NOT NULL,
    uuid_localizacao TEXT NOT NULL,
    criado_em        TEXT,
    atualizado_em    TEXT,
    PRIMARY KEY (uuid_mapeamento),
    FOREIGN KEY (uuid_versao) REFERENCES versoes (uuid_versao),
    FOREIGN KEY (uuid_localizacao) REFERENCES localizacoes (uuid_localizacao),
    UNIQUE (uuid_mapeamento),
    UNIQUE (uuid_versao, uuid_localizacao)
);

CREATE TABLE IF NOT EXISTS versoes
(
    uuid_versao   TEXT NOT NULL,
    nome          TEXT NOT NULL,
    uuid_ambiente TEXT NOT NULL,
    uuid_modulo   TEXT NOT NULL,
    criado_em     TEXT,
    atualizado_em TEXT,
    PRIMARY KEY (uuid_versao),
    FOREIGN KEY (uuid_ambiente) REFERENCES ambientes (uuid_ambiente),
    FOREIGN KEY (uuid_modulo) REFERENCES modulos (uuid_modulo),
    UNIQUE (uuid_versao),
    UNIQUE (nome)
);