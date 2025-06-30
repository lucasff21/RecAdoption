ALTER TABLE animais ADD COLUMN data_nascimento_aproximada DATE;

UPDATE animais
SET data_nascimento_aproximada = (
        CURRENT_DATE - (
            SPLIT_PART(LOWER(idade), ' ', 1)::INT *
            CASE
                WHEN idade ILIKE '%ano%' THEN INTERVAL '1 year'
                WHEN idade ILIKE '%mes%' THEN INTERVAL '1 month'
            END
        )
    )::DATE
WHERE idade IS NOT NULL;

ALTER TABLE animais DROP COLUMN idade;