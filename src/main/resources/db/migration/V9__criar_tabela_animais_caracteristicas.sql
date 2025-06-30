CREATE TABLE animais_caracteristicas (
    id SERIAL,
    animal_id BIGINT NOT NULL,
    caracteristica_id INT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (animal_id, caracteristica_id),
    CONSTRAINT fk_ac_animal FOREIGN KEY (animal_id) REFERENCES animais(id) ON DELETE CASCADE,
    CONSTRAINT fk_ac_caracteristica FOREIGN KEY (caracteristica_id) REFERENCES caracteristicas(id) ON DELETE CASCADE
);


CREATE INDEX idx_animais_caracteristicas_animal_id ON animais_caracteristicas (animal_id);
CREATE INDEX idx_animais_caracteristicas_caracteristica_id ON animais_caracteristicas (caracteristica_id);


INSERT INTO animais_caracteristicas (animal_id, caracteristica_id, created_at)
SELECT
    a.id,
    (SELECT id FROM caracteristicas WHERE nome = 'Brincalhão'),
    NOW()
FROM
    animais a
        JOIN
    cachorro c ON a.id = c.id
WHERE
    c.brincalhao IS TRUE;

INSERT INTO animais_caracteristicas (animal_id, caracteristica_id, created_at)
SELECT
    a.id,
    (SELECT id FROM caracteristicas WHERE nome = 'Gosta de crianças'),
    NOW()
FROM
    animais a
        JOIN
    cachorro c ON a.id = c.id
WHERE
    c.gosta_crianca IS TRUE;


INSERT INTO animais_caracteristicas (animal_id, caracteristica_id, created_at)
SELECT
    a.id,
    (SELECT id FROM caracteristicas WHERE nome = 'Muita Energia'),
    NOW()
FROM
    animais a
        JOIN
    cachorro c ON a.id = c.id
WHERE
    c.necessidade_correr IS TRUE;

INSERT INTO animais_caracteristicas (animal_id, caracteristica_id, created_at)
SELECT
    a.id,
    (SELECT id FROM caracteristicas WHERE nome = 'Late Bastante'),
    NOW()
FROM
    animais a
        JOIN
    cachorro c ON a.id = c.id
WHERE
    c.tende_latir IS TRUE;


INSERT INTO animais_caracteristicas (animal_id, caracteristica_id, created_at)
SELECT
    a.id,
    (SELECT id FROM caracteristicas WHERE nome = 'Protetor'),
    NOW()
FROM
    animais a
        JOIN
    cachorro c ON a.id = c.id
WHERE
    c.cao_guarda IS TRUE;


INSERT INTO animais_caracteristicas (animal_id, caracteristica_id, created_at)
SELECT
    a.id,
    (SELECT id FROM caracteristicas WHERE nome = 'Quieto'),
    NOW()
FROM
    animais a
        JOIN
    cachorro c ON a.id = c.id
WHERE
    c.ideal_casa IS TRUE;


INSERT INTO animais_caracteristicas (animal_id, caracteristica_id, created_at)
SELECT
    a.id,
    (SELECT id FROM caracteristicas WHERE nome = 'Hipoalergênico'),
    NOW()
FROM
    animais a
        JOIN
    cachorro c ON a.id = c.id
WHERE
    c.queda_pelo IS FALSE;