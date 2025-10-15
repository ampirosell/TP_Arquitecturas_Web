# Integrador 2 - Sistema de Estudiantes

## Descripción
Sistema de registro de estudiantes universitarios con **JPA standalone** (sin Spring Boot), MySQL y Docker. Implementa todas las funcionalidades requeridas con consultas JPQL optimizadas y clave compuesta. Los resultados se muestran en la terminal.

## Tecnologías
- Java 17
- **JPA/Hibernate (standalone)**
- MySQL 8.0
- Docker & Docker Compose
- Maven

## Estructura del proyecto
```
src/main/java/org/integrador/
├── entity/ (Estudiante, Carrera, EstudianteDeCarrera, EstudianteCarreraId)
├── repository/ (Repositorios con JPQL)
├── service/ (Lógica de negocio)
├── DTO/ (Data Transfer Objects)
├── helper/ (CSVReader)
└── inte/ (Main class - ejecuta consultas y muestra resultados en terminal)
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

### 2. Funcionalidades implementadas
- ✅ a) Agregar estudiante
- ✅ b) Matricular estudiante en carrera
- ✅ c) Obtener todos los estudiantes ordenados por apellido
- ✅ d) Obtener estudiante por número de libreta universitaria
- ✅ e) Obtener estudiantes por género
- ✅ f) Obtener carreras con estudiantes inscriptos ordenadas por cantidad
- ✅ g) Obtener estudiantes de una carrera específica que viven en una ciudad específica
- ✅ 3) Generar reporte de carreras con inscriptos y graduados

## Ejecución

### Prerequisitos
- Java 17
- Maven
- Docker (para MySQL)

### Pasos para ejecutar

1. **Iniciar MySQL con Docker:**
```bash
cd Integrador2_grupo8
docker-compose up -d
```

2. **Compilar y ejecutar:**
```bash
cd inte
mvn clean compile
mvn exec:java
```

### Salida esperada
La aplicación ejecutará todas las consultas requeridas y mostrará los resultados en la terminal:

```
=== INTEGRADOR 2 - SISTEMA DE ESTUDIANTES ===
Iniciando aplicación JPA...

1. Cargando datos desde archivos CSV...
✓ Datos cargados exitosamente

2. Agregando nuevo estudiante...
✓ Estudiante agregado: Juan Pérez

3. Matriculando estudiante en carrera...
✓ Estudiante matriculado en carrera: Ingeniería en Sistemas

4. Listando todos los estudiantes:
  - García, María (LU: LU12345)
  - López, Carlos (LU: LU12346)
  ...

=== APLICACIÓN COMPLETADA EXITOSAMENTE ===
```

## Arquitectura

### Capas
1. **Entity**: Estudiante, Carrera, EstudianteDeCarrera, EstudianteCarreraId
2. **Repository**: Consultas JPQL optimizadas
3. **Service**: Lógica de negocio y transacciones
4. **Main**: Ejecuta consultas y muestra resultados en terminal

### Diferencias con Integrador 3
- **Integrador 2**: JPA standalone, resultados en terminal
- **Integrador 3**: Spring Boot con REST endpoints


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
