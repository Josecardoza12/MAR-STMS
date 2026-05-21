# MAR-STMS
Proyecto MAR-STMS
## Descripción del proyecto
## Descripción del proyecto

MAR-STMS (MAR Smart Technical Management System) es un sistema desarrollado bajo arquitectura de microservicios orientado a la gestión integral de procesos operacionales y administrativos de un servicio técnico. Su objetivo principal es reemplazar procesos informales basados en planillas, registros manuales y control humano mediante una plataforma estructurada, trazable y automatizada.

La solución modela el funcionamiento real del negocio mediante dominios independientes y especializados, donde cada microservicio cumple una responsabilidad específica dentro del sistema, incluyendo autenticación, clientes, equipos, órdenes de trabajo, diagnósticos, reparaciones, inventario, bodega, pagos y finanzas.

El núcleo de la plataforma está centrado en la Orden de Trabajo (OT), la cual actúa como eje principal de coordinación, trazabilidad y comunicación entre servicios. A partir de ella se gestiona el flujo operativo completo del sistema:

Cliente → Equipo → Orden de Trabajo → Diagnóstico → Reparación → Bodega → Pago

Adicionalmente, el microservicio de Finanzas opera de forma transversal, integrando información proveniente de pagos, inventario y operaciones para mantener control de ingresos, egresos, movimientos financieros y flujo de caja.

Cada etapa incorpora estados definidos, validaciones automáticas y reglas de negocio obligatorias que garantizan la consistencia del sistema, evitando errores como reparaciones no autorizadas, entregas sin pago, uso de repuestos sin stock o inconsistencias entre procesos.

La comunicación entre microservicios se realiza mediante APIs REST, permitiendo una arquitectura desacoplada, escalable y fácil de mantener. De esta forma, MAR-STMS no solo automatiza procesos técnicos, sino que entrega trazabilidad completa y una gestión operativa y financiera integrada.
 Integrantes:

- JOSE CARDOZA.
- RIMSKY FARIAS
- CHRISTIAN QUIROZ 
