CREATE TABLE cliente(
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    rut VARCHAR(12) NOT NULL UNIQUE,
    telefono VARCHAR(15),
    direccion VARCHAR(100),
    tipo_Cliente VARCHAR(30)
)