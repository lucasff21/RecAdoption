ALTER TABLE animais_caracteristicas
    DROP CONSTRAINT IF EXISTS fk_animais_caracteristicas_on_caracteristica;

ALTER TABLE caracteristicas
    ALTER COLUMN id TYPE BIGINT;

ALTER TABLE animais_caracteristicas
    ALTER COLUMN caracteristica_id TYPE BIGINT;

ALTER TABLE animais_caracteristicas
    ADD CONSTRAINT fk_animais_caracteristicas_on_caracteristica FOREIGN KEY (caracteristica_id) REFERENCES caracteristicas (id);
