package com.example.listacomponenteqr.domain.repository

import com.example.listacomponenteqr.data.remote.dto.MaquinasList.MaquinaDto

interface MaquinaRepository {
    suspend fun getMaquina (n: String,idMaquina : String): List<MaquinaDto>
}