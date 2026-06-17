package com.example.chijeu

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

class actualizar : AppCompatActivity() {
    lateinit var Modelo: EditText
    lateinit var Marca: EditText
    lateinit var Fabricante: EditText
    lateinit var Precio: EditText
    lateinit var Pais: EditText
    lateinit var Telefono: EditText
    lateinit var TipoCuerpo: Spinner
    lateinit var TipoPastillas: Spinner
    lateinit var Puente: Spinner
    lateinit var Madera: Spinner
    lateinit var Cuerdas: Spinner
    lateinit var BtnAnterior: Button
    lateinit var BtnActualizar: Button
    lateinit var BtnSiguiente: Button

    var posicionActual = 0

    val tipocuerpo = arrayOf("Sólido", "Hueco", "Semi-hueco")
    val tipopastillas = arrayOf("Single Coil", "Humbucker", "P90", "Activas")
    val puente = arrayOf("Fijo", "Trémolo", "Floyd Rose")
    val madera = arrayOf("Caoba", "Arce", "Palosanto", "Aliso", "Fresno", "Ebano", "Nogal")
    val cuerdas = arrayOf("6 cuerdas", "7 cuerdas", "8 cuerdas", "12 cuerdas", "4 cuerdas", "5 cuerdas")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_actualizar)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        Modelo = findViewById(R.id.etNombreG)
        Marca = findViewById(R.id.etMarcaG)
        Fabricante = findViewById(R.id.etEmpresaG)
        Precio = findViewById(R.id.etPrecioG)
        Pais = findViewById(R.id.etPaisG)
        Telefono = findViewById(R.id.etTelefonoG)
        TipoCuerpo = findViewById(R.id.spTipoCuerpo)
        TipoPastillas = findViewById(R.id.spTipoPastillas)
        Puente = findViewById(R.id.spPuente)
        Madera = findViewById(R.id.spMadera)
        Cuerdas = findViewById(R.id.spCuerdas)
        BtnAnterior = findViewById(R.id.btnQuesoAnt)
        BtnActualizar = findViewById(R.id.btnActualizarQueso)
        BtnSiguiente = findViewById(R.id.btnQuesoSig)

        val tipoCuerpoAdapter = ArrayAdapter(this, R.layout.item_spinner, R.id.textoSpinner, tipocuerpo)
        tipoCuerpoAdapter.setDropDownViewResource(R.layout.item_spinner)
        TipoCuerpo.adapter = tipoCuerpoAdapter

        val tipoPastillasAdapter = ArrayAdapter(this, R.layout.item_spinner, R.id.textoSpinner, tipopastillas)
        tipoPastillasAdapter.setDropDownViewResource(R.layout.item_spinner)
        TipoPastillas.adapter = tipoPastillasAdapter

        val puenteAdapter = ArrayAdapter(this, R.layout.item_spinner, R.id.textoSpinner, puente)
        puenteAdapter.setDropDownViewResource(R.layout.item_spinner)
        Puente.adapter = puenteAdapter

        val maderaAdapter = ArrayAdapter(this, R.layout.item_spinner, R.id.textoSpinner, madera)
        maderaAdapter.setDropDownViewResource(R.layout.item_spinner)
        Madera.adapter = maderaAdapter

        val cuerdasAdapter = ArrayAdapter(this, R.layout.item_spinner, R.id.textoSpinner, cuerdas)
        cuerdasAdapter.setDropDownViewResource(R.layout.item_spinner)
        Cuerdas.adapter = cuerdasAdapter

        if (guitarra.listaGuitarras.isNotEmpty()) {
            cargarGuitarraEnUI(posicionActual)
        } else {
            cargarGuitarrasServidor()
        }

        BtnActualizar.setOnClickListener { actualizarGuitarra() }

        BtnAnterior.setOnClickListener {
            if (guitarra.listaGuitarras.isEmpty()) return@setOnClickListener
            posicionActual = if (posicionActual == 0) guitarra.listaGuitarras.size - 1 else posicionActual - 1
            cargarGuitarraEnUI(posicionActual)
        }

        BtnSiguiente.setOnClickListener {
            if (guitarra.listaGuitarras.isEmpty()) return@setOnClickListener
            posicionActual = if (posicionActual == guitarra.listaGuitarras.size - 1) 0 else posicionActual + 1
            cargarGuitarraEnUI(posicionActual)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun cargarGuitarraEnUI(pos: Int) {
        val item = guitarra.listaGuitarras[pos]
        Modelo.setText(item.modelo)
        Marca.setText(item.marca)
        Fabricante.setText(item.fabricante)
        Precio.setText(item.precio.toString())
        Pais.setText(item.pais)
        Telefono.setText(item.telefono)
        TipoCuerpo.setSelection(tipocuerpo.indexOf(item.tipocuerpo))
        TipoPastillas.setSelection(tipopastillas.indexOf(item.tipopastillas))
        Puente.setSelection(puente.indexOf(item.puente))
        Madera.setSelection(madera.indexOf(item.madera))
        Cuerdas.setSelection(cuerdas.indexOf(item.cuerdas))
    }

    private fun cargarGuitarrasServidor() {
        val peticion = object : StringRequest(Request.Method.POST, Config.URL_LISTAR, { respuesta ->
            try {
                val json = JSONObject(respuesta)
                if (json.getBoolean("exito")) {
                    guitarra.listaGuitarras.clear()
                    val arreglo = json.getJSONArray("guitarras")
                    for (i in 0 until arreglo.length()) {
                        val obj = arreglo.getJSONObject(i)
                        guitarra.listaGuitarras.add(datos(
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
                        ))
                    }
                    if (guitarra.listaGuitarras.isNotEmpty()) cargarGuitarraEnUI(0)
                }
            } catch (e: Exception) { e.printStackTrace() }
        }, { }) {}
        VolleySingleton.getInstance(this).requestQueue.add(peticion)
    }

    private fun actualizarGuitarra() {
        if (guitarra.listaGuitarras.isEmpty()) return
        val id = guitarra.listaGuitarras[posicionActual].id
        val modelo = Modelo.text.toString()
        val marca = Marca.text.toString()
        val fabricante = Fabricante.text.toString()
        val precio = Precio.text.toString()
        val pais = Pais.text.toString()
        val telefono = Telefono.text.toString()
        val tipocuerpoStr = TipoCuerpo.selectedItem.toString()
        val tipopastillasStr = TipoPastillas.selectedItem.toString()
        val puenteStr = Puente.selectedItem.toString()
        val maderaStr = Madera.selectedItem.toString()
        val cuerdasStr = Cuerdas.selectedItem.toString()

        val peticion = object : StringRequest(Request.Method.POST, Config.URL_EDITAR, { respuesta ->
            try {
                val json = JSONObject(respuesta)
                if (json.getBoolean("exito")) {
                    Toast.makeText(this, "Actualizado correctamente", Toast.LENGTH_SHORT).show()
                    cargarGuitarrasServidor() // Sincronizar
                } else {
                    Toast.makeText(this, json.getString("mensaje"), Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) { e.printStackTrace() }
        }, { }) {
            override fun getParams(): MutableMap<String, String> = hashMapOf(
                "id" to id.toString(), "modelo" to modelo, "marca" to marca, "fabricante" to fabricante,
                "precio" to precio, "pais" to pais, "telefono" to telefono, "tipocuerpo" to tipocuerpoStr,
                "tipopastillas" to tipopastillasStr, "puente" to puenteStr, "madera" to maderaStr, "cuerdas" to cuerdasStr
            )
        }
        VolleySingleton.getInstance(this).requestQueue.add(peticion)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.opc1 -> startActivity(Intent(this, MainActivity::class.java))
            R.id.opc2 -> {
                if (guitarra.listaGuitarras.isEmpty()) Toast.makeText(this, "No hay guitarras", Toast.LENGTH_SHORT).show()
                else startActivity(Intent(this, ver::class.java))
            }
            R.id.opc4 -> startActivity(Intent(this, creador::class.java))
            R.id.opc5 -> startActivity(Intent(this, contacto::class.java))
            R.id.iconEliminar -> startActivity(Intent(this, eliminar::class.java))
            R.id.iconCerrarSesion -> {
                getSharedPreferences("sesion", MODE_PRIVATE).edit().clear().apply()
                startActivity(Intent(this, login::class.java))
                finish()
            }
        }
        return true
    }
}