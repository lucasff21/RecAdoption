CREATE TABLE caracteristicas (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) UNIQUE NOT NULL,
    descricao TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_caracteristicas_nome ON caracteristicas (nome);

INSERT INTO caracteristicas (nome, descricao) VALUES
  ('Brincalhão', 'Com alto nível de energia, gosta de interagir em brincadeiras e atividades lúdicas.'),
  ('Calmo', 'Temperamento tranquilo, que prefere ambientes serenos e não se agita facilmente.'),
  ('Tímido', 'Pode demorar a se adaptar a novas pessoas ou ambientes, necessitando de paciência e espaço.'),
  ('Sociável', 'Possui facilidade para interagir com diferentes pessoas e outros animais, buscando convivência.'),
  ('Independente', 'Sente-se confortável em passar tempo sozinho e não necessita de atenção constante.'),
  ('Gosta de crianças', 'Demonstra boa interação e paciência com crianças, sendo adequado para famílias com pequenos.'),
  ('Bom com Outros Animais', 'Convive harmoniosamente com outras espécies ou outros pets na casa.'),
  ('Muita Energia', 'Alta demanda por atividades físicas diárias, como corridas e longas caminhadas.'),
  ('Late Bastante', 'Vocaliza com frequência, seja por aviso, brincadeira ou ansiedade.'),
  ('Carinhoso', 'Busca e aprecia contato físico, demonstrando afeto através de carinhos.'),
  ('Protetor', 'Instinto de guarda, que pode ser territorial ou demonstrar proteção à família.'),
  ('Hipoalergênico', 'Produz menos alérgenos, sendo mais adequado para pessoas com alergias.'),
  ('Treinável', 'Aprende comandos e truques com facilidade, respondendo bem ao treinamento.'),
  ('Quieto', 'Não costuma vocalizar muito, preferindo ambientes mais silenciosos.'),
  ('Arisco', 'Reage com medo ou cautela a interações, podendo se esquivar ou mostrar sinais de desconforto.'),
  ('Agressivo', 'Pode reagir de forma hostil ou agressiva a certos estímulos, necessitando de manejo especializado e cautela.');
