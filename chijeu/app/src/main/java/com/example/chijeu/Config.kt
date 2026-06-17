package com.example.chijeu

object Config {
    // Si pruebas en el emulador de Android Studio, 10.0.2.2 apunta al localhost de tu PC
    // Si pruebas en un celular físico, cambia esto por la IP de tu PC en la red (ej. "http://192.168.1.50/chijeu_api/")
    const val URL_BASE = "http://10.0.2.2/chijeu_api/"

    const val URL_LOGIN = URL_BASE + "login.php"
    const val URL_LISTAR = URL_BASE + "listar.php"
    const val URL_AGREGAR = URL_BASE + "agregar.php"
    const val URL_EDITAR = URL_BASE + "editar.php"
    const val URL_ELIMINAR = URL_BASE + "eliminar.php"
}
