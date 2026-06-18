package com.example.guitarritas
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.guitarritas.guitarra.listaGuitarras

class GuitarraAdapter(private val lista:List<datos>):
    RecyclerView.Adapter<GuitarraAdapter.ViewHolderClass>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_holder, parent, false)
        return ViewHolderClass(view)
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        val item = listaGuitarras[position]
        holder.modelo.text=item.modelo
        holder.marca.text=item.marca
        holder.modelo.setOnClickListener {
            val context=holder.itemView.context
            val tarj= Intent(context, tarjeta::class.java)
            tarj.putExtra("pos",position)
            context.startActivity(tarj)
        }

    }

    override fun getItemCount(): Int = lista.size
    class ViewHolderClass(view: View):
        RecyclerView.ViewHolder(view){
        val modelo=view.findViewById<TextView>(R.id.txtModelo)
        val marca=view.findViewById<TextView>(R.id.txtMarca)
    }
}
