package com.example.listacomponenteqr.domain.repository

import com.example.listacomponenteqr.data.remote.dto.MaquinaActivate.ActivateMaquinaDto

interface ActMaquinaRepository {
    suspend fun getActivateMaquina (n: String,idMaquina : String): ActivateMaquinaDto
}