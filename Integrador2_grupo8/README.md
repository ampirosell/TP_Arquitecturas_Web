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

### 2. Consultas implementadas (JPQL)

**a)** Dar de alta un estudiante
- `POST /api/estudiantes`

**b)** Matricular un estudiante en una carrera
- `POST /api/carreras/matricular`

**c)** Recuperar todos los estudiantes ordenados
- `GET /api/estudiantes`
- JPQL: `SELECT e FROM Estudiante e ORDER BY e.apellido, e.nombre`

**d)** Recuperar estudiante por numero LU
- `GET /api/estudiantes/libreta/{numeroLU}`
- JPQL: `SELECT e FROM Estudiante e WHERE e.numeroLU = :numeroLU`

**e)** Recuperar estudiantes por género
- `GET /api/estudiantes/genero/{genero}`
- JPQL: `SELECT e FROM Estudiante e WHERE e.genero = :genero`

**f)** Carreras con estudiantes ordenadas por cantidad
- `GET /api/carreras/con-estudiantes`
- JPQL: `SELECT c FROM Carrera c ORDER BY (SELECT COUNT(ec) FROM EstudianteDeCarrera ec WHERE ec.carrera.id = c.id) DESC`

**g)** Estudiantes de carrera filtrados por ciudad
- `GET /api/estudiantes/carrera/{carrera}/ciudad/{ciudad}`
- JPQL: `SELECT DISTINCT e FROM Estudiante e JOIN e.carreras ec JOIN ec.carrera c WHERE c.nombre = :nombreCarrera AND e.ciudadDeResidencia = :ciudad`

**3)** Reporte de carreras por año
- `GET /api/reportes/carreras-por-ano/formateado`
- JPQL: `SELECT c.nombre, YEAR(ec.fechaInscripcion), COUNT(CASE WHEN ec.graduado = false THEN 1 END) as inscriptos, COUNT(CASE WHEN ec.graduado = true THEN 1 END) as egresados FROM EstudianteDeCarrera ec JOIN ec.carrera c GROUP BY c.nombre, YEAR(ec.fechaInscripcion) ORDER BY c.nombre, YEAR(ec.fechaInscripcion)`

## Endpoints principales
- `POST /api/estudiantes` - Crear estudiante
- `GET /api/estudiantes` - Listar estudiantes ordenados
- `GET /api/estudiantes/libreta/{numeroLU}` - Buscar por libreta
- `GET /api/estudiantes/genero/{genero}` - Buscar por género
- `GET /api/estudiantes/carrera/{carrera}/ciudad/{ciudad}` - Estudiantes por carrera y ciudad
- `POST /api/carreras/matricular` - Matricular estudiante
- `GET /api/carreras/con-estudiantes` - Carreras con estudiantes ordenadas
- `GET /api/reportes/carreras-por-ano/formateado` - Reporte por año
- `POST /api/data/init` - Inicializar datos de prueba

## Como ejecutar

### Opción 1: Docker (Recomendado) 🐳
```bash
# Windows
docker-run.bat

# Linux/Mac
./docker-run.sh

# O manualmente
docker-compose up --build
```

### Opción 2: Local
1. Crear base de datos MySQL: `inte_db`
2. Ejecutar: `mvn spring-boot:run`
3. Inicializar datos: `curl -X POST http://localhost:8080/api/data/init`

**Ver DOCKER.md para más detalles**

## Arquitectura

### Capas
1. **Entity**: Estudiante, Carrera, EstudianteDeCarrera, EstudianteCarreraId
2. **Repository**: Consultas JPQL optimizadas
3. **Service**: Lógica de negocio y transacciones
4. **Controller**: API REST con endpoints documentados

### Archivos principales
- **Entidades**: `Estudiante.java`, `Carrera.java`, `EstudianteDeCarrera.java`, `EstudianteCarreraId.java`
- **Repositorios**: `EstudianteRepository.java`, `CarreraRepository.java`, `EstudianteDeCarreraRepository.java`
- **Servicios**: `EstudianteService.java`, `CarreraService.java`, `ReporteService.java`
- **Controladores**: `EstudianteController.java`, `CarreraController.java`, `ReporteController.java`, `DataController.java`

## Configuración

