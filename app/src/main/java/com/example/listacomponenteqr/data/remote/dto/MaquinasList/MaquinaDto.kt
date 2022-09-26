package com.example.listacomponenteqr.data.remote.dto.MaquinasList

import com.example.listacomponenteqr.domain.model.Maquina

data class MaquinaDto (
    val codigo: String?,
    val Refaccion: String?,
    val Status: Boolean,
    val Alert: Boolean,
    val vali: Int?
)

fun MaquinaDto.toMaquina(): Maquina {
    return Maquina(
        codigo = codigo,
        Refaccion = Refaccion,
        Status = Status,
        Alert = Alert,
        vali = vali
    )
}