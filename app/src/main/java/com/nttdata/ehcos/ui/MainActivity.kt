package com.nttdata.ehcos.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.nttdata.ehcos.adapter.ControlMedicoAdapter
import com.nttdata.ehcos.data.DatabaseHelper
import com.nttdata.ehcos.databinding.ActivityMainBinding
import com.nttdata.ehcos.model.ControlMedico

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var db: DatabaseHelper
    private lateinit var adapter: ControlMedicoAdapter

    companion object {
        const val EXTRA_ID = "extra_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = DatabaseHelper(this)

        setupRecyclerView()
        setupListeners()
    }

    override fun onResume() {
        super.onResume()
        cargarRegistros()
    }

    private fun setupRecyclerView() {
        adapter = ControlMedicoAdapter(mutableListOf()) { control ->
            val intent = Intent(this, DetalleActivity::class.java)
            intent.putExtra(EXTRA_ID, control.id)
            startActivity(intent)
        }
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = this@MainActivity.adapter
        }
    }

    private fun setupListeners() {
        binding.btnAgregar.setOnClickListener {
            startActivity(Intent(this, RegistroActivity::class.java))
        }
    }
    private fun cargarRegistros() {
        val lista = db.obtenerTodos()
        adapter.actualizarLista(lista)

        if (lista.isEmpty()) {
            binding.tvEmptyState.visibility = View.VISIBLE
            binding.recyclerView.visibility = View.GONE
        } else {
            binding.tvEmptyState.visibility = View.GONE
            binding.recyclerView.visibility = View.VISIBLE
        }
    }
}
