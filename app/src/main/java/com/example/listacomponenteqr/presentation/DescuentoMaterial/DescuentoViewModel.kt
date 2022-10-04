package com.example.listacomponenteqr.presentation.DescuentoMaterial

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.listacomponenteqr.RetrofitHelper
import com.example.listacomponenteqr.data.remote.dto.MaquinasSala.MaquinasSala
import com.example.listacomponenteqr.data.remote.dto.SolicitudRefaccion.*
import com.example.listacomponenteqr.presentation.maquinas_en_sala.MaquinasSalaViewModel
import com.example.listacomponenteqr.utils.SharedPrefence
import com.example.listacomponenteqr.utils.Utils
import com.example.zitrocrm.screens.login.components.Error
import com.example.zitrocrm.screens.login.components.progressBar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
/**
 * Created by Brian Fernando Mtz on 07-2022.
 */
class DescuentoViewModel  @Inject constructor(
): ViewModel() {
    var codigo  = mutableStateListOf<Refacciones>()
    val textalert = mutableStateOf("")
    val alertstate =  mutableStateOf(false)
    val alertstatecolor =  mutableStateOf(true)
    val delate = mutableStateOf(false)
    var salasxRegion  = mutableStateListOf<Salas>()
    val listDescuento =  mutableStateListOf<Solicitud>()
    val description = mutableStateOf("")
    val response = mutableStateOf("")
    val granel = mutableStateOf("")
    var getListSimilar = mutableStateListOf<RegionesEsp>()
    val maquinasSala = mutableStateListOf<MaquinasSala>()
    var getListSimilarSalas = mutableStateListOf<Salas>()


    fun getMaterial(string:String, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                codigo.clear()
                progressBar.value =true
                val authService = RetrofitHelper.getAuthService()
                val responseService = authService.getCodigosSolicitud()
                val vali = Utils(context)
                if(vali.getValidationRefaccion(string) || vali.getValidationRefaccion2(string)) {
                    codigo += responseService.refacciones.filter { it.codigo!!.uppercase().contains(string.uppercase()) }.asReversed()
                }
                if(vali.getValidationName(string)){
                    codigo += responseService.refacciones.filter { it.nombre!!.uppercase().contains(string.uppercase()) }.asReversed()
                }
                progressBar.value =false
            } catch (e: Exception) {
                eror()
                Log.d("getCodigos", "Error getCodigos", e)
            }
        }
    }

    fun getValidarSimilarRegion(context: Context, simRegion: String){
        getListSimilar.clear()
        if(simRegion.count() >= 2){
            getListSimilar += MaquinasSalaViewModel().regionesEspana.filter {
                it.nombre!!.uppercase().contains(simRegion.uppercase())
            } .asReversed()
        }
    }

    fun getValidarsimilarSalas(sala: String){
        getListSimilarSalas.clear()
        if(sala.count() >= 2){
            getListSimilarSalas += salasxRegion.filter {
                it.nombre!!.uppercase().contains(sala.uppercase())
            } .asReversed()
        }
    }

    fun postDescuentoMaterial(descuento: ArrayList<Solicitud>, context: Context, serie:String, observaciones:String, sala:String){
        viewModelScope.launch(Dispatchers.IO) {
            progressBar.value =true
            val dataStorePreferenceRepository = SharedPrefence(context)
            val usuid = dataStorePreferenceRepository.getUsuID()
            try {
                val authService = RetrofitHelper.getAuthService()
                val responseService = authService.postDescuentoMat(
                    DescuentoMaterial(
                        usuarioid = usuid.toString(),
                        descuento = descuento,
                        seriex = serie,
                        observacionesx = observaciones,
                        salaid = sala
                    )
                )
                if(responseService.isSuccessful){
                    if(responseService.body()!!.respuesta=="1"){
                        alert("Se envio correctamente el descuento de material  \n: ${responseService.body()!!.descripcion.toString()}",true)
                        delate.value = true
                    }else if(responseService.body()!!.respuesta=="0"){
                        val text = mutableStateOf("")
                        responseService.body()!!.descripcion.forEach { tt->
                            text.value = "${tt}\n${text.value}"
                        }
                        textalert.value = "Verifica los materiales:\n${text.value}"
                        alertstate.value = true
                        alertstatecolor.value = false
                        delay(6000)
                        alertstate.value = false
                        delate.value = true
                    }
                    Log.d("postPedido", "Success postPedido"+responseService.body()!!.toString())
                }else{
                    Log.d("postPedido", "else postPedido"+responseService.body()!!.toString())
                    alert("Ocurrió un error",false)
                }
                delay(1000)
                progressBar.value = false
            }catch(e:Exception){
                eror()
                Log.d("postPedido", "Error postPedido", e)
            }
        }
    }

    fun getSalas(regionid:String){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                salasxRegion.clear()
                progressBar.value = true
                val authService = RetrofitHelper.getAuthService()
                val responseService = authService.getSalasRegion(
                    regionidx = regionid
                )
                if(responseService.isSuccessful){
                    salasxRegion += responseService.body()!!.salas
                }else{
                    alert("No hay resultados",false)
                }
                if (responseService.body().toString()=="[]"){
                    alert("No hay resultados",false)
                }
                progressBar.value =false
            }catch (e:Exception){
                Log.d("getSalas", "Error getSalas", e)
                eror()
            }
        }
    }

    private fun alert(string:String,color:Boolean){
        viewModelScope.launch(Dispatchers.IO) {
            textalert.value = string
            alertstate.value = true
            alertstatecolor.value = color
            delay(3000)
            alertstate.value = false
        }
    }
    private fun eror(){
        viewModelScope.launch(Dispatchers.IO) {
            Error.value = true
            delay(3000)
            Error.value = false
            progressBar.value = false
        }
    }
}