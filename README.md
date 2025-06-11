# 📘 Manual de Usuario de **OWTunnel**

## 🧭 Introducción

**OWTunnel** es una solución integral para la gestión, conexión y administración de servicios VPN (Red Privada Virtual), pensada para ofrecer una experiencia de navegación segura, privada y sin restricciones geográficas. Está diseñada para cubrir tanto las necesidades de los usuarios finales como de los administradores del sistema, combinando accesibilidad y control. El sistema se basa en una arquitectura moderna, escalable y multiplataforma, compuesta por tres aplicaciones interconectadas:

### 🔌 API REST

- Gestiona toda la lógica de negocio y seguridad.
- Expone endpoints seguros para autenticación, usuarios, servidores, suscripciones, pagos y conexiones.
- Sirve como núcleo común para la aplicación de gestión y la aplicación VPN.

### 🖥️ Aplicación de Gestión

- Dirigida al personal con roles de administrador o soporte técnico.
- Permite la gestión centralizada de usuarios, suscripciones, servidores y estadísticas.
- Ejecutable en sistemas de escritorio (Windows, Linux, macOS).

### 📱 Aplicación VPN

- Pensada para el usuario final.
- Permite registrarse, gestionar su cuenta y conectarse fácilmente a servidores VPN de forma automática o manual.
- Disponible para dispositivos móviles y ordenadores de escritorio.

## 🧑‍💻 Sobre este manual

Este documento está dividido en tres bloques, uno por cada aplicación del sistema. Cada sección ofrece una explicación clara de:

- Sus funciones principales
- Los requisitos de uso
- Casos de uso, flujos de navegación y posibles errores

El objetivo es proporcionar una guía accesible tanto para usuarios técnicos como no técnicos, diferenciando claramente el uso según el rol asignado: administrador, soporte o usuario final.

---

## 📑 Índice

### 🔌 API REST

&nbsp;&nbsp;1.1.&nbsp;&nbsp; Funciones principales.  
&nbsp;&nbsp;1.2.&nbsp;&nbsp; Requisitos previos.  
&nbsp;&nbsp;1.3.&nbsp;&nbsp; Iniciar la API.  
&nbsp;&nbsp;1.4.&nbsp;&nbsp; Autenticación y seguridad.  
&nbsp;&nbsp;1.5.&nbsp;&nbsp; Pruebas con Postman.  
&nbsp;&nbsp;1.6.&nbsp;&nbsp; Estructura y mantenimiento.  

### 🖥️ Aplicación de Gestión

&nbsp;&nbsp;2.1.&nbsp;&nbsp; Funciones principales.  
&nbsp;&nbsp;2.2.&nbsp;&nbsp; Requisitos del sistema.  
&nbsp;&nbsp;2.3.&nbsp;&nbsp; Inicio de sesión y roles.  
&nbsp;&nbsp;2.4.&nbsp;&nbsp; Módulos de gestión.  
&nbsp;&nbsp;2.5.&nbsp;&nbsp; Interfaz y accesibilidad.  
&nbsp;&nbsp;2.6.&nbsp;&nbsp; Exportación de datos.  

### 📱 Aplicación VPN

&nbsp;&nbsp;3.1.&nbsp;&nbsp; Funciones principales.  
&nbsp;&nbsp;3.2.&nbsp;&nbsp; Requisitos.  
&nbsp;&nbsp;3.3.&nbsp;&nbsp; Navegación por la app.  
&nbsp;&nbsp;3.4.&nbsp;&nbsp; Configuraciones adicionales.  
&nbsp;&nbsp;3.5.&nbsp;&nbsp; Manejo de errores y soporte.  

---

## 1.1. Funciones principales

La API REST es el núcleo del sistema OWTunnel. Su función es exponer endpoints seguros y estructurados que permiten:

- Autenticación y registro de usuarios.
- Gestión de roles (usuario, soporte, administrador).
- Control de suscripciones, pagos y planes.
- Administración de servidores VPN.
- Registro y consulta de conexiones realizadas.
- Soporte para operaciones de la app VPN y de gestión.