### Local
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/inte_db
spring.datasource.username=root
spring.datasource.password=""
spring.jpa.hibernate.ddl-auto=update
```

### Docker
```properties
spring.datasource.url=jdbc:mysql://mysql:3306/inte_db
spring.datasource.username=inte_user
spring.datasource.password=inte_pass
```

## Cumplimiento de requerimientos

| Requerimiento | Estado | Implementación |
|---------------|--------|----------------|
| Diagrama objetos | ✅ | diagramas.md |
| Diagrama DER | ✅ | diagramas.md |
| Alta estudiante | ✅ | POST /api/estudiantes |
| Matricular estudiante | ✅ | POST /api/carreras/matricular |
| Listar estudiantes ordenados | ✅ | GET /api/estudiantes |
| Buscar por libreta | ✅ | GET /api/estudiantes/libreta/{numeroLU} |
| Buscar por género | ✅ | GET /api/estudiantes/genero/{genero} |
| Carreras por cantidad | ✅ | GET /api/carreras/con-estudiantes |
| Estudiantes por carrera/ciudad | ✅ | GET /api/estudiantes/carrera/{carrera}/ciudad/{ciudad} |
| Reporte por año | ✅ | GET /api/reportes/carreras-por-ano/formateado |
| Consultas JPQL | ✅ | Todas implementadas en JPQL |
| Ordenamiento alfabético | ✅ | Carreras ordenadas alfabéticamente |
| Ordenamiento cronológico | ✅ | Años ordenados cronológicamente |
| Clave compuesta | ✅ | @EmbeddedId, @Embeddable, @MapsId |
| Docker | ✅ | docker-compose.yml completo |

**Proyecto completo y funcional** ✅

---

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

## Configuración Docker Completa 🐳

### Archivos Docker
- `Dockerfile` - Imagen de la aplicación Spring Boot
- `docker-compose.yml` - Orquestación de servicios (app + MySQL)
- `.dockerignore` - Archivos a ignorar en la construcción
- `application-docker.properties` - Configuración específica para Docker
- `docker-run.bat` / `docker-run.sh` - Scripts de ejecución

### Servicios incluidos

**MySQL Database**
- Puerto: 3306
- Usuario: inte_user
- Password: inte_pass
- Base de datos: inte_db
- Volumen persistente: mysql_data

**Spring Boot App**
- Puerto: 8080
- URL: http://localhost:8080
- Perfil: docker

### Comandos Docker útiles

```bash
# Ver contenedores en ejecución
docker-compose ps

# Ver logs de la aplicación
docker-compose logs -f app

# Ver logs de MySQL
docker-compose logs -f mysql

# Detener servicios
docker-compose down

# Detener y eliminar volúmenes
docker-compose down -v

# Reiniciar servicios
docker-compose restart

# Acceder a MySQL
docker-compose exec mysql mysql -u inte_user -p inte_db

# Backup de la base de datos
docker-compose exec mysql mysqldump -u inte_user -p inte_db > backup.sql
```

### Solución de problemas Docker

**Puerto ocupado:**
```bash
# Cambiar puertos en docker-compose.yml
ports:
  - "8081:8080"  # App
  - "3307:3306"  # MySQL
```

**Reconstruir desde cero:**
```bash
docker-compose down -v
docker-compose up --build
```

**Ventajas de Docker:**
- ✅ Aislamiento completo del sistema
- ✅ Consistencia en cualquier máquina
- ✅ Un solo comando para ejecutar todo
- ✅ Portabilidad total
- ✅ Fácil reset y pruebas

---

## Mejoras Implementadas - Clave Compuesta

### Implementación con mejores prácticas JPA

**1. Clase EstudianteCarreraId (@Embeddable)**
```java
@Embeddable
public class EstudianteCarreraId implements Serializable {
    private Long estudianteId;
    private Long carreraId;
    // equals() y hashCode() obligatorios
}
```

**2. Entidad EstudianteDeCarrera actualizada**
```java
@Entity
public class EstudianteDeCarrera {
    @EmbeddedId
    private EstudianteCarreraId id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("estudianteId")
    @JoinColumn(name = "estudiante_id", nullable = false)
    private Estudiante estudiante;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("carreraId")
    @JoinColumn(name = "carrera_id", nullable = false)
    private Carrera carrera;
}
```

**3. Base de datos con clave compuesta**
```sql
CREATE TABLE estudiante_carrera (
    estudiante_id BIGINT NOT NULL,
    carrera_id BIGINT NOT NULL,
    fecha_inscripcion DATE NOT NULL,
    fecha_graduacion DATE NULL,
    graduado BOOLEAN NOT NULL DEFAULT FALSE,
    PRIMARY KEY (estudiante_id, carrera_id),  -- Clave compuesta
    FOREIGN KEY (estudiante_id) REFERENCES estudiante(estudiante_id),
    FOREIGN KEY (carrera_id) REFERENCES carrera(carrera_id)
);
```

### Ventajas de la implementación

**✅ Mejores prácticas JPA:**
- @EmbeddedId: Clave primaria compuesta
- @Embeddable: Clase reutilizable para clave compuesta  
- @MapsId: Mapeo automático de relaciones

**✅ Diseño mejorado:**
- Clave primaria natural (estudiante_id + carrera_id)
- Eliminación de ID artificial innecesario
- Mejor integridad referencial

**✅ Rendimiento optimizado:**
- Consultas más eficientes con clave compuesta
- Menos redundancia en la base de datos
- Mejor uso de índices

**✅ Código más limpio:**
- Relaciones más claras entre entidades
- Validación automática de unicidad
- Menos código de validación manual
