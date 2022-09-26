package com.example.listacomponenteqr.presentation.RecibirMaterial

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.listacomponenteqr.RetrofitHelper
import com.example.listacomponenteqr.data.remote.dto.Prueba
import com.example.listacomponenteqr.data.remote.dto.RecibirMat.RecibirMat
import com.example.listacomponenteqr.data.remote.dto.RecibirMat.RecibirMat1
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
 * Created by Brian Fernando Mtz on 08-2022.
 */
class RecibirMaterialViewModel @Inject constructor(
): ViewModel() {
    var codigo  = mutableStateListOf<Refacciones>()
    val textalert = mutableStateOf("")
    val alertstate =  mutableStateOf(0)
    val alertstatecolor =  mutableStateOf(true)
    val delate = mutableStateOf(false)
    val description = mutableStateOf("")
    val response = mutableStateOf("")
    val granel = mutableStateOf("")
    val listRecibirMat =  mutableStateListOf<RecibirMat>()

    val pruebalista = mutableStateListOf<Prueba>()

    /*fun getPrueba()/*: SnapshotStateList<Prueba>*/ {
        //var pruebalista = mutableStateListOf<Prueba>()
        viewModelScope.launch(Dispatchers.IO) {
            try {

                val authService = RetrofitHelper.getAuthService()
                val responseService = authService.apiprueba()
                if(responseService.isSuccessful){
                    if(pruebalista.isEmpty()){
                        pruebalista += responseService.body()!!
                    }
                }
            }catch (e:Exception){
                error(e)
            }
        }
        /*return pruebalista*/
    }*/

    fun getCS(string:String,context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                codigo.clear()
                progressBar.value =true
                val authService = RetrofitHelper.getAuthService()
                val responseService = authService.getCodigosSolicitud()
                val vali = Utils(context)
                if(vali.getValidationRefaccion(string) || vali.getValidationRefaccion2(string)) {
                    codigo += responseService.refacciones.filter { it.codigo!!.uppercase().contains(string.toUpperCase()) /*|| it.codigo!!.contains(string.toLowerCase()) */}.asReversed()//.take(5)
                }
                if(vali.getValidationName(string)){
                    codigo += responseService.refacciones.filter { it.nombre!!.uppercase().contains(string.toUpperCase()) /*|| it.nombre!!.lowercase().contains(string.toLowerCase()) || it.nombre!!.contains(string)*/}.asReversed() //.take(5)
                }
                /*if(vali.getValidationRefaccion(string) || vali.getValidationRefaccion2(string)) {
                    codigo += responseService.refacciones.filter { it.codigo!!.contains(string.toUpperCase()) || it.codigo!!.contains(string.toLowerCase()) }.asReversed()
                }
                if(vali.getValidationName(string)){
                    codigo += responseService.refacciones.filter { it.nombre!!.uppercase().contains(string.toUpperCase()) || it.nombre!!.lowercase().contains(string.toLowerCase()) || it.nombre!!.contains(string)}.asReversed()
                }*/
                progressBar.value =false
            } catch (e: Exception) {
                error(e.toString())
                Log.d("getCodigos", "Error getCodigos", e)
            }
        }
    }

    fun postDevolucion(material: ArrayList<RecibirMat>, context: Context){
        viewModelScope.launch(Dispatchers.IO) {
            progressBar.value =true
            val dataStorePreferenceRepository = SharedPrefence(context)
            val usuid = dataStorePreferenceRepository.getUsuID()
            val listMateial  = mutableStateListOf<RecibirMat1>()
            try {
                material.forEach {
                    listMateial.add(
                        RecibirMat1(
                            it.codigo,
                            it.cantidad,
                            it.estatus
                        )
                    )
                }
                val authService = RetrofitHelper.getAuthService()
                val responseService = authService.recibirMaterial(
                    PostRecibirMaterial(
                        usuarioid = usuid.toString(),
                        material = ArrayList(listMateial)
                    )
                )
                if(responseService.isSuccessful){
                    if(responseService.body()!!.respuesta?.toInt()==1){
                        progressBar.value = false
                        alert("${responseService.body()!!.ingreso}",true)
                        delate.value = true
                    }else if (responseService.body()!!.respuesta?.toInt()==0){
                        alert("No se pudo realizar el ingreso de material.  \n\n${responseService.body()!!.ingreso}",false)
                    }
                }else{
                    alert("Ocurrió un error con el ingreso de material",false)
                }
                delay(1000)
                progressBar.value = false
            }catch(e:Exception){
                error(e.toString())
                Log.d("postPedido", "Error postPedido", e)
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