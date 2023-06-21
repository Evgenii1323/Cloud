CREATE TABLE IF NOT EXISTS cloud.users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    login VARCHAR(255),
    password VARCHAR(255)
    );

INSERT INTO cloud.users (login, password)
VALUES ('USER@USER.COM', '$2a$12$u29pZONR5eOEQI.6gxUvw.P0G5r6LJic4P9TwpV2p9YvX8.34PnEK');

CREATE TABLE IF NOT EXISTS cloud.files (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    filename VARCHAR(255),
    size BIGINT,
    user VARCHAR(255)
    );