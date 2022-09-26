package com.example.listacomponenteqr.domain.use_case

import com.example.listacomponenteqr.common.Resource
import com.example.listacomponenteqr.data.remote.dto.MaquinasList.toMaquina
import com.example.listacomponenteqr.domain.model.Maquina
import com.example.listacomponenteqr.domain.repository.MaquinaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetMaquinasUseCase @Inject constructor(
    private val repository: MaquinaRepository
) {
    operator fun invoke (n: String,idMaquina: String): Flow<Resource<List<Maquina>>> = flow {
        try {
            emit(Resource.Loading<List<Maquina>>())
            val maquina = repository.getMaquina(n,idMaquina).map { it.toMaquina() }
            emit(Resource.Success<List<Maquina>>(maquina))
        } catch (e:HttpException){
            emit(Resource.Error<List<Maquina>>(e.localizedMessage ?: "Ocurrió un error, no se obtuvo nada"))
        } catch (e: IOException){
            emit(Resource.Error<List<Maquina>>( "No se pudo contactar con el servidor. Comprueba tu conexión a Internet."))
        }
    }
}