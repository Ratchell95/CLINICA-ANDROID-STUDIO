package com.nttdata.ehcos.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.nttdata.ehcos.data.DatabaseHelper
import com.nttdata.ehcos.databinding.ActivityRegistroBinding
import com.nttdata.ehcos.model.ControlMedico
import java.text.SimpleDateFormat
import java.util.*

class RegistroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistroBinding
    private lateinit var db: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.includeToolbar.toolbarPrincipal)
        supportActionBar?.apply {
            title = "Nuevo Registro"
            setDisplayHomeAsUpEnabled(true)
        }

        db = DatabaseHelper(this)

        binding.btnRegistrar.setOnClickListener {
            registrar()
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
    private fun registrar() {
        val nombres  = binding.etNombres.text.toString().trim()
        val edadStr  = binding.etEdad.text.toString().trim()
        val pesoStr  = binding.etPeso.text.toString().trim()
        val altStr   = binding.etAltura.text.toString().trim()
        val presion  = binding.etPresion.text.toString().trim()
        val comentario = binding.etComentario.text.toString().trim()

        // Validaciones
        if (nombres.isEmpty()) { binding.etNombres.error = "Campo requerido"; return }
        if (edadStr.isEmpty()) { binding.etEdad.error = "Campo requerido"; return }
        if (pesoStr.isEmpty()) { binding.etPeso.error = "Campo requerido"; return }
        if (altStr.isEmpty())  { binding.etAltura.error = "Campo requerido"; return }
        if (presion.isEmpty()) { binding.etPresion.error = "Campo requerido"; return }

        val edad   = edadStr.toIntOrNull()
        val peso   = pesoStr.toDoubleOrNull()
        val altura = altStr.toDoubleOrNull()

        if (edad == null || edad <= 0)   { binding.etEdad.error = "Edad inválida"; return }
        if (peso == null || peso <= 0)   { binding.etPeso.error = "Peso inválido"; return }
        if (altura == null || altura <= 0) { binding.etAltura.error = "Altura inválida"; return }

        // Fecha y hora del sistema
        val ahora  = Calendar.getInstance()
        val fecha  = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(ahora.time)
        val hora   = SimpleDateFormat("HH:mm", Locale.getDefault()).format(ahora.time)

        val control = ControlMedico(
            nombres          = nombres,
            edad             = edad,
            peso             = peso,
            altura           = altura,
            presionArterial  = presion,
            comentario       = comentario,
            fecha            = fecha,
            hora             = hora
        )

        val id = db.insertarControl(control)

        if (id > 0) {
            mostrarAlertaExito()
        } else {
            Toast.makeText(this, "Error al guardar el registro", Toast.LENGTH_SHORT).show()
        }
    }

    private fun mostrarAlertaExito() {
        AlertDialog.Builder(this)
            .setTitle("Registro correcto")
            .setMessage("Su registro de control médico se realizó correctamente")
            .setPositiveButton("Entiendo") { dialog, _ ->
                dialog.dismiss()
                limpiarFormulario()
                finish()
            }
            .setCancelable(false)
            .show()
    }
    private fun limpiarFormulario() {
        binding.etNombres.text?.clear()
        binding.etEdad.text?.clear()
        binding.etPeso.text?.clear()
        binding.etAltura.text?.clear()
        binding.etPresion.text?.clear()
        binding.etComentario.text?.clear()
    }
}