## 1.2. Requisitos previos

- **Java 21+**
- **Maven 3.6+**
- Base de datos MySQL (puede ejecutarse en local o remoto)

## 1.3. Iniciar la API

Desde el directorio del proyecto (`rest-api/`), se puede ejecutar en modo desarrollo o empaquetado:

```bash
# Ejecutar directamente o en un IDE
./mvnw spring-boot:run

# O compilar y ejecutar el JAR
./mvnw clean package
java -jar target/rest-api-0.0.1-SNAPSHOT.jar
```

La API estará disponible por defecto en:

```
http://localhost:8080
```

## 1.4. Autenticación y seguridad

La API utiliza autenticación basada en **JWT (JSON Web Tokens)**.

### Pasos para autenticarse:

1. Realiza una solicitud `POST` al endpoint `/auth/login` con los datos de acceso (correo y contraseña).
2. Recibirás un **token JWT** si las credenciales son válidas.
3. Añade el token en las siguientes peticiones como cabecera HTTP:

## 1.5. Pruebas con Postman

Para probar la API de forma manual, puedes usar [Postman](https://www.postman.com/):

1. Inicia la API localmente.
2. Envía una solicitud `POST` a `/auth/login` con las credenciales.
3. Copia el token recibido.
4. Añade el token en cada petición como **Bearer Token**:

```
Type: Bearer Token
Token: <tu_token>
```
![alt text](images/image_01.png)

## 1.6. Estructura y mantenimiento

- **Controladores (`controller`)**  
  Gestionan las solicitudes HTTP entrantes y exponen los endpoints REST. Delegan la lógica de negocio a los servicios.  
  Ejemplo: `UserController`, `AuthController`

- **Servicios (`service`)**  
  Implementan la lógica de negocio del sistema. Validan datos, gestionan procesos internos y comunican con los repositorios.  
  Ejemplo: `UserService`, `SubscriptionService`

- **Repositorios (`repository`)**  
  Interactúan con la base de datos mediante Spring Data JPA. Incluyen métodos CRUD y consultas personalizadas.  
  Ejemplo: `UserRepository`, `ServerRepository`

- **DTOs (`dto`)**  
  Objetos de transferencia de datos. Encapsulan la información intercambiada entre cliente, controladores y servicios.  
  Se suelen dividir en:
  - `request/`: datos entrantes (desde el cliente)
  - `response/`: datos salientes (hacia el cliente)  
  Ejemplo: `LoginRequest`, `UserResponse`

- **Modelos (`model` o `entity`)**  
  Representan las entidades del dominio y se corresponden con las tablas de la base de datos.  
  Ejemplo: `User`, `Subscription`, `Server`

- **Mappers (`mapper`)**  
  Transforman entidades (`model`) en DTOs y viceversa.  
  Ejemplo: `UserMapper`, `SubscriptionMapper`

- **Configuración (`config`)**  
  Contiene la configuración general del proyecto: seguridad, CORS, JWT, Swagger, etc.  
  Ejemplo: `SecurityConfig`, `CorsConfig`

- **Seguridad (`security`)**  
  Componentes relacionados con la autenticación y autorización: filtros, tokens, detalles de usuario.  
  Ejemplo: `JwtAuthenticationFilter`, `UserDetailsServiceImpl`

- **Excepciones (`exception`)**  
  Gestión centralizada de errores para devolver respuestas claras y controladas.  
  Ejemplo: `GlobalExceptionHandler`, `CustomException`

### Archivos importantes:

- `Application.java` – Punto de entrada principal de la API
- `pom.xml` – Gestión de dependencias y configuración de proyecto
- `application.properties` – Configuración de entorno (puertos, base de datos, JWT, CORS)

---

# Aplicación de Gestión

## 2.1. Funciones principales

La aplicación de gestión está diseñada para personal con rol de **administrador** o **soporte**, y permite:

- Visualizar, crear, editar y eliminar usuarios
- Consultar y modificar suscripciones
- Revisar pagos y exportar datos
- Administrar servidores VPN y planes
- Obtener métricas del sistema

## 2.2. Requisitos del sistema

- **Sistema operativo**: Windows, Linux o macOS
- **Modo desarrollo**:
  - Node.js 18+
  - npm
- **Modo producción**:
  - App empaquetada como ejecutable (`.exe`, `.dmg`, `.AppImage`, etc.)

## 2.3. Inicio de sesión y roles

Al iniciar, se solicita correo y contraseña. El backend determina el **rol** del usuario:

| Rol           | Permisos principales                |
|---------------|-------------------------------------|
| Administrador | Acceso completo a todos los módulos |
| Soporte       | Acceso completo a todos los módulos |

![alt text](images/image_03.png)

## 2.4. Módulos de gestión

### 📊 Panel de métricas
- Usuarios registrados
- Uusarios subscritos
- Conexiones activas
- Servidores activos
- Ingresos totales

![alt text](images/image_04.png)

### Usuarios
- Listado con filtros por rol, idioma y estado
- Botones para editar, desactivar o eliminar
- Acceso al historial de pagos y conexiones

![alt text](images/image_05.png)

### Servidores VPN
- Alta, edición y eliminación de servidores
- Atributos: país, ciudad, dirección IP, activo/inactivo

![alt text](images/image_06.png)

### Connexiones
- Ver el historial de conexiones realizadas por los usuarios
- Consultar IP, servidor utilizado, duración y fecha de la conexión

![alt text](images/image_07.png)

### Planes
- Crear o modificar duración, precio y nombre de cada plan
- Activar/desactivar disponibilidad pública

![alt text](images/image_08.png)

### Suscripciones y pagos
- Ver historial de pagos por usuario
- Cambiar plan o cancelar renovación automática
- Filtrado por fechas y exportación

![alt text](images/image_09.png)

![alt text](images/image_10.png)

## 2.5. Interfaz y accesibilidad

- **Modo claro / oscuro**
- **Diseño responsivo** compatible con pantallas 720p+
- **Atajos de teclado y menús accesibles**

![alt text](images/image_11.png)

## 2.6. Exportación de datos

Todos los módulos con tablas ofrecen botón de exportación `.CSV`:

- Usuarios
- Servidores
- Conexiones
- Planes
- Suscripciones
- Pagos

Ideal para auditorías o análisis externos.

![alt text](images/image_12.png)

---

# Aplicación VPN (Flutter)

## 3.1. Funciones principales

La aplicación VPN es el componente cliente principal de OWTunnel y está destinada al usuario final. Permite:

- Registrar y autenticar usuarios
- Conectarse de forma segura a servidores VPN
- Seleccionar servidores por país o automáticamente
- Gestionar el perfil y la suscripción
- Ejecutarse en dispositivos móviles y escritorio

## 3.2. Requisitos

- **Móvil**: Android (SDK 33+), iOS (13+)
- **Escritorio**: Windows, Linux, macOS
- **Flutter** 3.10+ instalado si se ejecuta en modo desarrollo

## 3.3. Navegación por la app

### Login y registro

- Se puede crear cuenta nueva o iniciar sesión.
- Validación por email/contraseña.

![alt text](images/image_13.png)

![alt text](images/image_14.png)

![alt text](images/image_15.png)

![alt text](images/image_16.png)

### Conexión VPN

- Botón central para conectar/desconectar
- Estado mostrado en tiempo real
- Selección manual de país, ciudad o latencia.
- Opción de conexión automática.

![alt text](images/image_17.png)

### Perfil

- Detalles del usuario
- Cambio de contraseña

![alt text](images/image_18.png)

![alt text](images/image_19.png)

### Suscripción

- Ver el plan actual y su vencimiento
- Cambiar o renovar la suscripción

![alt text](images/image_20.png)

![alt text](images/image_21.png)

## 3.4. Configuraciones adicionales

- Cambiar idioma o región
- Activar modo oscuro
- Preferencias de notificaciones

![alt text](images/image_22.png)

## 3.5. Manejo de errores y soporte

- Mensajes claros en caso de errores de conexión o expiración de sesión
- Enlace directo al soporte técnico o FAQ