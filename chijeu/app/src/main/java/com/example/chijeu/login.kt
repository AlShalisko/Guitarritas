package com.example.chijeu

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Request // métodos HTTP (POST, GET, etc.)
import com.android.volley.toolbox.StringRequest // esperamos texto/JSON como respuesta
import org.json.JSONObject // para leer la respuesta que manda el php

class login : AppCompatActivity() {
    lateinit var etUsuario: EditText
    lateinit var etPassword: EditText
    lateinit var btnIniciarSesion: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        etUsuario = findViewById(R.id.etUsuario)
        etPassword = findViewById(R.id.etPassword)
        btnIniciarSesion = findViewById(R.id.btnLogin)

        btnIniciarSesion.setOnClickListener {
            iniciarSesion()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun iniciarSesion() {
        val usuario = etUsuario.text.toString().trim()
        val password = etPassword.text.toString().trim()

        if (usuario.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, ingresa tus credenciales", Toast.LENGTH_SHORT).show()
            return
        }

        // Ahora el usuario/contraseña se valida contra la tabla "usuarios" en MySQL,
        // ya no contra lo que teníamos guardado en SharedPreferences
        val peticion = object : StringRequest(
            Request.Method.POST,
            Config.URL_LOGIN,
            { respuesta ->
                try {
                    val json = JSONObject(respuesta)
                    if (json.getBoolean("exito")) {
                        val rol = json.getString("rol")
                        val prefs = getSharedPreferences("sesion", Context.MODE_PRIVATE)
                        prefs.edit().putString("rol", rol).apply()

                        // Antes de entrar, traemos el catálogo actualizado desde el servidor
                        cargarGuitarras(rol)
                    } else {
                        Toast.makeText(this, json.getString("mensaje"), Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this, "Error al leer la respuesta", Toast.LENGTH_SHORT).show()
                }
            },
            { error ->
                Toast.makeText(this, "Error de conexión: ${error.message}", Toast.LENGTH_LONG).show()
            }
        ) {
            override fun getParams(): MutableMap<String, String> {
                return hashMapOf("nombre" to usuario, "clave" to password)
            }
        }

        VolleySingleton.getInstance(this).requestQueue.add(peticion)
    }

    private fun cargarGuitarras(rol: String) {
        val peticion = object : StringRequest(
            Request.Method.POST,
            Config.URL_LISTAR,
            { respuesta ->
                try {
                    val json = JSONObject(respuesta)
                    if (json.getBoolean("exito")) {
                        guitarra.listaGuitarras.clear()
                        val arreglo = json.getJSONArray("guitarras")
                        for (i in 0 until arreglo.length()) {
                            val obj = arreglo.getJSONObject(i)
                            guitarra.listaGuitarras.add(
                                datos(
                                    id = obj.getInt("id"),
                                    modelo = obj.getString("modelo"),
                                    marca = obj.getString("marca"),
                                    fabricante = obj.getString("fabricante"),
                                    precio = obj.getDouble("precio"),
                                    pais = obj.getString("pais"),
                                    telefono = obj.getString("telefono"),
                                    tipocuerpo = obj.getString("tipocuerpo"),
                                    tipopastillas = obj.getString("tipopastillas"),
                                    puente = obj.getString("puente"),
                                    madera = obj.getString("madera"),
                                    cuerdas = obj.getString("cuerdas")
                                )
                            )
                        }
                    }

                    // Mismo comportamiento de antes: admin siempre entra,
                    // trabajador solo si ya hay guitarras registradas
                    if (rol == "Administrador") {
                        startActivity(Intent(this, MainActivity::class.java))
                    } else if (guitarra.listaGuitarras.isEmpty()) {
                        Toast.makeText(this, "No hay guitarras en el inventario", Toast.LENGTH_SHORT).show()
                    } else {
                        startActivity(Intent(this, ver::class.java))
                    }
                } catch (e: Exception) {
                    Toast.makeText(this, "Error al leer la respuesta", Toast.LENGTH_SHORT).show()
                }
            },
            { error ->
                Toast.makeText(this, "Error de conexión: ${error.message}", Toast.LENGTH_LONG).show()
            }
        ) {}

        VolleySingleton.getInstance(this).requestQueue.add(peticion)
    }
}
