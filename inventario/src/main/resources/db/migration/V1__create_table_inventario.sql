
CREATE TABLE repuesto(
    repuesto_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    reparacion_id BIGINT,
    nombre VARCHAR(100),
    proveedor VARCHAR(100),
    costo DOUBLE,
    precio_sugerido DOUBLE,
    stock INT
);
