package com.nttdata.ehcos.model

data class ControlMedico(
    val id: Long = 0,
    val nombres: String,
    val edad: Int,
    val peso: Double,
    val altura: Double,
    val presionArterial: String,
    val comentario: String,
    val fecha: String,
    val hora: String
) {
    val imc: Double
        get() = if (altura > 0) peso / (altura * altura) else 0.0

    val clasificacionIMC: String
        get() = when {
            imc < 18.5 -> "Bajo peso"
            imc < 25.0 -> "Peso normal"
            imc < 30.0 -> "Sobrepeso"
            else       -> "Obesidad"
        }
}
