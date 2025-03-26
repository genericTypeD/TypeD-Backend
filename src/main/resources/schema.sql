CREATE TABLE IF NOT EXISTS refresh_token (
                                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                             member_id BIGINT NOT NULL,
                                             token_value VARCHAR(500) NOT NULL
    );

CREATE TABLE IF NOT EXISTS role (
                                    role_id BIGINT PRIMARY KEY,
                                    name VARCHAR(20) NOT NULL
    );

INSERT INTO role (role_id, name) VALUES (1, 'ROLE_USER');
INSERT INTO role (role_id, name) VALUES (2, 'ROLE_ADMIN');