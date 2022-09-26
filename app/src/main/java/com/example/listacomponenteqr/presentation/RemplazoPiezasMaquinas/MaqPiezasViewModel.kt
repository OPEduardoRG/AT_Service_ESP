package com.example.listacomponenteqr.presentation.RemplazoPiezasMaquinas

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.listacomponenteqr.RetrofitHelper
import com.example.listacomponenteqr.utils.SharedPrefence
import com.example.listacomponenteqr.utils.Utils
import com.example.zitrocrm.screens.login.components.Error
import com.example.zitrocrm.screens.login.components.Loading
import com.example.zitrocrm.screens.login.components.StringProgres
import com.example.zitrocrm.screens.login.components.progressBar
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MaqPiezasViewModel @Inject constructor(

): ViewModel() {
    val modelo =  mutableStateOf("¡Escanea QR de la máquina para remplazar componentes!")
    val textalert = mutableStateOf("Paso 1 - Escanea QR de la máquina a la que se realizara el remplazo de componentes.")
    val alertstate =  mutableStateOf(true)
    val alertstatecolor =  mutableStateOf(true)
    val serie =  mutableStateOf(" ")
    val n = mutableStateOf(0)
    val maquina  = mutableStateOf("")
    val component1  = mutableStateOf("")
    val component2  = mutableStateOf("")

    var serie_maquina = mutableStateOf("")
    var componente1 = mutableStateOf("")
    var componente2 = mutableStateOf("")

    val alert_manual = mutableStateOf(false)
    val alert_color_manual = mutableStateOf(false)
    val text_manual = mutableStateOf("")

    fun postRemplazoPiezas(manual:Boolean,idMaquina: String, codigo1x : String, codigo2x : String, context: Context){
        viewModelScope.launch(Dispatchers.IO) {
            val dataStorePreferenceRepository = SharedPrefence(context)
            val usuid = dataStorePreferenceRepository.getUsuID()
            try {
                progressBar.value = true
                val authService = RetrofitHelper.getAuthService()
                val responseService =
                    authService.postRemplazoComponete(n.value, idMaquina, codigo1x, codigo2x, usuid)
                if (manual) {
                    if (responseService.n == 1) {
                        alert_Manual_(true,responseService.descripcion.toString())
                    } else {
                        alert_Manual_(false,"Alguno de los datos capturados es incorrecto.\nPor favor comprueba tu información.")
                    }
                } else {
                    if (responseService.n == 1) {
                        textView(responseService.descripcion.toString(), true)
                        clear()
                    } else {
                        textView(responseService.descripcion.toString(), false)
                        clear()
                    }
                    n.value = 0
                    progressBar.value =false
                    delay(5000)
                    textView("Paso 1 - Escanea QR de la máquina a la que se realizara el remplazo de componentes.",true)
                }
                progressBar.value =false
            } catch (e: Exception) {
                Log.d("Logging", "Error Authentication", e)
                StringProgres.value = e.toString()
                progressBar.value =false
                Error.value = true
                delay(5000)
                Error.value = false
                modelo.value = "¡Escanea QR de la máquina para remplazar componentes!"
                serie.value = ""
            }
        }
    }
    fun SerieListSeach (srie:String,context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            val qr_val = Utils(context)
            try {
                if (qr_val.getValidationSerie(srie)) {
                    maquina.value = srie
                    alert()
                    textView("¡Serie de la máquina capturada correctamente! \n\nPaso 2 - Escanea componente a reemplazar",true)
                    component1.value = ""
                    component2.value = ""
                    serie.value = "Serie: ${srie}"
                    modelo.value = "Modelo:"+" "
                    n.value = 1
                } else {
                    textView("¡Serie invalida! \nPor favor, ingresa una serie correcta:" + " " + serie,false)
                }
            }
            catch (e: Exception) {
                Log.d("Exception", "", e)
            }
        }
    }
    fun MaquinaActivateComponentes(barcodeValue: String,context : Context){
        viewModelScope.launch(Dispatchers.IO) {
            val qr_val = Utils(context)
            if(qr_val.getValidation(barcodeValue) || qr_val.getValidation2(barcodeValue))
            {
                if(maquina.value.isBlank()){
                    val validationmaquina = barcodeValue.split(";")
                    maquina.value = validationmaquina[0]
                    alert()
                    textView("¡QR de la máquina escaneada correctamente! \n\nPaso 2 - Escanea componte a remplazar.",true)
                    Log.d("", "MaquinaActivate1:" + maquina.value)
                    val see: String = validationmaquina[1]
                    val mod: String = validationmaquina[2]
                    modelo.value = "Modelo:"+" "+mod
                    serie.value = "Serie:"+" "+see
                    n.value = 0
                }
            }
            if(maquina.value.isNotBlank()) {
                if (qr_val.getValidationComponent(barcodeValue) || qr_val.getValidationComponent2(barcodeValue)) {
                    if (component1.value.isBlank()) {
                        component1.value = barcodeValue
                        alert()
                        textView("¡QR del componente escaneado correctamente! \n\nPaso 3 - Escanea componente nuevo a relacionar con la máquina.",true)
                        Log.d("", "Component1Activate:" + component1.value)
                    } else if (component2.value.isBlank()){
                        if (component1.value==barcodeValue){
                            alert()
                            textView("¡Inválido! \nEscaneaste el mismo componte, Por favor escanea el componente nuevo a relacionar con la máquina. ",false)
                        }else{
                            component2.value = barcodeValue
                            alert()
                            Log.d("", "Component2Activate:" + component2.value)
                            textView("¡Verifica datos capturados! \nEspera un momento.",true)
                            delay(2500)
                            postRemplazoPiezas(
                                false,
                                maquina.value,
                                component1.value,
                                component2.value,
                                context
                            )
                        }
                    }
                }
            }else{
                if (qr_val.getValidationComponent2(barcodeValue) || qr_val.getValidationComponent(barcodeValue)){
                    alert()
                    textView("¡Inválido, QR escaneado pertenece a un componente! \nPor favor, escanea primero el QR de la máquina.",false)
                }
                else{
                    alert()
                    textView("¡Código QR inválido! \nCadena:"+" "+barcodeValue,false)
                }
            }
        }
    }
    fun alert_Manual_ (color:Boolean,text:String){
        viewModelScope.launch(Dispatchers.IO) {
            alert_manual.value = true
            alert_color_manual.value = color
            text_manual.value = text
            delay(4000)
            alert_manual.value = false
        }
    }

    fun alert(){
        viewModelScope.launch(Dispatchers.IO) {
            Loading.value = true
            delay(1000)
            Loading.value = false
        }
    }
    fun clear(){
        component1.value = " "
        maquina.value = ""
        component2.value = ""
        modelo.value =  "¡Escanea QR de la máquina para remplazar componentes!"
        serie.value = ""

        serie_maquina.value = ""
        componente1.value = ""
        componente2.value = ""
    }
    fun textView(string: String, tru:Boolean){
        if (tru) {
            alertstatecolor.value = true
            alertstate.value = true
            textalert.value = string
        }else{
            alertstatecolor.value = false
            alertstate.value = true
            textalert.value = string
        }
    }
}