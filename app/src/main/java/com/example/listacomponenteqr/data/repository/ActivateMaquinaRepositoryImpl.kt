package com.example.listacomponenteqr.data.repository

import com.example.listacomponenteqr.data.remote.ServiceApi
import com.example.listacomponenteqr.data.remote.dto.MaquinaActivate.ActivateMaquinaDto
import com.example.listacomponenteqr.domain.repository.ActMaquinaRepository
import javax.inject.Inject

class ActivateMaquinaRepositoryImpl @Inject constructor(
    private val api: ServiceApi
): ActMaquinaRepository {
    override suspend fun getActivateMaquina(n: String, idMaquina: String): ActivateMaquinaDto{
        return api.getActivate(n,idMaquina)
    }
}