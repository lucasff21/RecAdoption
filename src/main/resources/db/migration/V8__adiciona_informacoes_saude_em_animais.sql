ALTER TABLE animais
    ADD COLUMN castrado BOOLEAN NOT NULL DEFAULT false,
    ADD COLUMN data_ultima_vermifugacao DATE NULL,
    ADD COLUMN data_ultima_vacina_antirrabica DATE NULL,
    ADD COLUMN data_ultima_vacina_multipla DATE NULL,
    ADD COLUMN tipo_vacina_multipla VARCHAR(20) NULL, -- 'V10', 'V5'.
    ADD COLUMN observacoes_medicas TEXT NULL;