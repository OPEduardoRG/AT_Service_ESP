package com.example.listacomponenteqr.data.remote.dto.MaquinaActivate

import com.example.listacomponenteqr.domain.model.ActivateMaquina

data class ActivateMaquinaDto(
    val n:String?,
    val descripcion:String?
)

fun ActivateMaquinaDto.toActMaquina(): ActivateMaquina {
    return ActivateMaquina(
        n = n,
        descripcion = descripcion
    )
}