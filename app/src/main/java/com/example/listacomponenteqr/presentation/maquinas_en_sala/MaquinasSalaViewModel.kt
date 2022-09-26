package com.example.listacomponenteqr.presentation.maquinas_en_sala

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.listacomponenteqr.RetrofitHelper
import com.example.listacomponenteqr.data.remote.dto.MaquinasSala.MaquinasSala
import com.example.listacomponenteqr.data.remote.dto.SolicitudRefaccion.Salas
import com.example.zitrocrm.screens.login.components.Error
import com.example.zitrocrm.screens.login.components.progressBar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class MaquinasSalaViewModel @Inject constructor(
): ViewModel(){
    val maquinasSala = mutableStateListOf<MaquinasSala>()
    var salasxRegion  = mutableStateListOf<Salas>()
    val textalert = mutableStateOf("")
    val alertstate =  mutableStateOf(false)
    val alertstatecolor =  mutableStateOf(true)
    fun getMaquinasSala(id:Int){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                progressBar.value = true
                maquinasSala.clear()
                val authService = RetrofitHelper.getAuthService()
                val responseService = authService.getMaquinasSala(
                    id = id
                )
                if (responseService.isNotEmpty()){
                    maquinasSala += responseService
                }
                progressBar.value = false
            }catch (e:Exception){
                progressBar.value = false
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