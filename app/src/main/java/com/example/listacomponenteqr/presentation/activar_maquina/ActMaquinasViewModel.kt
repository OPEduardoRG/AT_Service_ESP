package com.example.listacomponenteqr.presentation.activar_maquina

import android.content.Context
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.listacomponenteqr.RetrofitHelper
import com.example.listacomponenteqr.common.Resource
import com.example.listacomponenteqr.domain.model.ActivateMaquina
import com.example.listacomponenteqr.domain.use_case.GetActivateMaquinasUseCase
import com.example.listacomponenteqr.utils.SharedPrefence
import com.example.listacomponenteqr.utils.Utils
import com.example.zitrocrm.screens.login.components.Error
import com.example.zitrocrm.screens.login.components.progressBar
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ActMaquinasViewModel @Inject constructor(
    private val getActivateMaquinasUseCase: GetActivateMaquinasUseCase
): ViewModel() {

    private val _state = mutableStateOf(ActMaquinasState())
    val state : State<ActMaquinasState> = _state
    private var _maquinas = MutableLiveData<ActivateMaquina>()
    var actmaquinas : MutableLiveData<ActivateMaquina> = _maquinas
    val textalert = mutableStateOf("")
    val alertstate =  mutableStateOf(false)
    val alertstatecolor =  mutableStateOf(false)
    val modelo =  mutableStateOf("¡Escanea QR o busca la máquina para activarla!")
    val serie =  mutableStateOf(" ")
    val n = mutableStateOf(3)
    val descripion = mutableStateOf("")
    val QRcodemaquina = mutableStateOf("")
    val componetval  = mutableStateOf(false)
    val maquina  = mutableStateOf(" ")
    val component1  = mutableStateOf(" ")
    val component2  = mutableStateOf(" ")

    val alert_manual = mutableStateOf(false)
    val alert_color_manual = mutableStateOf(false)
    val text_manual = mutableStateOf("")
    var serie_maquina = mutableStateOf("")
    var componente1 = mutableStateOf("")
    var componente2 = mutableStateOf("")

    fun getActivateMaquina(n: String, idMaquina: String){
        progressBar.value = true
        getActivateMaquinasUseCase(n,idMaquina).onEach { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = ActMaquinasState(actmaquinas = result.data?: ActivateMaquina(n, ""))
                    actmaquinas.value = result.data
                }
                is Resource.Error -> {
                    _state.value = ActMaquinasState(
                        error = result.message ?: "Ocurrió un error"
                    )
                }
                is Resource.Loading -> {
                    _state.value = ActMaquinasState(isLoading = true)
                }
            }
            progressBar.value = false
        }.launchIn(viewModelScope)
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
    fun getActivateComponents(manual:Boolean,idMaquina: String,codigo1x : String,codigo2x : String,context: Context){
        viewModelScope.launch(Dispatchers.IO) {
            val dataStorePreferenceRepository = SharedPrefence(context)
            val usuid = dataStorePreferenceRepository.getUsuID()
            try {
                var nn = ""
                val qr_val = Utils(context)
                if(qr_val.getValidation(idMaquina) || qr_val.getValidation2(idMaquina)){
                    nn = "0"
                }else{
                    nn = "1"
                }
                progressBar.value =true
                val authService = RetrofitHelper.getAuthService()
                val responseService = authService.getActMaqComp(nn,idMaquina,codigo1x,codigo2x,usuid)
                Log.d("ACTIVATION", "ACTIVATION  " + responseService.n.toString()+ responseService.descripcion.toString())
                if(manual){
                    if (responseService.n==1) {
                        progressBar.value =false
                        alert_Manual_(true,responseService.descripcion.toString())
                        componente1.value = ""
                        componente2.value = ""
                        serie_maquina.value = ""

                    }else{
                        progressBar.value =false
                        alert_Manual_(false,"Alguno de los datos capturados es incorrecto.\nPor favor comprueba tu información.")
                    }
                }else{
                    n.value = responseService.n!!.toInt()
                    descripion.value = responseService.descripcion.toString()
                    if (responseService.n==1) {
                        progressBar.value =false
                        delay(5000L)
                        n.value = 4
                        modelo.value = "¡Escanea QR o busca la máquina para activarla!"
                        serie.value = ""
                    } else {
                        progressBar.value =false
                        delay(5000L)
                        n.value = 3
                    }
                }
            } catch (e: Exception) {
                progressBar.value =false
                Log.d("Logging", "Error Authentication", e)
                Error.value = true
                delay(3000)
                Error.value = false
                n.value = 4
                modelo.value = "¡Escanea QR o busca la máquina para activarla!"
                serie.value = ""
            }
        }
    }

    fun SerieListSeach (serie:String,context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            n.value = 3
            val qr_val = Utils(context)
            try {
                if (qr_val.getValidationSerie(serie)) {
                    getActivateMaquina("1", serie)
                    QRcodemaquina.value = serie
                    alertstate.value = true
                    alertstatecolor.value = true
                    textalert.value = "¡Cargando máquina!\nSerie:" + " " + serie + " "
                } else {
                    alertstate.value = true
                    alertstatecolor.value = false
                    textalert.value = "¡Serie invalida!\nPor favor, ingresa una serie correcta:" + " " + serie
                }
            }
            catch (e: Exception) {
                Log.d("Exception", "", e)
            }
        }
    }
    val delay = mutableStateOf(true)
    fun MaquinaActivateComponentes(barcodeValue: String,context : Context){
        viewModelScope.launch(Dispatchers.IO) {
            val qr_val = Utils(context)
            if(qr_val.getValidation(barcodeValue) || qr_val.getValidation2(barcodeValue))
            {
                val validationmaquina = barcodeValue.split(";")
                maquina.value = validationmaquina[0]
                Log.d("", "MaquinaActivate1:" + maquina.value)
            }
            if (QRcodemaquina.value!=""){
                maquina.value = QRcodemaquina.value
                Log.d("", "MaquinaActivate3:" + maquina.value)
            }
            if(actmaquinas.value?.n=="1"){
                componetval.value=true
                if (componetval.value==true){
                    if (qr_val.getValidationComponent(barcodeValue) || qr_val.getValidationComponent2(barcodeValue)) {
                        if (component1.value==" ") {
                            component1.value = barcodeValue
                            alertstatecolor.value = true
                            alertstate.value = true
                            textalert.value  =  "¡Activación de la máquina! \nEstatus 1/2 \nComponente 1 - Escaneado correctamente"
                            Log.d("", "Component1Activate:" + component1.value)
                            delay.value = false
                            delay(4000L)
                            delay.value = true
                        } else {
                            if(component1.value==barcodeValue){ }
                            else{
                                component2.value = barcodeValue
                                Log.d("", "Component2Activate:" + component2.value)
                                alertstatecolor.value = true
                                alertstate.value = true
                                textalert.value  =  "¡Activación de la máquina! \nEstatus 2/2 \nComponente 2 - Escaneado correctamente.\nCargando el detalle de activación de la máquina."
                                delay(2500L)
                                getActivateComponents(manual = false,maquina.value,component1.value,component2.value,context)
                                componetval.value=false
                                component1.value=" "
                                component2.value=" "
                                QRcodemaquina.value= ""
                                delay.value = false
                                delay(4000L)
                                delay.value = true
                            }
                        }
                    }
                }
            }
        }
    }

    fun MaquinaComponentValidation (barcodeValue:String,context: Context) {
        val qr_val = Utils(context)
        n.value = 3
        if(qr_val.getValidation(barcodeValue) || qr_val.getValidation2(barcodeValue))
        {
            val validationmaquina = barcodeValue.split(";")
            val see: String = validationmaquina[1]
            val mod: String = validationmaquina[2]
            getActivateMaquina("0",validationmaquina[0])
            modelo.value = "Modelo:"+" "+mod
            serie.value = "Serie:"+" "+see

        } else if(qr_val.getValidationComponent(barcodeValue) || qr_val.getValidationComponent2(barcodeValue))
        {
            val actmaquinas = actmaquinas.value
            if(actmaquinas == null)
            {
                alertstatecolor.value = false
                alertstate.value = true
                textalert.value  =  "¡QR Escaneado pertenece a un componente!\nPor favor, escanea primero el QR de la máquina."
            }else if(actmaquinas.n == "0"){
                val yourArray = barcodeValue.substring(0,6)
                Log.d("", "CameraPrevieww: " + yourArray)
                alertstate.value = true
                alertstatecolor.value = false
                textalert.value  = "¡No se puede activar componente, ya que la máquina se encuentra activa!\nPor favor, escanea una máquina que no esté activa."
            }
        } else{
            alertstate.value = true
            alertstatecolor.value = false
            textalert.value  = "¡Código QR invalido!\nCadena:"+" "+barcodeValue
        }
    }
}