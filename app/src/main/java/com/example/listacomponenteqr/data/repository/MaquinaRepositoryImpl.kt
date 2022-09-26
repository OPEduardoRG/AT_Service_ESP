package com.example.listacomponenteqr.data.repository

import com.example.listacomponenteqr.data.remote.ServiceApi
import com.example.listacomponenteqr.data.remote.dto.MaquinasList.MaquinaDto
import com.example.listacomponenteqr.domain.repository.MaquinaRepository
import javax.inject.Inject

class MaquinaRepositoryImpl @Inject constructor(
    private val api: ServiceApi
): MaquinaRepository {
    override suspend fun getMaquina(n: String,idMaquina : String): List<MaquinaDto> {
        return api.getlistmaquina(n, idMaquina)
    }
}