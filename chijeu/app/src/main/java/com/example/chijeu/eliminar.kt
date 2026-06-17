package com.example.chijeu

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import org.json.JSONObject

class eliminar : AppCompatActivity() {
    lateinit var recy: RecyclerView
    lateinit var btnEliminar: Button
    lateinit var adapter: EliminarAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_eliminar)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        recy = findViewById(R.id.rvEliminar)
        btnEliminar = findViewById(R.id.btnEliminar)

        recy.layoutManager = LinearLayoutManager(this)
        adapter = EliminarAdapter(guitarra.listaGuitarras)
        recy.adapter = adapter

        btnEliminar.setOnClickListener {
            val indices = guitarra.listaGuitarras.indices
                .filter { adapter.seleccionados[it] }
                .sortedDescending()

            if (indices.isEmpty()) {
                Toast.makeText(this, "Selecciona al menos una guitarra", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            var pendientes = indices.size

            for (i in indices) {
                val id = guitarra.listaGuitarras[i].id
                val peticion = object : StringRequest(
                    Request.Method.POST,
                    Config.URL_ELIMINAR,
                    { respuesta ->
                        try {
                            val json = JSONObject(respuesta)
                            if (!json.getBoolean("exito")) {
                                Toast.makeText(this, json.getString("mensaje"), Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: Exception) { e.printStackTrace() }
                        
                        pendientes--
                        if (pendientes == 0) {
                            Toast.makeText(this, "Operación finalizada", Toast.LENGTH_SHORT).show()
                            cargarGuitarras() // Refrescamos todo al terminar
                        }
                    },
                    { error ->
                        pendientes--
                        if (pendientes == 0) cargarGuitarras()
                    }
                ) {
                    override fun getParams(): MutableMap<String, String> = hashMapOf("id" to id.toString())
                }
                VolleySingleton.getInstance(this).requestQueue.add(peticion)
            }
        }

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
                        adapter.notifyDataSetChanged()
                        // Reiniciar selección
                        adapter.seleccionados.clear()
                        adapter.seleccionados.addAll(List(guitarra.listaGuitarras.size) { false })
                    }
                } catch (e: Exception) { e.printStackTrace() }
            },
            { }
        ) {}
        VolleySingleton.getInstance(this).requestQueue.add(peticion)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.opc1 -> startActivity(Intent(this, MainActivity::class.java))
            R.id.opc2 -> startActivity(Intent(this, ver::class.java))
            R.id.opc3 -> startActivity(Intent(this, actualizar::class.java))
            R.id.opc4 -> startActivity(Intent(this, creador::class.java))
            R.id.opc5 -> startActivity(Intent(this, contacto::class.java))
            R.id.iconCerrarSesion -> {
                getSharedPreferences("sesion", MODE_PRIVATE).edit().clear().apply()
                startActivity(Intent(this, login::class.java))
                finish()
            }
        }
        return true
    }
}