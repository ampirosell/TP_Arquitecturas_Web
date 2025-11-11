# Microservicios Grupo 8

Este módulo contiene los cinco microservicios Spring Boot del trabajo integrador junto con la infraestructura para ejecutarlos en contenedores Docker.

- `microservicio-user`
- `microservicio-parada`
- `microservicio-monopatin`
- `microservicio-viaje`
- `microservicio-cuenta`

Cada microservicio incluye `Dockerfile`, configuración local (`application.properties`) y configuración para Docker (`application-docker.properties`).

---

## Requisitos

- Docker Desktop / Docker Engine 24+
- Docker Compose V2 (incluido en Docker Desktop)
- JDK 17 y Maven 3.9+ (solo si querés ejecutar local sin contenedores)

---

## Puertos y Bases de Datos

| Servicio                | Puerto | Base                   | Motor   |
|-------------------------|--------|------------------------|---------|
| microservicio-user      | 8001   | user                   | MySQL   |
| microservicio-parada    | 8002   | parada                 | MySQL   |
| microservicio-monopatin | 8003   | monopatin              | MongoDB |
| microservicio-viaje     | 8004   | viaje                  | MySQL   |
| microservicio-cuenta    | 8005   | cuenta                 | MySQL   |

Docker Compose levanta automáticamente cuatro instancias de MySQL y una de MongoDB con las credenciales configuradas.

---

## Ejecutar Todo con Docker

```bash
docker compose up -d --build
```

Esto construye las imágenes (multi-stage) y levanta:
- 5 microservicios (`*_service`)
- 4 MySQL (`inte4_*_db`)
- 1 MongoDB (`inte4_monopatin_db`)
- red `inte4_net`

Verificar:
```bash
docker ps
docker logs -f user_service         # ejemplo
```

Detener:
```bash
docker compose down
```

Opcional (limpieza total):
```bash
docker compose down --rmi all --volumes
```

---

## Ejecución Local (sin Docker)

```bash
cd microservicio-user
mvn spring-boot:run
```

Debés tener las bases corriendo localmente con los puertos indicados en `application.properties`.

---

## Postman

En la carpeta raíz:
- `postman_collection.json`
- `postman_environment.json`

Pasos:
1. Importá ambos archivos en Postman.
2. Seleccioná el environment “Microservicios Grupo8 - Local”.
3. Ejecutá los requests carpeta por carpeta. Ajustá payloads/IDs desde el environment si es necesario.

---

## Guía de Testing Rápida

### Verificar contenedores
```bash
docker ps
```
Deben aparecer:
- `inte4_*_db` (MySQL en 3306-3309)
- `inte4_monopatin_db` (Mongo en 27017)
- `user_service`, `parada_service`, `monopatin_service`, `viaje_service`, `cuenta_service`

### Curl de ejemplo

**User**
```bash
curl {{user_base_url}}/users
```

**Parada**
```bash
curl -X POST {{parada_base_url}}/paradas \
  -H "Content-Type: application/json" \
  -d '{"direccion":"Av. Siempreviva 742","nombre":"Parada Centro","latitud":-34.6037,"longitud":-58.3816}'
```

**Monopatín**
```bash
curl -X POST {{monopatin_base_url}}/monopatines \
  -H "Content-Type: application/json" \
  -d '{"estadoMonopatin":"LIBRE","kmRecorridos":0,"latitud":-34.6037,"longitud":-58.3816}'
```

**Viaje**
```bash
curl {{viaje_base_url}}/viajes
```

**Crear viaje**
```bash
curl -X POST {{viaje_base_url}}/viajes \
  -H "Content-Type: application/json" \
  -d '{"fechaInicio":"2025-11-11T08:30:00","fechaFin":"2025-11-11T09:00:00","kmRecorridos":5.5,"pausa":false,"idMonopatin":1,"idUsuario":1}'
```

**Crear pausa**
```bash
curl -X POST {{viaje_base_url}}/pausas \
  -H "Content-Type: application/json" \
  -d '{"fechaInicio":"2025-11-11T09:10:00","fechaFin":"2025-11-11T09:40:00","pausaTotal":30,"viaje":{"idViaje":1}}'
```

**Cuenta**
```bash
curl {{cuenta_base_url}}/cuenta/1
```

---

## Troubleshooting

- **`Failed to configure a DataSource`**  
  Verificá que el contenedor MySQL correspondiente figure como `healthy` y que la URL en `application-docker.properties` apunte al hostname interno (`inte4_*_db`).

- **`Cannot connect to database`**  
  Revisá `docker ps`. MySQL puede tardar unos segundos en subir; los microservicios esperan los healthchecks, pero si fallan reiniciá con `docker compose up -d`.

- **Conflicto de puertos**  
  Si tu host ya usa 8001-8005 o 3306-3309, cambiá los puertos en `docker-compose.yml` (lado host).

- **Cambios en el código**  
  Después de editar Java o configuración, rebuild con:
  ```bash
  docker compose up -d --build
  ```

- **Logs de contenedores**
  ```bash
  docker logs -f user_service
  docker logs -f inte4_user_db
  docker logs -f inte4_monopatin_db
  ```

### Error UNKNOWN al ejecutar

#### Verificar y Configurar el SDK del Proyecto

1. **File** → **Project Structure** (o `Ctrl+Alt+Shift+S`)
2. En la sección **Project**:
    - **SDK**: Selecciona Java 17 (o crea uno si no existe)
    - **Language level**: Selecciona **17 - Sealed classes, always-strict floating-point semantics**
    - **Project compiler output**: Puede quedar vacío o usar `demo/out`
3. Haz clic en **Apply** y luego **OK**
---

Grupo 8 [el más mejor] – Arquitecturas Web – TP Integrador
