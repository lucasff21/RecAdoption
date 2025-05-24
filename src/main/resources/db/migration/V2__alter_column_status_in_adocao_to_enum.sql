DO $$
    BEGIN
        IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'adocao_status') THEN
            CREATE TYPE adocao_status AS ENUM (
                'PENDENTE',
                'EM_ANALISE',
                'APROVADO',
                'RECUSADO',
                'FINALIZADO'
                );
        END IF;
    END$$;

ALTER TABLE adocao
ALTER COLUMN status TYPE adocao_status
USING status::text::adocao_status;

ALTER TABLE adocao ALTER COLUMN status SET NOT NULL;