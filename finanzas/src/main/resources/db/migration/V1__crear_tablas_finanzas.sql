

CREATE TABLE finanzas (
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    pago_Id BIGINT,
    fecha       DATE,
    categoria   VARCHAR(100),
    detalle     VARCHAR(255),
    proveedor   VARCHAR(100),
    medio_pago  VARCHAR(50),
    total       DOUBLE NOT NULL
);