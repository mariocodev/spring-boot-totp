# 🚀 Spring Boot TOTP Authentication System

<div style="text-align: center">

![Java](https://img.shields.io/badge/Java-17%2B-blue)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.1%2B-brightgreen)
![H2 Database](https://img.shields.io/badge/H2-Database-lightgrey)

</div>

Implementación de autenticación TOTP (Time-Based One-Time Password) en Spring Boot y H2 Database

## 📋 Tabla de Contenidos

1. [Estructura del Proyecto](#-estructura-del-proyecto)
2. [Cómo Probar la Aplicación](#-cómo-probar-la-aplicación)
3. [Acceso a la Consola H2](#-acceso-a-la-consola-h2)
4. [Endpoints API](#-endpoints-api)
5. [Tecnologías utilizadas](#-tecnologías-utilizadas)
6. [Consideraciones](#-consideraciones)

## 🏗 Estructura del Proyecto

```bash

src/
├── main/
│ ├── java/
│ │ └── com/demo/totp
│ │ ├── controller/ # Controladores REST
│ │ ├── dto/ # Objetos de Transferencia
│ │ ├── model/ # Entidades
│ │ ├── repository/ # Repositorios
│ │ ├── service/ # Lógica de negocio
│ │ └── TotpApplication.java
│ └── resources/
│ ├── application.yml
│ └── static/ # Recursos estáticos
└── test/ # Pruebas

```

## 🧪 Cómo Probar la Aplicación

### Requisitos

- JDK 17+
- Maven 3.8+
- Aplicación autenticadora (Google/Microsoft Authenticator)

### Pasos:

Copiar el archivo denominado `application-example.yml` que se encuentra en el directorio `src/main/resource`s y renombrarlo como el environment seleccionado a levantar, ejemplo `application-local.yml`. Modificar los valores de las propiedades por los del ambiente local.

1. **Iniciar la aplicación**:

```bash
   mvn spring-boot:run -P local
```

2. **Configurar TOTP para un usuario**:

```http
    GET /api/totp/setup/{username}
```
Ejemplo: http://localhost:8080/api/totp/setup/usuario1

3. **Obtener código QR**:

```http
    GET /api/totp/qr/{username}
```

Ejemplo: http://localhost:8080/api/totp/qr/usuario1

4. **Validar código**:

```http
    POST /api/totp/validate
```

| Parameter  | Type     | Description                       |
|:-----------| :------- | :-------------------------------- |
| `username` | `string` | **Required**. Nombre del usuario |
| `code`     | `string` | **Required**. Código generado en aplicación TOTP |

Ejemplo:
* Body: {"username": "usuario1", "code": "123456"}


## 🖥 Acceso a la Consola H2

Durante el desarrollo, accede a la consola H2:

* URL: http://localhost:8080/h2-console
* Credenciales:
  - JDBC URL: jdbc:h2:mem:totpdb
  - User: sa
  - Password: (dejar vacío)

## 📡 Endpoints API

| Método | Endpoint | Descripción |
| :----- | :------- | :---------- |
| `GET`  |   `/api/totp/setup/{username}`  |   Configura TOTP para un usuario    |
| `GET`  |	`/api/totp/qr/{username}` |	Genera código QR para autenticador  |
| `POST` |	`/api/totp/validate` |	Valida un código TOTP   |

## 🛠 Tecnologías Utilizadas

* Spring Boot 3.1+
* H2 Database (en memoria)
* Lombok (reducción de código boilerplate)
* ZXing (generación de códigos QR)
* Java OTP (implementación TOTP)

## ⚡️ Consideraciones

Esta implementación proporciona una solución completa con:

* Arquitectura por capas bien definida
* Documentación de métodos
* Uso de Lombok para reducir código boilerplate
* Persistencia con H2 (en memoria)
* Generación y validación de códigos TOTP
* Generación de QR codes
