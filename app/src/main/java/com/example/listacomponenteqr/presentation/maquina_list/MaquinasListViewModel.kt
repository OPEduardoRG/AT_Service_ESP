package com.example.listacomponenteqr.presentation.maquina_list

import android.content.Context
import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.listacomponenteqr.common.Resource
import com.example.listacomponenteqr.domain.model.Maquina
import com.example.listacomponenteqr.domain.use_case.GetMaquinasUseCase
import com.example.listacomponenteqr.utils.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MaquinasListViewModel @Inject constructor(
    private val getMaquinasUseCase: GetMaquinasUseCase
): ViewModel() {

    /**VIEW MODEL QR MAQUINAS LIST**/
    private val _state = mutableStateOf(MaquinasListState())
    val state: State<MaquinasListState> = _state
    private var _maquinas = MutableLiveData<List<Maquina>>()
    var maquinas: MutableLiveData<List<Maquina>> = _maquinas
    val textalert = mutableStateOf("")
    val alertstate = mutableStateOf(false)
    val alertstatevalidation = mutableStateOf(false)
    val alertstatecolor = mutableStateOf(false)
    val modelo = mutableStateOf("¡Escanea QR o busca la máquina por serie!")
    val serie = mutableStateOf(" ")
    var maquinasi =  mutableStateOf(" ")
    val act = mutableStateOf(true)

    fun getMaquinas(n: String, idMaquina: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                getMaquinasUseCase(n, idMaquina).onEach { result ->
                    when (result) {
                        is Resource.Success -> {
                            _state.value = MaquinasListState(maquinas = result.data ?: emptyList())
                            maquinas.value = result.data
                            Log.d("", "STATEMAQUINAS: " + _state.value.maquinas)
                            Log.d("", "MAQUINASVALUE: " + result.data)
                        }
                        is Resource.Error -> {
                            _state.value = MaquinasListState(
                                error = result.message ?: "Ocurrió un error"
                            )
                        }
                        is Resource.Loading -> {
                            _state.value = MaquinasListState(isLoading = true)
                        }
                    }
                }.launchIn(viewModelScope)
            } catch (e: Exception) {
                alertstate.value = true
                alertstatecolor.value = false
                textalert.value = "¡No hay resultados!"
                delay(2000L)
                alertstate.value = false
            }
        }
    }
    fun checkStatusComponent(modelo: String) {
        viewModelScope.launch(Dispatchers.IO) {
            maquinas.value?.find { it.codigo == modelo }?.Status = true
            maquinasi.value = maquinas.value?.find { it.codigo == modelo }.toString()
            Log.d("", "MAQUINAALER: " + maquinasi.value)
            if (maquinasi.value=="null"){
                alertstate.value = true
                alertstatecolor.value = false
                textalert.value = "¡El componente no pertenece a la máquina! \nComponente:" +" "+ modelo
                delay(2000L)
                alertstate.value = false
                maquinasi.value = " "
            }else{
                alertstatevalidation.value = true
                alertstate.value = true
                alertstatecolor.value = true
                textalert.value  = "¡El componente si pertenece a la máquina! \nComponente:" +" "+ modelo
                delay(2000L)
                alertstate.value = false
                maquinasi.value = " "
            }
        }
        act.value = false
        act.value = true
    }

    fun SerieListSeach(serie: String,context:Context) {
        viewModelScope.launch(Dispatchers.IO) {
            val qr_val = Utils(context)
            try {
                if (qr_val.getValidationSerie(serie)) {
                    alertstate.value = true
                    alertstatecolor.value = true
                    textalert.value = "¡Cargando componentes de la máquina!\nSerie:" + " " + serie + " "
                    delay(1000L)
                    getMaquinas("1", serie)
                    alertstate.value = false
                    textalert.value = ""

                } else {
                    alertstate.value = true
                    alertstatecolor.value = false
                    textalert.value = "¡Serie invalida!\nPor favor, ingresa una serie correcta:" + " " + serie
                    delay(2000L)
                    alertstate.value = false
                    textalert.value = ""

                }
            } catch (e: Exception) {
                alertstate.value = true
                alertstatecolor.value = false
                textalert.value = "¡Ocurrió un error, inténtalo de nuevo!"
                delay(2000L)
                alertstate.value = false
            }
        }
    }
    fun MaquinaComponentValidation(barcodeValue: String,context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            val qr_val = Utils(context)
            try {
                if (qr_val.getValidation(barcodeValue) || qr_val.getValidation2(barcodeValue)) {
                    val validationmaquina = barcodeValue.split(";")
                    getMaquinas("0", validationmaquina[0])
                    modelo.value = "Modelo:" + " " + validationmaquina[2]
                    serie.value = "Serie:" + " " + validationmaquina[1]
                }
                else if (qr_val.getValidationComponent2(barcodeValue) || qr_val.getValidationComponent(barcodeValue)) {
                    if (maquinas.value.isNullOrEmpty()) {
                        alertstatecolor.value = false
                        alertstate.value = true
                        textalert.value = "¡El QR pertenece a un componente! \nPor favor, escanea primero el QR de la máquina."
                        delay(2000L)
                        alertstate.value = false
                    } else {
                        val validationmaquina = barcodeValue.split(" ").toTypedArray()
                        checkStatusComponent(validationmaquina[0])
                    }
                } else{
                    alertstate.value = true
                    alertstatecolor.value = false
                    textalert.value = "¡Código QR inválido! \nCadena:" + " " + barcodeValue
                    delay(2000L)
                    alertstate.value = false
                }
            } catch (e: Exception) {
                alertstate.value = true
                alertstatecolor.value = false
                textalert.value = "¡Ocurrió un error, inténtalo de nuevo!"
                delay(2000L)
                alertstate.value = false
            }
        }
    }
}