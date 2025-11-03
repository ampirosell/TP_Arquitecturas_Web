# Guía de Testing - Microservicios

## 📋 Pasos Previos

### 1. Asegúrate de que Docker esté corriendo

**Microservicio Parada (MySQL):**
```bash
cd microservicio-parada
docker-compose up -d
```

**Microservicio Monopatin (MongoDB):**
```bash
cd microservicio-monopatin
docker-compose up -d
```

### 2. Verifica que los contenedores estén corriendo
```bash
docker ps
```

Deberías ver:
- `inte4_parada` (MySQL en puerto 3307)
- `inte4_monopatin` (MongoDB en puerto 27017)

## 🚀 Ejecutar los Microservicios

### Opción 1: Desde IntelliJ IDEA
1. Ejecuta la clase `MicroservicioParadaApplication` (puerto 8002)
2. Ejecuta la clase `MicroservicioMonopatinApplication` (puerto 8003)

### Opción 2: Desde Maven
```bash
# Terminal 1 - Parada
cd microservicio-parada
mvn spring-boot:run

# Terminal 2 - Monopatin
cd microservicio-monopatin
mvn spring-boot:run
```

## 🧪 Testing de Endpoints

### Microservicio Parada (Puerto 8002)

#### 1. Crear una Parada
```bash
curl -X POST http://localhost:8002/paradas \
  -H "Content-Type: application/json" \
  -d '{
    "direccion": "Av. Corrientes 1234",
    "nombre": "Parada Centro",
    "latitud": -34.6037,
    "longitud": -58.3816
  }'
```

#### 2. Obtener todas las Paradas
```bash
curl http://localhost:8002/paradas
```

#### 3. Obtener Parada por ID
```bash
curl http://localhost:8002/paradas/1
```

#### 4. Buscar Paradas cercanas
```bash
curl "http://localhost:8002/paradas/monopatinesCercanos/-34.6037/-58.3816?distanciaCercana=0.1"
```

---

### Microservicio Monopatin (Puerto 8003)

#### 1. Crear un Monopatin
```bash
curl -X POST http://localhost:8003/monopatines \
  -H "Content-Type: application/json" \
  -d '{
    "estadoMonopatin": "LIBRE",
    "kmRecorridos": 0,
    "latitud": -34.6037,
    "longitud": -58.3816
  }'
```

#### 2. Obtener todos los Monopatines
```bash
curl http://localhost:8003/monopatines
```

#### 3. Obtener Monopatin por ID
```bash
curl http://localhost:8003/monopatines/{id}
```
*Nota: MongoDB genera IDs automáticamente. Usa el ID que recibiste al crear un monopatin.*

#### 4. Buscar Monopatines cercanos
```bash
curl "http://localhost:8003/monopatines/monopatinesCercanos/-34.6037/-58.3816?distanciaCercana=0.1"
```

---

## 📝 Ejemplos de JSON para Testing

### Crear Parada
```json
{
  "direccion": "Av. Santa Fe 1234",
  "nombre": "Parada Palermo",
  "latitud": -34.5889,
  "longitud": -58.3974
}
```

### Crear Monopatin
```json
{
  "estadoMonopatin": "LIBRE",
  "kmRecorridos": 0,
  "latitud": -34.5889,
  "longitud": -58.3974
}
```

**Estados disponibles para Monopatin:**
- `LIBRE`
- `MANTENIMIENTO`
- `EN_USO`

---

## 🔍 Usando Postman

1. Importa la colección de Postman (si existe)
2. O crea requests manualmente:
   - **Method**: GET, POST según corresponda
   - **URL**: `http://localhost:8002/paradas` o `http://localhost:8003/monopatines`
   - **Headers**: `Content-Type: application/json` (para POST)
   - **Body**: JSON raw (para POST)

---

## 🐛 Verificar Logs

### Ver logs de Docker
```bash
# Parada (MySQL)
docker logs inte4_parada

# Monopatin (MongoDB)
docker logs inte4_monopatin
```

### Ver logs de la aplicación
Los logs aparecen en la consola donde ejecutaste `mvn spring-boot:run` o en la consola de IntelliJ.

---

## ✅ Checklist de Testing

- [ ] Docker containers corriendo
- [ ] Microservicio Parada corriendo (puerto 8002)
- [ ] Microservicio Monopatin corriendo (puerto 8003)
- [ ] Crear parada exitosamente
- [ ] Crear monopatin exitosamente
- [ ] Listar todas las paradas
- [ ] Listar todos los monopatines
- [ ] Buscar por ID funciona
- [ ] Búsqueda de cercanos funciona

---

## 🛠️ Troubleshooting

### Error: "Connection refused"
- Verifica que el servicio esté corriendo
- Verifica el puerto correcto (8002 para parada, 8003 para monopatin)

### Error: "Cannot connect to database"
- Verifica que Docker esté corriendo
- Verifica que los contenedores estén activos: `docker ps`
- Verifica los puertos en application.properties

### Error: "Port already in use"
- Detén otros servicios que usen esos puertos
- O cambia el puerto en application.properties


## Error UNKNOWN al ejecutar

### Verificar y Configurar el SDK del Proyecto

1. **File** → **Project Structure** (o `Ctrl+Alt+Shift+S`)
2. En la sección **Project**:
    - **SDK**: Selecciona Java 17 (o crea uno si no existe)
    - **Language level**: Selecciona **17 - Sealed classes, always-strict floating-point semantics**
    - **Project compiler output**: Puede quedar vacío o usar `demo/out`
3. Haz clic en **Apply** y luego **OK**

