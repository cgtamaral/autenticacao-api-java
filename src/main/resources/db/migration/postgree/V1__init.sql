CREATE TABLE usuario(
 id serial PRIMARY KEY,
 name VARCHAR (255) NOT NULL,
 email VARCHAR (50) UNIQUE NOT NULL,
 password VARCHAR (12)  NOT NULL,
 profile VARCHAR (12)  NOT NULL,
 active BOOLEAN NOT NULL,
 creationdate DATE NOT NULL
);