CREATE TABLE orden_trabajo(
 id BIGINT PRIMARY KEY AUTO_INCREMENT,

     idCliente BIGINT,

     idEquipo BIGINT,

     numeroOt INT(50) NOT NULL UNIQUE,

     estado VARCHAR(50) NOT NULL,

    fechaIngreso DATE NOT NULL,

    fechaEntrega DATE,

     diagnosticoMonto DOUBLE(10,2) ,

    repuestosMonto DOUBLE (10,2),

    manoObraMonto DOUBLE (10,2),

    totalCobrado DOUBLE (10,2),

    estadoPago VARCHAR(20) NOT NULL

)