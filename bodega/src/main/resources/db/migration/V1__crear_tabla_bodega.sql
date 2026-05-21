CREATE TABLE bodega (
    id              BIGINT PRIMARY KEY AUTO_INCREMENT,
    ot_id           BIGINT NOT NULL UNIQUE,
    fecha_listo     DATE,
    dias_en_bodega  INT DEFAULT 0,
    estado_cobro    VARCHAR(20) DEFAULT 'sin_cobro',
    monto_bodegaje  DOUBLE DEFAULT 0.0
);