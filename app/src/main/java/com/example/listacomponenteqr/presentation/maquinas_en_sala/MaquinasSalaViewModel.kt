package com.example.listacomponenteqr.presentation.maquinas_en_sala

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.listacomponenteqr.RetrofitHelper
import com.example.listacomponenteqr.data.remote.dto.MaquinasSala.MaquinasSala
import com.example.listacomponenteqr.data.remote.dto.SolicitudRefaccion.RegionesEsp
import com.example.listacomponenteqr.data.remote.dto.SolicitudRefaccion.Salas
import com.example.zitrocrm.screens.login.components.Error
import com.example.zitrocrm.screens.login.components.progressBar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class MaquinasSalaViewModel @Inject constructor(
): ViewModel(){
//    ----------------------------------------------------------------------------------------------
    val maquinasSala = mutableStateListOf<MaquinasSala>()
    var salasxRegion  = mutableStateListOf<Salas>()
    val textalert = mutableStateOf("")
    val alertstate =  mutableStateOf(false)
    val alertstatecolor =  mutableStateOf(true)

//    ----------------------------------------------------------------------------------------------
val regionesEspana = listOf<RegionesEsp>(
        RegionesEsp("30", "Alava"),
        RegionesEsp("36", "Albacete"),
        RegionesEsp("7", "Alicante"),
        RegionesEsp("5", "Almeria"),
        RegionesEsp("37", "Asturias"),
        RegionesEsp("12", "Avila"),
        RegionesEsp("13", "Badajoz"),
        RegionesEsp("31", "Baleares"),
        RegionesEsp("40", "Barcelona"),
        RegionesEsp("1", "Burgos"),
        RegionesEsp("17", "Caceres"),
        RegionesEsp("19", "Cadiz"),
        RegionesEsp("34", "Cantabria"),
        RegionesEsp("51", "Castellon"),
        RegionesEsp("52", "Ceuta"),
        RegionesEsp("46", "Ciudad Real"),
        RegionesEsp("15", "Cordoba"),
        RegionesEsp("44", "Cuenca"),
        RegionesEsp("50", "Gerona"),
        RegionesEsp("18", "Granada"),
        RegionesEsp("28", "Guadalajara"),
        RegionesEsp("32", "Guipuzcoa"),
        RegionesEsp("33", "Huelva"),
        RegionesEsp("27", "Huesca"),
        RegionesEsp("14", "Jaen"),
        RegionesEsp("38", "La Coruña"),
        RegionesEsp("21", "Las Palmas"),
        RegionesEsp("43", "Leon"),
        RegionesEsp("41", "Lerida"),
        RegionesEsp("35", "Logroño"),
        RegionesEsp("53", "Lugo"),
        RegionesEsp("6", "Madrid"),
        RegionesEsp("2", "Malaga"),
        RegionesEsp("42", "Melilla"),
        RegionesEsp("3", "Murcia"),
        RegionesEsp("23", "Navarra"),
        RegionesEsp("29", "no-region-0004"),
        RegionesEsp("49", "Orense"),
        RegionesEsp("4", "Palencia"),
        RegionesEsp("39", "Pontevedra"),
        RegionesEsp("16", "Salamanca"),
        RegionesEsp("24", "Santa Cruz Tenerife"),
        RegionesEsp("22", "Segovia"),
        RegionesEsp("20", "Sevilla"),
        RegionesEsp("45", "Soria"),
        RegionesEsp("8", "Tarragona"),
        RegionesEsp("26", "Teruel"),
        RegionesEsp("48", "Toledo"),
        RegionesEsp("11", "Valencia"),
        RegionesEsp("9", "Valladolid"),
        RegionesEsp("10", "Vizcaya"),
        RegionesEsp("47", "Zamora"),
        RegionesEsp("25", "Zaragoza"),
    )
    var getListSimilar = mutableStateListOf<RegionesEsp>()

    fun getValidarSimilarLista(context: Context, simRegion: String){
        getListSimilar.clear()
        if(simRegion.count() >= 2){
            getListSimilar += regionesEspana.filter {
                it.nombre!!.uppercase().contains(simRegion.uppercase())
                } .asReversed()
        }
    }

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