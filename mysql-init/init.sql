-- ====================================
-- Tabela principal: T_CLIENTS
-- ====================================
CREATE TABLE T_CLIENTS (
                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           recipient VARCHAR(150) NOT NULL
)
    ENGINE = InnoDB;

-- ====================================
-- Tabela principal: T_SENDERS
-- ====================================
CREATE TABLE T_SENDERS (
                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           sender VARCHAR(150) NOT NULL
)
    ENGINE = InnoDB;

-- ====================================
-- Tabela principal: T_PACKAGES
-- ====================================
CREATE TABLE T_PACKAGES (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,

                            description VARCHAR(255) NOT NULL,

                            sender_id BIGINT NOT NULL,

                            client_id BIGINT NOT NULL,

                            status ENUM('CREATED','IN_TRANSIT','DELIVERED','CANCELLED') NOT NULL,

                            is_holiday TINYINT(1) DEFAULT 0 NOT NULL,   -- Se é feriado (0=false/1=true)
                            fun_fact VARCHAR(500),                      -- Armazena texto da Dog API (opcional)

                            estimated_delivery_date DATE NOT NULL,

    -- Campos de criação e atualização (auditoria)
                            created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    -- Constraints de FK
                            CONSTRAINT fk_sender
                                FOREIGN KEY (sender_id)
                                    REFERENCES T_SENDERS(id)
                                    ON DELETE RESTRICT
                                    ON UPDATE CASCADE,

                            CONSTRAINT fk_client
                                FOREIGN KEY (client_id)
                                    REFERENCES T_CLIENTS(id)
                                    ON DELETE RESTRICT
                                    ON UPDATE CASCADE
)
    ENGINE = InnoDB;

-- ====================================
-- Tabela secundária: T_TRACKING_EVENTS
-- ====================================
CREATE TABLE T_TRACKING_EVENTS (
                                   id BIGINT AUTO_INCREMENT PRIMARY KEY,

                                   package_id BIGINT NOT NULL,

                                   event_location VARCHAR(300) NOT NULL,
                                   event_description VARCHAR(500) NOT NULL,
                                   event_timestamp DATETIME NOT NULL,

    -- Campos de auditoria
                                   created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                   updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    -- Constraint de Foreign Key para ligar ao Package
                                   CONSTRAINT fk_package
                                       FOREIGN KEY (package_id)
                                           REFERENCES T_PACKAGES(id)
                                           ON DELETE CASCADE  -- exclui eventos se o package for excluído
                                           ON UPDATE CASCADE
)
    ENGINE = InnoDB;

-- ====================================
-- Índices para aumentar a perfomance de busca de pacotes de um usuário específico (A query será composta por um join)
-- Tabelas utilizadas: T_CLIENTS; T_PACKAGES
-- ====================================
CREATE INDEX idx_clients_recipient ON T_CLIENTS(recipient);
CREATE INDEX idx_packages_client_id ON T_PACKAGES(client_id);

-- ====================================
-- Índexa a data de criação para facilitar buscas por pacotes com intervalo de datas
-- Tabelas utilizadas: T_CLIENTS; T_PACKAGES
-- ====================================
CREATE INDEX idx_packages_created_at ON T_PACKAGES(created_at);

-- ====================================
-- Índice composto que localiza linhas que atendem aos dois critérios: status/ updated_at
-- Tabelas utilizadas: T_PACKAGES
-- ====================================
CREATE INDEX idx_packages_status_updated_at ON T_PACKAGES(status, updated_at);

-- ====================================
-- Executa consultas que buscam todos os eventos de rastreamentos relacionados a um pacote
-- Tabela utilizada: T_TRACKING_EVENTS
-- ====================================
CREATE INDEX idx_events_package_id ON T_TRACKING_EVENTS(package_id);

-- ====================================
-- Indexa consultas por client e sender
-- ====================================
CREATE INDEX idx_packages_recipient ON T_PACKAGES(client_id);
CREATE INDEX idx_packages_sender ON T_PACKAGES(sender_id);