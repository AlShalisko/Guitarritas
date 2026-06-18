package com.example.guitarritas

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ver : AppCompatActivity() {
    lateinit var recy: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ver)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        recy = findViewById<RecyclerView>(R.id.rv)
        recy.layoutManager = LinearLayoutManager(this)
        val adapter = GuitarraAdapter(guitarra.listaGuitarras)
        recy.adapter = adapter
        adapter.notifyDataSetChanged()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        val prefs = getSharedPreferences("sesion", MODE_PRIVATE)
        val rol = prefs.getString("rol", "")

        if (rol== "Trabajador") {
            menu?.findItem(R.id.opc1)?.isVisible = false
            menu?.findItem(R.id.opc3)?.isVisible = false
            menu?.findItem(R.id.iconEliminar)?.isVisible = false
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val prefs = getSharedPreferences("sesion", MODE_PRIVATE)
        val rol = prefs.getString("rol", "")

        if (item.itemId == R.id.opc1) {
            if (rol == "Trabajador") {
                Toast.makeText(this, "No tienes permisos para esta acción", Toast.LENGTH_SHORT).show()
            } else {
                startActivity(Intent(this, MainActivity::class.java))
            }
        }
        if (item.itemId == R.id.opc2) {
            Toast.makeText(this, "Ya estás aquí", Toast.LENGTH_SHORT).show()
        }
        if (item.itemId == R.id.opc3) {
            if (rol == "Trabajador") {
                Toast.makeText(this, "No tienes permisos para esta acción", Toast.LENGTH_SHORT).show()
            } else if (guitarra.listaGuitarras.isEmpty()) {
                Toast.makeText(this, "No hay ninguna guitarra registrada", Toast.LENGTH_SHORT).show()
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
            if (rol == "Trabajador") {
                Toast.makeText(this, "No tienes permisos para esta acción", Toast.LENGTH_SHORT).show()
            } else if (guitarra.listaGuitarras.isEmpty()) {
                Toast.makeText(this, "No hay ninguna guitarra registrada", Toast.LENGTH_SHORT).show()
            } else {
                startActivity(Intent(this, eliminar::class.java))
            }
        }

        if (item.itemId == R.id.iconCerrarSesion) {
            val prefs = getSharedPreferences("sesion", MODE_PRIVATE)
            prefs.edit().clear().apply()
            val intent = Intent(this, login::class.java)
            startActivity(intent)
        }
        return true
    }
}