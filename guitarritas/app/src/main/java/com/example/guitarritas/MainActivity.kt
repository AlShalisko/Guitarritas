package com.example.guitarritas

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    // 6 EditTexts (Misma cantidad que antes)
    lateinit var Modelo: EditText
    lateinit var Marca: EditText
    lateinit var Fabricante: EditText
    lateinit var Precio: EditText
    lateinit var Pais: EditText
    lateinit var Telefono: EditText

    lateinit var TipoCuerpo: Spinner
    lateinit var TipoPastillas: Spinner
    lateinit var Puente: Spinner
    lateinit var Maderas: Spinner
    lateinit var Cuerdas: Spinner

    lateinit var Guardar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Inicialización de los 6 EditTexts usando los IDs existentes en tu XML
        Modelo = findViewById<EditText>(R.id.etModelo)
        Marca = findViewById<EditText>(R.id.etMarca)
        Fabricante = findViewById<EditText>(R.id.etFabricante)
        Precio = findViewById<EditText>(R.id.etPrecio)
        Pais = findViewById<EditText>(R.id.etPais)
        Telefono = findViewById<EditText>(R.id.etTelefono)
        TipoCuerpo = findViewById<Spinner>(R.id.spTipoCuerpo)
        TipoPastillas = findViewById<Spinner>(R.id.spTipoPastillas)
        Puente = findViewById<Spinner>(R.id.spPuente)
        Maderas = findViewById<Spinner>(R.id.spMadera)
        Cuerdas = findViewById<Spinner>(R.id.spCuerdas)

        Guardar = findViewById<Button>(R.id.btnGuardar)

        val tipocuerpo = arrayOf("Sólido", "Hueco", "Semi-hueco")
        val tipopastillas = arrayOf("Single Coil", "Humbucker", "P90", "Activas")
        val puente = arrayOf("Fijo", "Trémolo", "Floyd Rose")
        val madera = arrayOf("Caoba", "Arce", "Palosanto", "Aliso", "Fresno", "Ebano", "Nogal")
        val cuerdas = arrayOf("6 cuerdas", "7 cuerdas", "8 cuerdas", "12 cuerdas", "4 cuerdas", "5 cuerdas")

        Guardar.setOnClickListener {
            guardar()
        }

        // Adaptadores usando los mismos IDs de Spinner y Layouts
        val cuerpoAdapter = ArrayAdapter(this, R.layout.item_spinner, R.id.textoSpinner, tipocuerpo)
        cuerpoAdapter.setDropDownViewResource(R.layout.item_spinner)
        TipoCuerpo.adapter = cuerpoAdapter

        val pastillasAdapter = ArrayAdapter(this, R.layout.item_spinner, R.id.textoSpinner, tipopastillas)
        pastillasAdapter.setDropDownViewResource(R.layout.item_spinner)
        TipoPastillas.adapter = pastillasAdapter

        val puenteAdapter = ArrayAdapter(this, R.layout.item_spinner, R.id.textoSpinner, puente)
        puenteAdapter.setDropDownViewResource(R.layout.item_spinner)
        Puente.adapter = puenteAdapter

        val maderasAdapter = ArrayAdapter(this, R.layout.item_spinner, R.id.textoSpinner, madera)
        maderasAdapter.setDropDownViewResource(R.layout.item_spinner)
        Maderas.adapter = maderasAdapter

        val cuerdasAdapter = ArrayAdapter(this, R.layout.item_spinner, R.id.textoSpinner, cuerdas)
        cuerdasAdapter.setDropDownViewResource(R.layout.item_spinner)
        Cuerdas.adapter = cuerdasAdapter

        cargarGuitarras()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun cargarGuitarras() {
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
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            },
            { error ->
                error.printStackTrace()
            }
        ) {}
        VolleySingleton.getInstance(this).requestQueue.add(peticion)
    }

    private fun guardar() {
        val telefono = Telefono.text.toString()

        if (Modelo.text.isEmpty() || Marca.text.isEmpty() || Fabricante.text.isEmpty() ||
            Precio.text.isEmpty() || Pais.text.isEmpty() || telefono.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos de la guitarra", Toast.LENGTH_SHORT).show()
            return
        }
        if (telefono.length != 10) {
            Toast.makeText(this, "Número telefónico inválido", Toast.LENGTH_SHORT).show()
            return
        }

        val modelo = Modelo.text.toString()
        val marca = Marca.text.toString()
        val fabricante = Fabricante.text.toString()
        val precio = Precio.text.toString()
        val pais = Pais.text.toString()
        val tipocuerpo = TipoCuerpo.selectedItem.toString()
        val tipopastillas = TipoPastillas.selectedItem.toString()
        val puente = Puente.selectedItem.toString()
        val madera = Maderas.selectedItem.toString()
        val cuerdas = Cuerdas.selectedItem.toString()

        val peticion = object : StringRequest(
            Request.Method.POST,
            Config.URL_AGREGAR,
            { respuesta ->
                try {
                    val json = JSONObject(respuesta)
                    if (json.getBoolean("exito")) {
                        Toast.makeText(this, "La guitarra se ha guardado correctamente", Toast.LENGTH_SHORT).show()
                        Modelo.text.clear()
                        Marca.text.clear()
                        Fabricante.text.clear()
                        Precio.text.clear()
                        Pais.text.clear()
                        Telefono.text.clear()
                        cargarGuitarras()
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
                return hashMapOf(
                    "modelo" to modelo,
                    "marca" to marca,
                    "fabricante" to fabricante,
                    "precio" to precio,
                    "pais" to pais,
                    "telefono" to telefono,
                    "tipocuerpo" to tipocuerpo,
                    "tipopastillas" to tipopastillas,
                    "puente" to puente,
                    "madera" to madera,
                    "cuerdas" to cuerdas
                )
            }
        }

        VolleySingleton.getInstance(this).requestQueue.add(peticion)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        val prefs = getSharedPreferences("sesion", MODE_PRIVATE)
        val rol = prefs.getString("rol", "")
        if (rol == "Trabajador") {
            menu?.findItem(R.id.opc1)?.isVisible = false
            menu?.findItem(R.id.opc3)?.isVisible = false
            menu?.findItem(R.id.iconEliminar)?.isVisible = false
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.opc1){
            Toast.makeText(this, "Ya estas aquí", Toast.LENGTH_SHORT).show()
        }
        if (item.itemId == R.id.opc2) {
            if (guitarra.listaGuitarras.isEmpty()) {
                Toast.makeText(this, "No hay guitarras registradas", Toast.LENGTH_SHORT).show()
            } else {
                startActivity(Intent(this, ver::class.java))
            }
        }
        if (item.itemId == R.id.opc3) {
            if (guitarra.listaGuitarras.isEmpty()) {
                Toast.makeText(this, "No hay guitarras para actualizar", Toast.LENGTH_SHORT).show()
            } else {
                startActivity(Intent(this, actualizar::class.java))
            }
        }
        if (item.itemId == R.id.opc4) {
            startActivity(Intent(this, creador::class.java))
        }
        if (item.itemId == R.id.opc5) {
            startActivity(Intent(this, contacto::class.java))
        }

        if (item.itemId == R.id.iconEliminar) {
            val prefs = getSharedPreferences("sesion", MODE_PRIVATE)
            val rol = prefs.getString("rol", "")
            if (rol == "trabajador") {
                Toast.makeText(this, "No tienes permisos para esta acción", Toast.LENGTH_SHORT).show()
            } else if (guitarra.listaGuitarras.isEmpty()) {
                Toast.makeText(this, "No hay ninguna guitarra para eliminar", Toast.LENGTH_SHORT).show()
            } else {
                startActivity(Intent(this, eliminar::class.java))
            }
        }
        if (item.itemId == R.id.iconCerrarSesion) {
            val prefs = getSharedPreferences("sesion", MODE_PRIVATE)
            prefs.edit().clear().apply()
            startActivity(Intent(this, login::class.java))
            finish()
        }
        return true
    }
}