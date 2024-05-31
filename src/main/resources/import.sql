INSERT INTO roles (name) VALUES ('ADMIN'),('USUARIO'),('COBRADOR'),('GENERICO');
-- permisos  cliente
INSERT INTO permisos (descripcion) VALUES ('registrar-cliente'),('consultar-clientes'),('consultar-cliente-por-cedula'),('consultar-cuotas-por-fecha'),('actualizar-cliente');
-- permisos  credito
INSERT INTO permisos (descripcion) VALUES ('registrar-credito'),('consultar-credito'),('consultar-creditos-activos'),('modificar-estado-credito');
-- permisos cuota
INSERT INTO permisos (descripcion) VALUES ('pagar-cuota'),('consultar-cuota-cliente'),('consultar-credito-saldo'),('modificar-fecha-pago'),('generar-reporte-interes-capital'),('consultar-abonos-realizados'),('consultar-ultimos-abonos-realizados'),('consultar-abono-por-id');