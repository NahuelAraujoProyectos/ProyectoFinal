# ProyectoFinal# Vehicle Registration API

Una API RESTful desarrollada con **Spring Boot** para la gestión de usuarios y registros de vehículos. Este proyecto incluye autenticación, roles de usuario y persistencia en una base de datos MySQL. Ideal para aprender o desarrollar sistemas similares en producción.

## Tabla de Contenidos
- [Descripción](#descripción)
- [Características](#características)
- [Requisitos previos](#requisitos-previos)
- [Instalación](#instalación)
- [Uso](#uso)
- [Contribuir](#contribuir)
- [Licencia](#licencia)

## Descripción

El proyecto **Vehicle Registration API** permite gestionar usuarios con roles y registros de vehículos mediante operaciones CRUD. Incluye autenticación basada en tokens JWT, validaciones de datos, manejo de excepciones personalizadas y persistencia en una base de datos MySQL levantada con Docker.

## Características

- Autenticación y autorización con **Spring Security** y **JWT**.
- API RESTful para gestionar usuarios y vehículos.
- Base de datos en memoria H2 para simplicidad en desarrollo.
- Validaciones personalizadas y manejo centralizado de excepciones.
- Estructura modular para facilitar la escalabilidad.

## Requisitos previos

Asegúrate de tener lo siguiente instalado en tu máquina:
- **Java 17** o superior
- **Maven** (opcional si usas IDEs como IntelliJ)
- **Docker** (para la base de datos)
- Un cliente API como Postman o cURL para probar los endpoints

## Instalación

1. Clona este repositorio en tu máquina local:
   ```bash
   git clone https://github.com/yourusername/vehicleregistration.git
   cd vehicleregistration

2. Construye y ejecuta el proyecto:
    ./mvnw spring-boot:run

3. Accede a la consola de la base de datos H2 en tu navegador:
    URL: http://localhost:8080/h2-console
    JDBC URL: jdbc:h2:mem:testdb
    Usuario: sa
    Contraseña: (deja este campo vacío)
    Desde la consola puedes consultar las tablas y datos.

## Uso

Endpoints principales
Autenticación
    POST /api/auth/register: Registra un nuevo usuario.
    POST /api/auth/login: Inicia sesión y obtiene un token JWT.

Gestión de usuarios
    GET /api/users: Lista todos los usuarios (admin).
    GET /api/users/{id}: Obtiene un usuario por ID.

Gestión de vehículos
    POST /api/vehicles: Crea un registro de vehículo.
    GET /api/vehicles: Lista todos los vehículos.


## Contribuir

¡Las contribuciones son bienvenidas! Si deseas colaborar en este proyecto, sigue estos pasos:

1. Haz un **fork** del repositorio.
    ```bash
    git fork https://github.com/yourusername/vehicleregistration.git

2. Clona tu repositorio forkeado en tu máquina local:
    git clone https://github.com/yourusername/vehicleregistration.git
    cd vehicleregistration

3. Crea una nueva rama para tu funcionalidad o corrección de errores:
    git checkout -b feature/nueva-funcionalidad

4. Realiza los cambios necesarios y haz un commit:
    git add .
    git commit -m "Añadida nueva funcionalidad"

5. Sube tu rama al repositorio remoto:
    git push origin feature/nueva-funcionalidad

6. Abre un Pull Request desde tu repositorio forkeado hacia el repositorio principal.