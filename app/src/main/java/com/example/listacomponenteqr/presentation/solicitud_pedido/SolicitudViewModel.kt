package com.example.listacomponenteqr.presentation.solicitud_pedido

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.listacomponenteqr.RetrofitHelper
import com.example.listacomponenteqr.data.remote.dto.SolicitudRefaccion.*
import com.example.listacomponenteqr.utils.SharedPrefence
import com.example.listacomponenteqr.utils.Utils
import com.example.zitrocrm.screens.login.components.Error
import com.example.zitrocrm.screens.login.components.StringProgres
import com.example.zitrocrm.screens.login.components.progressBar
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Brian Fernando Mtz on 07-2022.
 */
@HiltViewModel
class SolicitudViewModel @Inject constructor(

): ViewModel() {
    var codigo  = mutableStateListOf<Refacciones>()
    var `QR-Manual` =  mutableStateOf(false)
    val textalert = mutableStateOf("")
    val alertstate =  mutableStateOf(false)
    val alertstatecolor =  mutableStateOf(true)
    var salasxRegion  = mutableStateListOf<Salas>()
    val delate = mutableStateOf(false)
    val listSolicitud =  mutableStateListOf<Solicitud>()
    var subcentros = listOf<Subcentros>()

    fun getSubCentros(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if(subcentros.isEmpty()){
                    progressBar.value = true
                    val authService = RetrofitHelper.getAuthService()
                    val responseService = authService.getSubcentros()
                    if(responseService.subcentros.isNotEmpty()){
                        subcentros = responseService.subcentros
                    }
                    progressBar.value = false
                }
            }catch (e:Exception){
                progressBar.value = false
                eRror(e.toString())
                Log.d("getCodigos", "Error getCodigos", e)
            }
        }
    }

    fun getCodSolicitud(string:String, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                codigo.clear()
                progressBar.value =true
                val authService = RetrofitHelper.getAuthService()
                val responseService = authService.getCodigosSolicitud()
                val vali = Utils(context)
                if(vali.getValidationRefaccion(string) || vali.getValidationRefaccion2(string)) {
                    codigo.clear()
                    codigo += responseService.refacciones.filter { it.codigo!!.uppercase().contains(string.uppercase()) /*|| it.codigo!!.contains(string.toLowerCase()) */}.asReversed()//.take(5)
                }
                if(vali.getValidationName(string)){
                    codigo += responseService.refacciones.filter { it.nombre!!.uppercase().contains(string.uppercase()) /*|| it.nombre!!.lowercase().contains(string.toLowerCase()) || it.nombre!!.contains(string)*/}.asReversed() //.take(5)
                }
                progressBar.value =false
            } catch (e: Exception) {
                progressBar.value =false
                eRror(e.toString())
                Log.d("getCodigos", "Error getCodigos", e)
            }
        }
    }

    fun SolicitarPorQr(maquina:String,codigo:String,almacen:String, fechahora:String, context: Context){
        viewModelScope.launch(Dispatchers.IO) {
            progressBar.value =true
            val dataStorePreferenceRepository = SharedPrefence(context)
            val usuid = dataStorePreferenceRepository.getUsuID()
            try {
                val authService = RetrofitHelper.getAuthService()
                val responseService = authService.solicitarPorQr(
                    Solicitud_QR(
                        usuarioid = usuid.toString(),
                        maquina = maquina,
                        codigo = codigo,
                        almacenId = almacen,
                        fechaHora = fechahora
                    )
                )
                if (responseService.isSuccessful){
                    if(responseService.body()!!.respuesta=="1"){
                        alert("Se envió correctamente la solicitud de material  \nN.° pedido: ${responseService.body()!!.solicitud}",true)
                        delate.value = true
                    }else if(responseService.body()!!.respuesta=="0"){
                        Log.d("postPedido", "else postPedido"+responseService.body()!!.toString())
                        alert("${responseService.body()!!.descripcion}",false)
                    }
                }
                progressBar.value = false
            }catch (e:Exception){
                progressBar.value = false
                eRror(e.toString())
                Log.d("SolicitarPorQr", "Error SolicitarPorQr", e)
            }
        }
    }

    fun postPedido(solicitud: ArrayList<Solicitud>, salaid:String, almacen:String, fechahora:String,context: Context){
        viewModelScope.launch(Dispatchers.IO) {
            progressBar.value =true
            val dataStorePreferenceRepository = SharedPrefence(context)
            val usuid = dataStorePreferenceRepository.getUsuID()
            try {
                val authService = RetrofitHelper.getAuthService()
                val responseService = authService.postCrearPedido(
                    PedidoMaterial(
                        usuarioid = usuid.toString(),
                        solicitud = solicitud,
                        salaid = salaid,
                        almacenId = almacen,
                        fechaHora = fechahora
                    )
                )
                if(responseService.isSuccessful){
                    if(responseService.body()!!.respuesta=="1"){
                        alert("Se envió correctamente el pedido de material  \nN.° pedido: ${responseService.body()!!.solicitud}",true)
                        delate.value = true
                    }
                    Log.d("postPedido", "Success postPedido"+responseService.body()!!.toString())
                }else{
                    Log.d("postPedido", "else postPedido"+responseService.body()!!.toString())
                    alert("Ocurrió un error",false)
                }
                progressBar.value = false
            }catch(e:Exception){
                eRror(e.toString())
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
                eRror(e.toString())
            }
        }
    }
    fun alert(string:String,color:Boolean){
        viewModelScope.launch(Dispatchers.IO) {
            textalert.value = string
            alertstate.value = true
            alertstatecolor.value = color
            delay(3000)
            alertstate.value = false
        }
    }
    fun eRror(string: String){
        viewModelScope.launch(Dispatchers.IO) {
            StringProgres.value = string
            Error.value = true
            delay(3000)
            Error.value = false
            progressBar.value = false
        }
    }
}