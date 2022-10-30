package com.example.proyectandroid

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class TareaAdapter(
    private val tareas: MutableList<DataTarea>
) : RecyclerView.Adapter<TareaAdapter.TareasViewHolder>() {
    class TareasViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TareasViewHolder {
        return TareasViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.lista_tareas,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: TareasViewHolder, position: Int) {
        val curTarea = tareas[position]
        holder.itemView.
    }

    override fun getItemCount(): Int {
        return tareas.size
    }
}