# Backend de chijeu (guitarras) con PHP + XAMPP

## 1. Copiar la carpeta del backend a XAMPP
Copia la carpeta `chijeu_api` completa dentro de la carpeta `htdocs` de tu instalación de XAMPP, normalmente:

```
C:\xampp\htdocs\chijeu_api
```

## 2. Crear la base de datos
1. Abre el panel de control de XAMPP y arranca **Apache** y **MySQL**.
2. Ve a `http://localhost/phpmyadmin`.
3. Click en la pestaña **Importar** y selecciona el archivo `chijeu_db.sql` (está dentro de `chijeu_api`).
4. Esto crea la base `chijeu_db` con dos tablas:
   - `usuarios` (ya tiene los usuarios admin_musica/guitarra123 y vendedor/musica2024)
   - `guitarras` (vacía, se va llenando desde la app)

## 3. Probar que el backend funciona
Antes de tocar la app, prueba en el navegador o con Postman:

```
http://localhost/chijeu_api/listar.php
```

Debería regresar algo como `{"exito":true,"guitarras":[]}`.

## 4. Configurar la URL en Android
En `Config.kt` ya está puesta la URL para el emulador de Android Studio:

```kotlin
const val URL_BASE = "http://10.0.2.2/chijeu_api/"
```

- Si pruebas en el **emulador**, déjala así (`10.0.2.2` es como el emulador le dice "la PC donde estoy corriendo").
- Si pruebas en un **celular físico**, conecta el celular a la misma red Wi-Fi que tu PC, busca la IP local de tu PC (`ipconfig` en CMD, busca algo como `192.168.x.x`) y cámbiala por esa, por ejemplo:

```kotlin
const val URL_BASE = "http://192.168.1.50/chijeu_api/"
```

## 5. Archivos que se agregaron/cambiaron

**Backend (carpeta `chijeu_api`, nueva):**
- `conexion.php` — conexión a MySQL
- `login.php` — valida usuario/contraseña contra la tabla `usuarios`
- `listar.php` — regresa todas las guitarras registradas
- `agregar.php` — inserta una guitarra nueva
- `editar.php` — actualiza una guitarra existente
- `eliminar.php` — borra una guitarra por id
- `chijeu_db.sql` — script para crear la base y las tablas

**Android (carpeta `chijeu`):**
- `Config.kt` (nuevo) — URLs de los endpoints
- `VolleySingleton.kt` (nuevo) — cola de peticiones de Volley
- `datos.kt` — se agregó el campo `id`, necesario para editar/eliminar en la base
- `AndroidManifest.xml` — se agregó el permiso de `INTERNET` y `usesCleartextTraffic` (para poder hablar con http normal, no https)
- `login.kt` — ahora valida contra `login.php` y carga el catálogo con `listar.php` antes de entrar
- `MainActivity.kt` — `guardar()` ahora manda la guitarra a `agregar.php`
- `actualizar.kt` — `actualizarGuitarra()` ahora manda los cambios a `editar.php`
- `eliminar.kt` — al eliminar, primero pide a `eliminar.php` que borre cada guitarra seleccionada y solo la quita de la lista si el servidor confirma que se borró

Todos los endpoints PHP ya fueron probados (login, agregar, listar, editar, eliminar) corriendo MySQL y un servidor PHP de prueba, así que el flujo completo funciona de punta a punta.
