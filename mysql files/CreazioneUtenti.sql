
-- DROP DATABASE IF EXISTS playsphere;
-- CREATE DATABASE playsphere;

-- USE playsphere;

-- CREATE TABLE utenti(
--     email_utente VARCHAR(100) PRIMARY KEY NOT NULL,
--     nome VARCHAR(50) NOT NULL,
--     cognome VARCHAR(50) NOT NULL,
--     password VARCHAR(300) NOT NULL,
--     data_creazione DATETIME NOT NULL
-- );

-- CREATE TABLE temp_utenti(

--     email_utente VARCHAR(100) NOT NULL,
--     nome VARCHAR(50) NOT NULL,
--     cognome VARCHAR(50) NOT NULL,
--     password VARCHAR(300) NOT NULL,
--     otp VARCHAR(300) NOT NULL,
--     expire_at DATETIME NOT NULL,
--     PRIMARY KEY(email_utente,otp)

-- );

-- CREATE TABLE account(
--     email_utente VARCHAR(100) PRIMARY KEY NOT NULL,
--     password VARCHAR(300) NOT NULL,
--     data_creazione DATETIME NOT NULL,

--     FOREIGN KEY(email_utente) REFERENCES utenti(email_utente)
--         ON DELETE CASCADE
-- );



DROP DATABASE IF EXISTS playsphere;
CREATE DATABASE playsphere;

USE playsphere;

CREATE TABLE utenti(
    email_utente VARCHAR(100) PRIMARY KEY NOT NULL,
    nome VARCHAR(50) NOT NULL,
    cognome VARCHAR(50) NOT NULL,
    password VARCHAR(300) NOT NULL,
    data_nascita DATETIME NOT NULL,
    sesso CHAR(1) NOT NULL,
    data_creazione DATETIME NOT NULL,
    privilegi_flg CHAR(1) NOT NULL
);

CREATE TABLE temp_utenti(
    email_utente VARCHAR(100) NOT NULL,
    nome VARCHAR(50) NOT NULL,
    cognome VARCHAR(50) NOT NULL,
    password VARCHAR(300) NOT NULL,
    data_nascita DATETIME NOT NULL,
    sesso char(1) NOT NULL,
    otp VARCHAR(300) NOT NULL,
    privilegi_flg CHAR(1) NOT NULL,
    expire_at DATETIME NOT NULL,
    PRIMARY KEY(email_utente,otp)
);

CREATE TABLE sports(
    nome VARCHAR(30) PRIMARY KEY NOT NULL
);

INSERT INTO sports VALUES
    ("Calcio"),
    ("Basket"),
    ("Pallavolo"),
    ("Golf"),
    ("Tennis"),
    ("Corsa"),
    ("Fresbee"),
    ("Hockey"),
    ("Scherma"),
    ("Rugby"),
    ("Padel"),
    ("Baseball"),
    ("Dodgeball");

CREATE TABLE tornei(
    nome_torneo VARCHAR(100) NOT NULL,
    data_torneo DATETIME NOT NULL,
    descrizione TEXT,
    eta_minima SMALLINT DEFAULT 5,
    min_partecipanti SMALLINT DEFAULT 0,
    max_partecipanti SMALLINT,
    is_interno BOOLEAN NOT NULL,
    email_organizzatore VARCHAR(100),
    sport VARCHAR(30),
    PRIMARY KEY(nome_torneo,data_torneo),
    FOREIGN KEY (email_organizzatore) REFERENCES utenti(email_utente) ON DELETE SET NULL,
    FOREIGN KEY (sport) REFERENCES sports(nome) ON UPDATE CASCADE ON DELETE SET NULL
);

CREATE TABLE partecipazioni (
    email_partecipante VARCHAR(100) NOT NULL,
    nome_torneo VARCHAR(100) NOT NULL,
    data_torneo DATETIME NOT NULL,
    PRIMARY KEY(email_partecipante, nome_torneo, data_torneo),
    FOREIGN KEY (email_partecipante) REFERENCES utenti(email_utente) ON DELETE CASCADE,
    FOREIGN KEY (nome_torneo, data_torneo) REFERENCES tornei(nome_torneo, data_torneo) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE temp_pass_recover(
    email_utente VARCHAR(100) DEFAULT "nd",
    passkey VARCHAR(300) not NULL,
    expire_at DATETIME NOT NULL,
    PRIMARY KEY (email_utente, passkey),
    FOREIGN KEY (email_utente) REFERENCES utenti(email_utente) on DELETE set DEFAULT

);

CREATE TABLE privilegi(
    id_privilegio TINYINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    utente CHAR(1) NOT NULL,
    evento VARCHAR(20) NOT NULL
);

INSERT INTO privilegi VALUES
    ("e", "Crea esterno"),
    ("p", "Crea esterno"),
    ("p", "Crea interno"),
    ("s", "Crea esterno");
