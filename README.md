# ProyectoFinal - Vehicle Registration API

Una API RESTful desarrollada con **Spring Boot** para la gestión de usuarios y registros de vehículos. Este proyecto incluye autenticación, roles de usuario y persistencia en una base de datos **H2** en memoria. Ideal para aprender o desarrollar sistemas similares en entornos de desarrollo.

## Tabla de Contenidos
- [Descripción](#descripción)
- [Características](#características)
- [Requisitos previos](#requisitos-previos)
- [Instalación](#instalación)
- [Uso](#uso)
- [Documentación con Swagger](#documentación-con-swagger)
- [Contribuir](#contribuir)
- [Licencia](#licencia)

## Descripción

El proyecto **Vehicle Registration API** permite gestionar usuarios con roles y registros de vehículos mediante operaciones CRUD. Incluye autenticación basada en tokens JWT, validaciones de datos, manejo de excepciones personalizadas y persistencia en una base de datos MySQL levantada con Docker.

## Características

- Autenticación y autorización con **Spring Security** y **JWT**.
- API RESTful para gestionar usuarios y vehículos.
- Base de datos en memoria **H2** para simplicidad en desarrollo.
- Documentación interactiva de la API con **Swagger UI**.
- Validaciones personalizadas y manejo centralizado de excepciones.
- Estructura modular para facilitar la escalabilidad.

## Requisitos previos

Asegúrate de tener lo siguiente instalado en tu máquina:
- **Java 17** o superior
- **Maven** (opcional si usas IDEs como IntelliJ)
- Un cliente API como Postman o cURL para probar los endpoints

## Instalación

1. Clona este repositorio en tu máquina local:
   ```bash
   git clone https://github.com/yourusername/vehicleregistration.git
   cd vehicleregistration

2. Construye y ejecuta el proyecto:
   ```bash
   ./mvnw spring-boot:run

4. Accede a la consola de la base de datos H2 en tu navegador:
   ```bash
   URL: http://localhost:8080/h2-console
   JDBC URL: jdbc:h2:mem:testdb
   Usuario: sa
   Contraseña: password
   Desde la consola puedes consultar las tablas y datos.

## Uso -Endpoints principales

Autenticación
- POST /signup: Registra un nuevo usuario.
    - Body: Datos del nuevo usuario (nombre, correo, contraseña, rol).
- POST /login: Inicia sesión y obtiene un token JWT para la autenticación.
    - Body: Credenciales del usuario (correo y contraseña).

Gestión de usuarios
- GET /users/get/me: Obtiene los datos del perfil del usuario autenticado.
- GET /users/get/{id}: Obtiene un perfil de usuario por su ID (solo accesible para administradores).

Gestión de vehículos
- POST /cars/add: Agrega un nuevo vehículo al sistema.
    - Body: Información del vehículo (marca, modelo, año, etc.).
- GET /cars/get/{id}: Obtiene los detalles de un vehículo por su ID.

Roles y permisos:
- ADMIN: Accede a todos los endpoints, incluyendo la gestión de usuarios y vehículos.
- CLIENT: Puede gestionar su propio perfil y vehículos, pero no tiene acceso a la administración global de usuarios o vehículos.
Cada endpoint está protegido y requiere autenticación mediante el token JWT obtenido al hacer login.

## Documentación con Swagger
Swagger UI está habilitado para documentar y probar la API. 
- Puedes acceder a la interfaz en:
    URL: http://localhost:8080/swagger-ui.html

- Para obtener los esquemas JSON de la API, puedes usar:
    OpenAPI Docs: http://localhost:8080/api-docs

Swagger facilita explorar y probar los endpoints directamente desde el navegador.

## Contribuir

¡Las contribuciones son bienvenidas! Si deseas colaborar en este proyecto, sigue estos pasos:

1. Haz un **fork** del repositorio.
    ```bash
    git fork https://github.com/yourusername/vehicleregistration.git

2. Clona tu repositorio forkeado en tu máquina local:
   ```bash
   git clone https://github.com/yourusername/vehicleregistration.git
   cd vehicleregistration

4. Crea una nueva rama para tu funcionalidad o corrección de errores:
   ```bash
   git checkout -b feature/nueva-funcionalidad

6. Realiza los cambios necesarios y haz un commit:
   ```bash
   git add .
   git commit -m "Añadida nueva funcionalidad"

8. Sube tu rama al repositorio remoto:
   ```bash
   git push origin feature/nueva-funcionalidad

10. Abre un Pull Request desde tu repositorio forkeado hacia el repositorio principal.

## Licencia

Este proyecto está licenciado bajo la [Licencia GPL-3.0](https://www.gnu.org/licenses/gpl-3.0.html), lo que significa que puedes usar, modificar y distribuir el código bajo los términos de la GPL-3.0. 

Para más detalles, consulta el archivo [LICENSE](LICENSE) en este repositorio.