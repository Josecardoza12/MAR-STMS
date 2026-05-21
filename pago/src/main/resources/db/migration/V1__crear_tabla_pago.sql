CREATE TABLE pago (
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    ot_id       BIGINT NOT NULL,
    fecha       DATE,
    monto       DOUBLE NOT NULL,
    forma_pago  VARCHAR(50),
    estado      VARCHAR(20) DEFAULT 'pendiente'
);