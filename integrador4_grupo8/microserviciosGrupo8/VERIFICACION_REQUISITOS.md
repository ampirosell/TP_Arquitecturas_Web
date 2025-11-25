# Verificación de Requisitos - TP Arquitecturas Web

Revisión de lo que está implementado y lo que falta.

## 1ra ENTREGA

### 1. Modelamiento en Sub-dominios

**Estado: COMPLETO**

Tenemos 6 microservicios:
- `microservicio-user` - maneja usuarios
- `microservicio-cuenta` - cuentas y créditos
- `microservicio-parada` - paradas
- `microservicio-monopatin` - flota (usa MongoDB)
- `microservicio-viaje` - viajes y pausas
- `microservicio-facturacion` - facturación y tarifas

Cada uno tiene su propia base de datos. El de monopatines usa MongoDB, los demás MySQL.

---

### 2. Entidades y Relaciones (mapeo a BD)

**Estado: COMPLETO**

Todas las entidades están mapeadas a bases de datos:
- 5 microservicios con MySQL (user, cuenta, parada, viaje, facturacion)
- 1 microservicio con MongoDB (monopatin)

---

### 3. Backend Básico con ABM

**Estado: COMPLETO**

Todas las entidades tienen ABM completo (GET, POST, PUT/PATCH, DELETE):

- User ✅
- Cuenta ✅
- Parada ✅
- Monopatin ✅
- Viaje ✅
- Pausa ✅
- Tarifa ✅
- Factura (solo GET y POST, no tiene DELETE porque no tiene sentido)

Funcionalidades principales:
- Iniciar viaje
- Finalizar viaje
- Registrar/finalizar pausa
- Registrar/finalizar mantenimiento
- Ubicar monopatín en parada

---

### 4. Servicios/Reportes

**Estado: PARCIAL (7 de 8 completos)**

- **a)** Reporte de km (con/sin pausas) - ✅ `GET /viajes/reportes/kilometros`
- **b)** Anular cuenta - ✅ `PATCH /cuenta/{id}/estado`
- **c)** Monopatines con más de X viajes - ✅ `GET /viajes/reportes/monopatines-frecuentes`
- **d)** Total facturado en rango de meses - ✅ `GET /facturacion/total-mensual`
- **e)** Usuarios que más utilizan (filtrado por período y tipo) - ⚠️ `GET /viajes/usuarios-mas-viajes` - **FALTA FILTRAR POR TIPO DE USUARIO** (solo filtra por período)
- **f)** Ajuste de precios con vigencia - ✅ `POST /tarifas`
- **g)** Monopatines cercanos - ✅ `GET /monopatines/cercanos`
- **h)** Reporte de uso por período (usuario y opcionalmente otros usuarios relacionados) - ✅ `GET /viajes/usado-en-periodo`

**Nota sobre e):** El endpoint existe y filtra por período, pero el parámetro `tipoUsuario` que se recibe en el controlador no se usa. Hay que agregar el filtro por tipo de usuario en la query del repository.

---

## 2da ENTREGA

### 5. Ajustar Correcciones de 1ra Entrega

**Estado: DESCONOCIDO**

No tengo información sobre qué correcciones se pidieron en la 1ra entrega, así que no puedo verificar si se aplicaron.

---

### 6. Mock de Servicios Externos

**Estado: NO IMPLEMENTADO ❌**

No encontré ningún mock de servicios externos como:
- MercadoPago (para pagos)
- Servicios de mapas (para búsquedas geográficas)

**Falta implementar:** Mocks o servicios simulados para estos servicios externos.

---

### 7. Segurización con JWT

**Estado: COMPLETO ✅**

JWT está implementado en **todos los microservicios**:
- `microservicio-user` ✅
- `microservicio-viaje` ✅
- `microservicio-cuenta` ✅
- `microservicio-parada` ✅
- `microservicio-monopatin` ✅
- `microservicio-facturacion` ✅

