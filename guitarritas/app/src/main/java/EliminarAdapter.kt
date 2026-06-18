package com.example.guitarritas
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.guitarritas.R

class EliminarAdapter (private val lista:MutableList<datos>):
    RecyclerView.Adapter<EliminarAdapter.ViewHolderClass>() {
        val seleccionados = MutableList(lista.size){false}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_holder_eliminar, parent, false)
        return ViewHolderClass(view)
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        val item = lista[position]
        holder.modelo.text = "Modelo: ${item.modelo}"
        holder.marca.text = "Marca: ${item.marca}"
        holder.puente.text = "Puente: ${item.puente}"
        holder.fabricante.text = "Fabricante: ${item.fabricante}"

        holder.checkBox.setOnCheckedChangeListener(null)
        holder.checkBox.isChecked = seleccionados[position]

        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            seleccionados[position] = isChecked
        }
    }

    override fun getItemCount(): Int = lista.size

    class ViewHolderClass(view: View) : RecyclerView.ViewHolder(view) {
        val modelo     = view.findViewById<TextView>(R.id.tvModeloE)
        val marca      = view.findViewById<TextView>(R.id.tvMarcaE)
        val puente     = view.findViewById<TextView>(R.id.tvPuenteE)
        val fabricante = view.findViewById<TextView>(R.id.tvFabricanteE)
        val checkBox   = view.findViewById<CheckBox>(R.id.checkboxEliminar)
    }
}
