CREATE DATABASE IF NOT EXISTS loja_solidaria;

USE loja_solidaria;

CREATE TABLE beneficiarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    cpf VARCHAR(11) NOT NULL UNIQUE,
    telefone VARCHAR(20)
);

CREATE TABLE categorias (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(50) NOT NULL UNIQUE
);

INSERT INTO categorias (nome)
VALUES ('Masculinos'),
    ('Femininos'),
    ('Infantil'),
    ('Calçados'),
    ('Diversos'),
    ('Não especificado');

CREATE TABLE doacoes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    tipo ENUM('ENTRADA', 'SAIDA') NOT NULL,
    categoria VARCHAR(50),
    quantidade INT NOT NULL,
    data_evento DATE NOT NULL
);

CREATE TABLE estoque_emprestimos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    categoria VARCHAR(50) NOT NULL UNIQUE,
    quantidade INT NOT NULL DEFAULT 0
);

INSERT INTO
    estoque_emprestimos (categoria, quantidade)
VALUES ('Masculino', 0),
    ('Feminino', 0),
    ('Não especificado', 0);

CREATE TABLE emprestimos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    beneficiario_id INT NOT NULL,
    categoria VARCHAR(50) NOT NULL,
    quantidade INT NOT NULL,
    emprestado BOOLEAN NOT NULL DEFAULT TRUE,
    data_evento DATE NOT NULL,
    FOREIGN KEY (beneficiario_id) REFERENCES beneficiarios (id) ON DELETE RESTRICT ON UPDATE CASCADE
);

CREATE TABLE usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuario VARCHAR(50) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL
);



INSERT INTO usuarios (usuario, senha) VALUES ('admin', '1234');


/*
    
    

*/