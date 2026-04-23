package com.nttdata.ehcos.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nttdata.ehcos.databinding.ItemControlMedicoBinding
import com.nttdata.ehcos.model.ControlMedico

class ControlMedicoAdapter(
    private var lista: MutableList<ControlMedico>,
    private val onItemClick: (ControlMedico) -> Unit
) : RecyclerView.Adapter<ControlMedicoAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemControlMedicoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(control: ControlMedico) {
            binding.apply {
                tvFechaRegistro.text = "Fecha Registro: ${control.fecha}"
                tvHoraRegistro.text  = "Hora Registro: ${control.hora}"
                tvNombrePaciente.text = control.nombres
                tvEdad.text          = control.edad.toString()
                tvAltura.text        = "${control.altura} m"
                tvPresion.text       = control.presionArterial
                tvComentario.text    = control.comentario.ifEmpty { "Sin comentarios" }

                root.setOnClickListener { onItemClick(control) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemControlMedicoBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(lista[position])
    }

    override fun getItemCount() = lista.size

    fun actualizarLista(nuevaLista: List<ControlMedico>) {
        lista.clear()
        lista.addAll(nuevaLista)
        notifyDataSetChanged()
    }
}
