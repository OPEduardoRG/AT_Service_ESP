package com.example.listacomponenteqr.presentation.DevolucionMaterial

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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
/**
 * Created by Brian Fernando Mtz on 07-2022.
 */
class DevolucionViewModel @Inject constructor(
): ViewModel() {
    var codigo  = mutableStateListOf<Refacciones>()
    val textalert = mutableStateOf("")
    val alertstate =  mutableStateOf(0)
    val alertstatecolor =  mutableStateOf(true)
    val delate = mutableStateOf(false)
    val description = mutableStateOf("")
    val response = mutableStateOf("")
    val granel = mutableStateOf("")
    val listDevolucion = mutableStateListOf<Solicitud>()

    fun getCS(string:String,context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                codigo.clear()
                progressBar.value = true
                val authService = RetrofitHelper.getAuthService()
                val responseService = authService.getCodigosSolicitud()
                val vali = Utils(context)
                if(vali.getValidationRefaccion(string) || vali.getValidationRefaccion2(string)) {
                    codigo += responseService.refacciones.filter { it.codigo!!.uppercase().contains(string.uppercase())}.asReversed()//.take(5)
                }
                if(vali.getValidationName(string)){
                    codigo += responseService.refacciones.filter { it.nombre!!.uppercase().contains(string.uppercase())}.asReversed() //.take(5)
                }
                progressBar.value =false
            } catch (e: Exception) {
                error(e.toString())
                Log.d("getCodigos", "Error getCodigos", e)
            }
        }
    }
    fun solicitarNuevoMaterialdeDevolucion(solicitud: ArrayList<Solicitud>, context: Context){
        viewModelScope.launch(Dispatchers.IO) {
            val dataStorePreferenceRepository = SharedPrefence(context)
            val usuid = dataStorePreferenceRepository.getUsuID()
            val sala = dataStorePreferenceRepository.getSala()
            progressBar.value =true
            try {
                val authService = RetrofitHelper.getAuthService()
                val responseService = authService.solPedidoDevolucion(
                    DevSolicitudMaterial(
                        usuarioid = usuid.toString(),
                        solicitud = solicitud,
                        sala = sala
                    )
                )
                if(responseService.isSuccessful){
                    if(responseService.body()!!.respuesta=="1"){
                        alert("Se envió correctamente la solicitud de material  \nN° solicitud: ${responseService.body()!!.solicitud}",true)
                        delate.value = true
                    }else{
                        alert("Ocurrió un error intenta de nuevo",false)
                    }
                }
                progressBar.value =false
            }catch (e:Exception){
                progressBar.value =false
                error(e.toString())
                Log.d("solicitarNuevoMaterialdeDevolucion", "Error solicitarNuevoMaterialdeDevolucion", e)
            }
        }
    }
    fun postDevolucion(/*estatus:String,*/solicitud: ArrayList<Solicitud>,comentarios:String, context: Context){
        viewModelScope.launch(Dispatchers.IO) {
            progressBar.value =true
            val dataStorePreferenceRepository = SharedPrefence(context)
            val usuid = dataStorePreferenceRepository.getUsuID()
            try {
                val authService = RetrofitHelper.getAuthService()
                val responseService = authService.postDevolucionPedido(
                    DevolucionMaterial(
                        usuarioid = usuid.toString(),
                        //solicitud = solicitud,
                        //estatus = estatus,
                        comentarios = comentarios,
                        solicitud = solicitud
                        )
                    )
                if(responseService.isSuccessful){
                    if(responseService.body()!!.respuesta=="1"){
                        progressBar.value = false
                        alertstate.value = 2
                        StringProgres.value = "Se envió correctamente la devolución de material  \nN° devolución: ${responseService.body()!!.devolucion}"
                        //alert("Se envio correctamente la devolucion de material  \nN° devolucion: ${responseService.body()!!.devolucion}",true)
                    }
                    Log.d("postPedido", "Success postPedido"+responseService.body()!!.toString())
                }else {
                    Log.d("postPedido", "else postPedido" + responseService.body()!!.toString())
                    alert("Ocurrió un error con la devolucion de material", false)
                }
                delay(1000)
                progressBar.value = false
            }catch(e:Exception){
                error(e.toString())
                Log.d("postPedido", "Error postPedido", e)
            }
        }
    }
    fun getvalidationQrMaterial(codigo:String,context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                progressBar.value = true
                val dataStorePreferenceRepository = SharedPrefence(context)
                val usuid = dataStorePreferenceRepository.getUsuID()
                val authService = RetrofitHelper.getAuthService()
                val responseService = authService.getValidationQrMaterial(
                    ValidaQR(
                        usuarioid = usuid.toString(),
                        codigo = codigo
                    )
                )
                if (responseService.isSuccessful) {
                    if (responseService.body()!!.respuesta.toString() == "1") {
                        alert("Código correcto", true)
                        description.value = responseService.body()!!.descripcion.toString()
                        granel.value = responseService.body()!!.granel.toString()
                        response.value = responseService.body()!!.respuesta.toString()
                    } else if (responseService.body()!!.respuesta.toString() == "0") {
                        alert("Código no válido para devolución.", false)
                    }
                    Log.d("getvalidationQrMaterial", "Sucess getvalidationQrMaterial" + responseService.body()!!.toString())
                }
                progressBar.value = false
            } catch (e: Exception) {
                Log.d("getvalidationQrMaterial", "Catch getvalidationQrMaterial" + e)
                progressBar.value = false
                error(e.toString())
            }
        }
    }
    fun alert(string:String,color:Boolean){
        viewModelScope.launch(Dispatchers.IO) {
            textalert.value = string
            alertstate.value = 1
            alertstatecolor.value = color
            delay(3000)
            alertstate.value = 0
        }
    }
    fun error(string: String){
        viewModelScope.launch(Dispatchers.IO) {
            StringProgres.value = string
            Error.value = true
            delay(3000)
            Error.value = false
            progressBar.value = false
        }
    }
}