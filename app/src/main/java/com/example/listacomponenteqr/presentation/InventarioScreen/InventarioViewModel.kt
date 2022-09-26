package com.example.listacomponenteqr.presentation.InventarioScreen

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.listacomponenteqr.RetrofitHelper
import com.example.listacomponenteqr.data.remote.dto.InventarioMov.Refacciones
import com.example.listacomponenteqr.data.remote.dto.SolicitudRefaccion.DevolucionMaterial
import com.example.listacomponenteqr.data.remote.dto.SolicitudRefaccion.Solicitud
import com.example.listacomponenteqr.utils.SharedPrefence
import com.example.zitrocrm.screens.login.components.StringProgres
import com.example.zitrocrm.screens.login.components.progressBar
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InventarioViewModel @Inject constructor(
): ViewModel(){
    var listInventario = listOf<Refacciones>()
    var listInventarioDevolucion = mutableStateListOf<Refacciones>()
    var listPiezasInventario = mutableStateListOf<Refacciones>()
    val textalert = mutableStateOf("")
    val alertstate =  mutableStateOf(0)
    val alertstatecolor =  mutableStateOf(true)
    val refaccionid = mutableStateOf("")
    val subcentro = mutableStateOf("")
    val cantidads = mutableStateOf("")
    val a = mutableStateOf(true)
    val delate = mutableStateOf(false)

    fun getInvetarioMovil(estatus:String, context: Context){
        viewModelScope.launch(Dispatchers.IO) {
             try {
                 listInventarioDevolucion.clear()
                 progressBar.value =true
                 val dataStorePreferenceRepository = SharedPrefence(context)
                 val usuid = dataStorePreferenceRepository.getUsuID()
                 val authService = RetrofitHelper.getAuthService()
                 val responseService = authService.getInventariio(
                     usuarioIDx = usuid.toString(),
                     estatusx = estatus
                 )
                 if(responseService.isSuccessful){
                     listInventario = responseService.body()!!.refacciones
                     listInventarioDevolucion += responseService.body()!!.refacciones
                     a.value=false
                     a.value=true
                 }
                 progressBar.value = false
             }catch (e:Exception){
                 progressBar.value = false
                 alert("No tienes inventario.",false)
             }
        }
    }
    fun postDescuetoInventario(idMaquina:String,codigo:String,cantidad:String,n:String,context: Context){
        viewModelScope.launch(Dispatchers.IO) {
            try{
                cantidads.value = cantidad
                progressBar.value = true
                val dataStorePreferenceRepository = SharedPrefence(context)
                val usuid = dataStorePreferenceRepository.getUsuID()
                val authService = RetrofitHelper.getAuthService()
                val responseService = authService.postDecuentoMtVan(
                    idmaquina = idMaquina,
                    codigo = codigo,
                    cantidad = cantidad,
                    n = n,
                    usuarioid = usuid.toString()
                )
                if(responseService.isSuccessful){
                    Log.d("postDescuetoInventario","RESPONSE:  "+responseService.body().toString())
                    responseService.body()!!.let {
                        if(it.respuesta==1){
                            StringProgres.value = it.descripcion.toString()
                            refaccionid.value = it.refaccionid.toString()
                            subcentro.value = it.subcentro.toString()
                            alertstate.value = 2
                            a.value=false
                            a.value=true
                        }else{
                            alert(it.descripcion.toString(),false)
                        }
                    }
                }
                progressBar.value = false
            }catch (e:Exception){
                alert(e.toString(),false)
                progressBar.value = false
            }
        }
    }
    fun postRevisionVan(context: Context){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                progressBar.value = true
                val dataStorePreferenceRepository = SharedPrefence(context)
                val usuid = dataStorePreferenceRepository.getUsuID()
                val authService = RetrofitHelper.getAuthService()
                val responseService = authService.agregaVanRevision(
                    subcentro=subcentro.value,
                    cantidad = cantidads.value,
                    refaccionid = refaccionid.value,
                    usuarioid = usuid.toString()
                )
                if(responseService.isSuccessful){
                    responseService.body().let {
                        if(it!!.respuesta==1){
                            alert(it.descripcion.toString(),true)
                            getInvetarioMovil("1",context)
                            cantidads.value = ""
                        }else{
                            alert(it.descripcion.toString(),false)
                        }
                    }
                    a.value=false
                    a.value=true
                }
                progressBar.value = false
            }catch (e:Exception){
                alert(e.toString(),false)
                progressBar.value = false
                Log.d("postRevisionVan", "postRevisionVan Exception",e)
            }
        }
    }

    fun postDevolucion(solicitud: ArrayList<Solicitud>, context: Context){
        viewModelScope.launch(Dispatchers.IO) {
            progressBar.value =true
            val dataStorePreferenceRepository = SharedPrefence(context)
            val usuid = dataStorePreferenceRepository.getUsuID()
            try {
                val authService = RetrofitHelper.getAuthService()
                val responseService = authService.postDevolucionPedido(
                    DevolucionMaterial(
                        usuarioid = usuid.toString(),
                        solicitud = solicitud,
                    )
                )
                if(responseService.isSuccessful){
                    if(responseService.body()!!.respuesta=="1"){
                        alert("Se envió correctamente la devolución de material  \nN° devolución: ${responseService.body()!!.devolucion}",true)
                        listPiezasInventario.clear()
                        listInventarioDevolucion.clear()
                        progressBar.value = false
                    }
                    Log.d("postPedido", "Success postPedido"+responseService.body()!!.toString())
                }else{
                    Log.d("postPedido", "else postPedido"+responseService.body()!!.toString())
                    alert("Ocurrió un error al devolver el material",false)
                }
                delay(1000)
                progressBar.value = false
            }catch(e:Exception){
                progressBar.value = false
                //error("")
                alert(e.toString(),false)
                Log.d("postPedido", "Error postPedido", e)
            }
        }
    }

    private fun alert(string:String,color:Boolean){
        viewModelScope.launch(Dispatchers.IO) {
            textalert.value = string
            alertstate.value = 1
            alertstatecolor.value = color
            delay(3000)
            alertstate.value = 0
        }
    }
}