Cada microservicio tiene:
- `JwtUtil` - utilidad para generar, validar y extraer información de tokens
- `JwtAuthenticationFilter` - filtro que intercepta requests y valida tokens JWT
- `SecurityConfig` - configuración de Spring Security con JWT
- `RoleValidator` - actualizado para usar JWT del contexto de seguridad
- `application.properties` - configurado con `jwt.secret` y `jwt.expiration`

Todos los controladores ahora usan JWT automáticamente. Los tokens se obtienen del endpoint `/users/login` o `/users/register` y se envían en el header `Authorization: Bearer <token>`.

---

### 8. Swagger/OpenAPI

**Estado: COMPLETO**

Todos los microservicios tienen Swagger configurado. Se accede en `/swagger-ui/index.html` de cada servicio.

---

### 9. Base NoSQL (MongoDB)

**Estado: COMPLETO**

El `microservicio-monopatin` usa MongoDB. Como el requisito dice "MongoDB o gRPC", con tener MongoDB ya está cumplido.

---

### 10. Servicio de Chat con LLM

**Estado: NO IMPLEMENTADO ❌**

No encontré ningún servicio de chat con LLM para usuarios premium. El requisito pide:
- Chat que permita consultar en lenguaje natural sobre servicios predefinidos
- Integración con un servicio de LLMs (sugerencia: Groq)
- Exponer servicios predefinidos como tools
- Funcionalidad para usuarios premium

**Falta implementar:** Todo el servicio de chat con LLM.

---

### 11. Docker (Opcional)

**Estado: COMPLETO**

Tenemos `docker-compose.yml` con todos los servicios, bases de datos, y configuración de red. También hay Dockerfiles para cada microservicio.

---

## Resumen

### 1ra Entrega: ~94% ⚠️

- Puntos 1, 2, 3: ✅ Completos
- Punto 4: ⚠️ 7 de 8 reportes completos (falta filtrar por tipo de usuario en el reporte e)

### 2da Entrega: ~60% ⚠️

- Punto 5: ❓ Desconocido (no sé qué correcciones se pidieron)
- Punto 6: ❌ No implementado (mocks de servicios externos)
- Punto 7: ✅ Completo (JWT en todos los microservicios)
- Punto 8: ✅ Completo (Swagger)
- Punto 9: ✅ Completo (MongoDB)
- Punto 10: ❌ No implementado (chat con LLM)
- Punto 11: ✅ Completo (Docker)

**Estado general: ~75% completado**

---

## Lo que falta hacer

### Prioridad Alta (1ra Entrega)

1. **Completar reporte e) - Filtrar por tipo de usuario**
   - Modificar `ViajeRepository.findUsuariosConMasViajesPorPeriodo` para aceptar tipo de usuario
   - Actualizar la query para filtrar por tipo
   - Actualizar `ViajeService` y `UserService` para pasar el parámetro

### Prioridad Alta (2da Entrega)

2. **Implementar mocks de servicios externos**
   - Crear servicio mock para MercadoPago
   - Crear servicio mock para búsquedas en mapas
   - Integrar estos mocks donde se necesiten

3. **Implementar servicio de chat con LLM**
   - Crear microservicio o endpoint para chat
   - Integrar con Groq u otro servicio de LLM
   - Exponer servicios predefinidos como tools
   - Implementar lógica de usuarios premium
   - Re-diseñar microservicios si es necesario

### Prioridad Media

5. **Verificar correcciones de 1ra entrega**
   - Revisar feedback de la 1ra entrega
   - Aplicar las correcciones solicitadas

Tiempo estimado: ~30-40 horas más de trabajo.

---

## Conclusión

**Estado general: ~75% completado**

- 1ra entrega: ~94% (falta completar filtro por tipo de usuario en reporte e)
- 2da entrega: ~60% (faltan mocks de servicios externos y chat con LLM)

El proyecto tiene una buena base. JWT está completamente implementado en todos los microservicios. Faltan principalmente los mocks de servicios externos y el servicio de chat con LLM, que requiere un re-diseño de los microservicios.
