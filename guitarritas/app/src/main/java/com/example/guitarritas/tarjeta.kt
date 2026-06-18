package com.example.guitarritas

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.Manifest
import android.content.pm.PackageManager

class tarjeta : AppCompatActivity() {
    private val REQUEST_CALL = 1
    lateinit var mod: TextView
    lateinit var marc: TextView
    lateinit var pai: TextView
    lateinit var fab: TextView
    lateinit var prec: TextView
    lateinit var tcuerpo: TextView
    lateinit var tpastillas: TextView
    lateinit var puent: TextView
    lateinit var mad: TextView
    lateinit var cuer: TextView
    lateinit var tel: TextView
    lateinit var llamar: Button
    lateinit var regresar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tarjeta)

        regresar = findViewById<Button>(R.id.btnRegresar)
        mod = findViewById<TextView>(R.id.txtNom)
        marc = findViewById<TextView>(R.id.txtMarc)
        pai = findViewById<TextView>(R.id.txtPais)
        fab = findViewById<TextView>(R.id.txtEmpresa)
        prec = findViewById<TextView>(R.id.txtPrecio)
        tcuerpo = findViewById<TextView>(R.id.txtTipoLeche)
        tpastillas = findViewById<TextView>(R.id.txtSabor)
        puent = findViewById<TextView>(R.id.txtProceso)
        mad = findViewById<TextView>(R.id.txtPresentacion)
        cuer = findViewById<TextView>(R.id.txtPeso)
        tel = findViewById<TextView>(R.id.txtTelefono)
        llamar = findViewById<Button>(R.id.btnLlamar)

        regresar.setOnClickListener { finish() }
        llamar.setOnClickListener { llamar() }

        val posicion = intent.getIntExtra("pos", -1)
        if (posicion != -1 && posicion < guitarra.listaGuitarras.size) {
            val item = guitarra.listaGuitarras[posicion]

            mod.text = item.modelo
            marc.text = item.marca
            fab.text = item.fabricante
            prec.text = item.precio.toString()
            pai.text = item.pais
            tel.text = item.telefono
            tcuerpo.text = item.tipocuerpo // Representa Cuerpo
            tpastillas.text = item.tipopastillas     // Representa Pastillas
            puent.text = item.puente // Representa Puente
            mad.text = item.madera       // Representa Maderas
            cuer.text = item.cuerdas          // Representa Cuerdas
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun llamar(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE)
            != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.CALL_PHONE),
                REQUEST_CALL)
        } else {
            val intent= Intent(Intent.ACTION_CALL)
            intent.data= Uri.parse("tel:"+tel.text.toString())
            startActivity(intent)
        }
    }
}