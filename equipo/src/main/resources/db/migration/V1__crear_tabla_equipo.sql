CREATE TABLE equipo(
     id BIGINT PRIMARY KEY AUTO_INCREMENT,
     idCliente  BIGINT NOT NULL,
     tipoEquipo VARCHAR(100) NOT NULL,
     marca VARCHAR(50) NOT NULL,
      modelo VARCHAR(50) NOT NULL,
     numeroSerie VARCHAR(100) NOT NULL,
     estadoIngreso VARCHAR(50) NOT NULL
)