package com.nttdata.ehcos.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.nttdata.ehcos.data.DatabaseHelper
import com.nttdata.ehcos.databinding.ActivityDetalleBinding
import com.nttdata.ehcos.model.ControlMedico
import java.util.Locale

class DetalleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetalleBinding
    private lateinit var db: DatabaseHelper
    private var controlId: Long = -1
    private var control: ControlMedico? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetalleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.includeToolbar.toolbarPrincipal)
        supportActionBar?.apply {
            title = "Control Médico"
            setDisplayHomeAsUpEnabled(true)
        }

        db = DatabaseHelper(this)
        controlId = intent.getLongExtra(MainActivity.EXTRA_ID, -1L)

        if (controlId == -1L) {
            Toast.makeText(this, "Registro no encontrado", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        cargarDetalle()

        binding.btnEliminar.setOnClickListener {
            mostrarConfirmacionEliminar()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    private fun cargarDetalle() {
        control = db.obtenerPorId(controlId)
        control?.let { c ->
            binding.apply {

                tvFecha.text = "Fecha: ${c.fecha}"
                tvHora.text  = "Hora: ${c.hora}"
                tvNombre.setText(c.nombres)
                tvEdad.setText(c.edad.toString())
                tvAltura.setText("${c.altura} m")
                tvPresion.setText(c.presionArterial)
                tvComentario.setText(c.comentario.ifEmpty { "Sin comentarios" })

                tvImcClasificacion.text = c.clasificacionIMC
            }
        } ?: run {
            Toast.makeText(this, "Registro no encontrado", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
    private fun mostrarConfirmacionEliminar() {
        AlertDialog.Builder(this)
            .setTitle("Eliminar Registro")
            .setMessage("Los datos se eliminarán permanentemente")
            .setNegativeButton("Cancelar") { dialog, _ -> dialog.dismiss() }
            .setPositiveButton("Eliminar") { _, _ ->
                val eliminado = db.eliminarControl(controlId)
                if (eliminado) {
                    Toast.makeText(this, "Registro eliminado", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
            .show()
    }
}