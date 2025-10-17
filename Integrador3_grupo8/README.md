# Integrador 3 - Sistema de Estudiantes con REST API

## Descripción
Sistema de registro de estudiantes universitarios con **Spring Boot**, **JPA/Hibernate**, MySQL y Docker. Implementa todas las funcionalidades requeridas con servicios REST y clave primaria basada en DNI.

## Tecnologías
- Java 17
- **Spring Boot 3.5.6**
- **JPA/Hibernate**
- MySQL 8.0
- Docker & Docker Compose
- Maven

## Estructura del proyecto
```
src/main/java/org/integrador/
├── entity/ (Estudiante, Carrera, EstudianteDeCarrera, EstudianteCarreraId)
├── repository/ (Repositorios con JPQL)
├── service/ (Lógica de negocio)
├── controller/ (REST Controllers)
├── DTO/ (Data Transfer Objects)
├── helper/ (CSVReader)
└── inte/ (Main Spring Boot Application)
```

## Entidades
- **Estudiante**: dni (PK), nombre, apellido, edad, género, ciudadDeResidencia, numeroLU
- **Carrera**: carreraId (PK), nombre, duracion
- **EstudianteDeCarrera**: clave compuesta (dni + carreraId), fechaInscripcion, graduado, fechaGraduacion
- **EstudianteCarreraId**: clase embeddable para clave compuesta (@EmbeddedId, @Embeddable, @MapsId)

## Requerimientos completados ✅

### 1. Diseño
- ✅ Diagrama de objetos (diagramas.md)
- ✅ Diagrama DER (diagramas.md)
- ✅ Entidades JPA con relaciones correctas
- ✅ Clave compuesta con mejores prácticas JPA
- ✅ DNI como clave primaria

### 2. Funcionalidades implementadas
- ✅ a) Dar de alta un estudiante
- ✅ b) Matricular un estudiante en una carrera
- ✅ c) Recuperar todos los estudiantes ordenados por apellido
- ✅ d) Recuperar un estudiante por número de libreta universitaria
- ✅ e) Recuperar todos los estudiantes por género
- ✅ f) Recuperar las carreras con estudiantes inscriptos ordenadas por cantidad
- ✅ g) Recuperar los estudiantes de una carrera específica filtrados por ciudad
- ✅ h) Generar reporte de las carreras con inscriptos y egresados por año

## Ejecución

### Prerequisitos
- Java 17
- Maven
- Docker (para MySQL)

### Pasos para ejecutar

1. **Iniciar MySQL con Docker:**
```bash
cd Integrador3_grupo8
docker-compose up -d
```

2. **Compilar y ejecutar:**
```bash
mvn clean compile
mvn spring-boot:run
```

3. **La aplicación estará disponible en:** `http://localhost:8081`

## API REST Endpoints

### Estudiantes (`/api/estudiantes`)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/estudiantes` | Crear nuevo estudiante |
| GET | `/api/estudiantes` | Obtener todos los estudiantes ordenados |
| GET | `/api/estudiantes/{dni}` | Obtener estudiante por DNI |
| GET | `/api/estudiantes/libreta/{numeroLU}` | Obtener estudiante por número LU |
| GET | `/api/estudiantes/genero/{genero}` | Obtener estudiantes por género |
| GET | `/api/estudiantes/carrera/{carrera}/ciudad/{ciudad}` | Obtener estudiantes por carrera y ciudad |
| PUT | `/api/estudiantes/{dni}` | Actualizar estudiante |
| DELETE | `/api/estudiantes/{dni}` | Eliminar estudiante |

### Carreras (`/api/carreras`)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/carreras` | Crear nueva carrera |
| POST | `/api/carreras/{carreraId}/matricular/{dni}` | Matricular estudiante en carrera |
| POST | `/api/carreras/matricular` | Matricular estudiante (JSON) |
| GET | `/api/carreras` | Obtener todas las carreras |
| GET | `/api/carreras/con-estudiantes` | Obtener carreras con estudiantes ordenadas |
| GET | `/api/carreras/{id}` | Obtener carrera por ID |
| PUT | `/api/carreras/{id}` | Actualizar carrera |
| DELETE | `/api/carreras/{id}` | Eliminar carrera |

### Reportes (`/api/reportes`)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/reportes/carreras-por-ano` | Reporte de carreras por año |
| GET | `/api/reportes/carreras-por-ano/formateado` | Reporte formateado |
| GET | `/api/reportes/carrera/{nombre}/por-ano` | Reporte de carrera específica |

## Testing con Postman

1. **Importar la colección:** `Postman_Collection.json`
2. **Configurar variable:** `baseUrl = http://localhost:8081/api`
3. **Ejecutar requests** en el orden sugerido

### Ejemplos de uso

#### Crear estudiante:
```json
POST /api/estudiantes
{
  "dni": 99999999,
  "nombre": "Pedro",
  "apellido": "García",
  "edad": 23,
  "genero": "Masculino",
  "ciudadDeResidencia": "Buenos Aires",
  "numeroLU": "LU99999"
}
```

#### Matricular estudiante:
```json
POST /api/carreras/1/matricular/12345678?fechaInscripcion=2024-01-15
```

#### Obtener estudiantes por género:
```
GET /api/estudiantes/genero/Femenino
```

## Arquitectura

### Capas
1. **Controller**: REST endpoints con validación
2. **Service**: Lógica de negocio y transacciones
3. **Repository**: Consultas JPQL optimizadas
4. **Entity**: Mapeo JPA con relaciones

### Características técnicas
- **Clave primaria**: DNI (Integer) para Estudiante
- **Clave compuesta**: DNI + CarreraId para EstudianteDeCarrera
- **Relaciones**: Many-to-Many con tabla intermedia
- **Validaciones**: Duplicados, existencia de entidades
- **Transacciones**: @Transactional en servicios

## Base de datos

### Tablas principales
- **estudiante**: dni (PK), nombre, apellido, edad, genero, ciudad_residencia, numero_lu
- **carrera**: carrera_id (PK), nombre, duracion
- **estudiante_carrera**: dni (PK,FK), carrera_id (PK,FK), fecha_inscripcion, fecha_graduacion, graduado

### Datos de ejemplo
El script `database_setup.sql` incluye datos de prueba con:
- 6 estudiantes
- 8 carreras
- 7 inscripciones

## Diferencias con versiones anteriores
- **Integrador 2**: JPA standalone, resultados en terminal
- **Integrador 3**: Spring Boot con REST endpoints, DNI como PK

---