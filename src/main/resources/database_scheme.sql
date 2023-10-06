CREATE TABLE IF NOT EXISTS modules
(
    module_id         TEXT PRIMARY KEY,
    module_name       TEXT NOT NULL,
    module_created_at TEXT,
    module_updated_at TEXT
);
CREATE INDEX IF NOT EXISTS idx_module_id ON modules (module_id);

CREATE TABLE IF NOT EXISTS environments
(
    environment_id         TEXT PRIMARY KEY,
    environment_name       TEXT NOT NULL,
    environment_created_at TEXT,
    environment_updated_at TEXT
);
CREATE INDEX IF NOT EXISTS idx_environment_id ON environments (environment_id);

CREATE TABLE IF NOT EXISTS file_registries
(
    file_registry_hash       TEXT PRIMARY KEY,
    file_registry_bytea      TEXT NOT NULL,
    file_registry_created_at TEXT,
    file_registry_updated_at TEXT
);
CREATE INDEX IF NOT EXISTS idx_file_registry_hash ON file_registries (file_registry_hash);

CREATE TABLE IF NOT EXISTS file_locations_map
(
    file_location_id         TEXT PRIMARY KEY,
    file_location_hash_ref   TEXT NOT NULL,
    file_location_path       TEXT NOT NULL,
    file_location_created_at TEXT,
    file_location_updated_at TEXT,
    FOREIGN KEY (file_location_hash_ref) REFERENCES file_registries (file_registry_hash)
);
CREATE INDEX IF NOT EXISTS idx_file_location_id ON file_locations_map (file_location_id);

CREATE TABLE IF NOT EXISTS versions
(
    version_id              TEXT PRIMARY KEY,
    version_name            TEXT NOT NULL,
    version_environment_ref TEXT NOT NULL,
    version_module_ref      TEXT NOT NULL,
    version_created_at      TEXT,
    version_updated_at      TEXT,
    FOREIGN KEY (version_environment_ref) REFERENCES environments (environment_id),
    FOREIGN KEY (version_module_ref) REFERENCES modules (module_id)
);
CREATE INDEX IF NOT EXISTS idx_version_id ON versions (version_id);

CREATE TABLE IF NOT EXISTS files_version_map
(
    file_version_map_id           TEXT PRIMARY KEY,
    file_version_map_version_ref  TEXT NOT NULL,
    file_version_map_location_ref TEXT NOT NULL,
    file_version_map_created_at   TEXT,
    file_version_map_updated_at   TEXT,
    FOREIGN KEY (file_version_map_version_ref) REFERENCES versions (version_id),
    FOREIGN KEY (file_version_map_location_ref) REFERENCES file_locations_map (file_location_id)
);
CREATE INDEX IF NOT EXISTS idx_file_version_map_id ON files_version_map (file_version_map_id);