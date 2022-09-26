package com.example.listacomponenteqr.domain.use_case

import com.example.listacomponenteqr.common.Resource
import com.example.listacomponenteqr.data.remote.dto.MaquinaActivate.toActMaquina
import com.example.listacomponenteqr.domain.model.ActivateMaquina
import com.example.listacomponenteqr.domain.repository.ActMaquinaRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class GetActivateMaquinasUseCase @Inject constructor (
    private val repository: ActMaquinaRepository
    ) {
    operator fun invoke(n : String, idMaquina:String): Flow<Resource<ActivateMaquina?>> = flow{
        try {
            emit(Resource.Loading<ActivateMaquina?>())
            val actmaquina = repository.getActivateMaquina(n, idMaquina).run { toActMaquina() }
            emit(Resource.Success<ActivateMaquina?>(actmaquina))
        } catch (e: HttpException){
            emit(Resource.Error<ActivateMaquina?>(e.localizedMessage ?: "Ocurrió un error, no se obtuvo nada"))
        } catch (e: IOException){
            emit(Resource.Error<ActivateMaquina?>( "No se pudo contactar con el servidor. Comprueba tu conexión a Internet."))
        }
    }
}