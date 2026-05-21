CREATE TABLE reparacion (
  reparacion_id BIGINT PRIMARY KEY AUTO_INCREMENT,
  ot_id BIGINT UNIQUE NOT NULL,
  fecha_inicio DATE,
  fecha_termino DATE,
  detalle_trabajo VARCHAR(255),
  estado VARCHAR(30)
);