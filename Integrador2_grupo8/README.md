# Integrador 2 - Sistema de Estudiantes

## Descripción
Sistema de registro de estudiantes universitarios con Spring Boot, MySQL y Docker. Implementa todas las funcionalidades requeridas con consultas JPQL optimizadas y clave compuesta.

## Tecnologías
- Java 17
- Spring Boot 3.5.6
- MySQL 8.0
- JPA/Hibernate
- Docker & Docker Compose

## Estructura del proyecto
```
src/main/java/org/integrador/
├── entity/ (Estudiante, Carrera, EstudianteDeCarrera, EstudianteCarreraId)
├── repository/ (Repositorios con JPQL)
├── service/ (Lógica de negocio)
├── controller/ (API REST)
└── inte/ (Main class)
```

## Entidades
- **Estudiante**: nombre, apellido, edad, género, dni, ciudad, numeroLU
- **Carrera**: nombre, duracion
- **EstudianteDeCarrera**: clave compuesta (estudiante_id + carrera_id), fecha inscripcion, graduado, fecha graduacion
- **EstudianteCarreraId**: clase embeddable para clave compuesta (@EmbeddedId, @Embeddable, @MapsId)

## Requerimientos completados ✅

### 1. Diseño
- ✅ Diagrama de objetos (diagramas.md)
- ✅ Diagrama DER (diagramas.md)
- ✅ Entidades JPA con relaciones correctas
- ✅ Clave compuesta con mejores prácticas JPA


## Endpoints principales
- `POST /api/estudiantes` - Crear estudiante
- `GET /api/estudiantes` - Listar estudiantes ordenados
- `GET /api/estudiantes/libreta/{numeroLU}` - Buscar por libreta
- `GET /api/estudiantes/genero/{genero}` - Buscar por género
- `GET /api/estudiantes/carrera/{carrera}/ciudad/{ciudad}` - Estudiantes por carrera y ciudad
- `POST /api/carreras/matricular` - Matricular estudiante
- `GET /api/carreras/con-estudiantes` - Carreras con estudiantes ordenadas
- `POST /api/data/init` - Inicializar datos de prueba


## Arquitectura

### Capas
1. **Entity**: Estudiante, Carrera, EstudianteDeCarrera, EstudianteCarreraId
2. **Repository**: Consultas JPQL optimizadas
3. **Service**: Lógica de negocio y transacciones
4. **Controller**: API REST con endpoints documentados


## Diagramas del Sistema

### 1. Diagrama de Objetos

```mermaid
classDiagram
    class Estudiante {
        -Long estudianteId
        -String nombre
        -String apellido
        -Integer edad
        -String genero
        -String dni
        -String ciudadDeResidencia
        -String numeroLU
        -List~EstudianteDeCarrera~ carreras
    }

    class Carrera {
        -Long carreraId
        -String nombre
        -Integer duracion
        -List~EstudianteDeCarrera~ estudiantes
    }

    class EstudianteDeCarrera {
        -EstudianteCarreraId id
        -Estudiante estudiante
        -Carrera carrera
        -Date fechaInscripcion
        -Date fechaGraduacion
        -boolean graduado
    }

    Estudiante ||--o{ EstudianteDeCarrera
    Carrera ||--o{ EstudianteDeCarrera
```

### 2. Diagrama Entidad-Relación (DER)

```mermaid
erDiagram
    ESTUDIANTE {
        bigint estudiante_id PK
        varchar nombre
        varchar apellido
        int edad
        varchar genero
        varchar dni UK
        varchar ciudad_residencia
        varchar numero_lu UK
    }

    CARRERA {
        bigint carrera_id PK
        varchar nombre UK
        int duracion
    }

    ESTUDIANTE_CARRERA {
        bigint estudiante_id PK
        bigint carrera_id PK
        date fecha_inscripcion
        date fecha_graduacion
        boolean graduado
    }

    ESTUDIANTE ||--o{ ESTUDIANTE_CARRERA
    CARRERA ||--o{ ESTUDIANTE_CARRERA
```

### Estructura de Tablas

**ESTUDIANTE**
- estudiante_id (PK)
- nombre, apellido, edad, genero
- dni (único), numero_lu (único)
- ciudad_residencia

**CARRERA**  
- carrera_id (PK)
- nombre (único)
- duracion

**ESTUDIANTE_CARRERA**
- estudiante_id (PK, FK)
- carrera_id (PK, FK) 
- fecha_inscripcion
- fecha_graduacion
- graduado (boolean)

**Relaciones:**
- Un estudiante puede estar en varias carreras
- Una carrera puede tener varios estudiantes
- Clave compuesta maneja la relación muchos a muchos

---
