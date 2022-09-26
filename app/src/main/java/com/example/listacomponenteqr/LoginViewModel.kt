package com.example.listacomponenteqr

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.dataStore
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.listacomponenteqr.common.Constants
import com.example.listacomponenteqr.data.remote.ServiceApi
import com.example.listacomponenteqr.utils.SharedPrefence
import com.example.zitrocrm.screens.login.components.Error
import com.example.zitrocrm.screens.login.components.StringProgres
import com.example.zitrocrm.screens.login.components.progressBar
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/******************/
@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {
    val isSuccessLoading = mutableStateOf(value = false)
    val imageErrorAuth = mutableStateOf(value = false)
    val checkInicio = mutableStateOf(false)
    private val loginRequestLiveData = MutableLiveData<Boolean>()
    val name = mutableStateOf("")

    fun login(usuariox: String, passx: String, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            val dataStorePreferenceRepository = SharedPrefence(context)
            try {
                progressBar.value = true
                val authService = RetrofitHelper.getAuthService()
                val responseService = authService.getLogin(usuariox,passx)
                Log.d("Loggging", "Response: $responseService")
                dataStorePreferenceRepository.clearSharedPreference()
                dataStorePreferenceRepository.saveinicio(checkInicio.value)
                dataStorePreferenceRepository.saveUserInfo(usuariox,passx,responseService.body()!!.n!!.toInt(),responseService.body()!!.nombre.toString())
                name.value = responseService.body()?.n.toString()
                Log.d("Loggging", "ResponseEEEEEEEEEEEEEEE:" + responseService.body()?.nombre.toString())
                if (responseService.isSuccessful) {
                    delay(1500L)
                    isSuccessLoading.value = true
                    imageErrorAuth.value = true
                    responseService.body()?.let { tokenDto ->
                        Log.d("Logging", "Response TokenDto: ${tokenDto}")
                        name.value = "${tokenDto.nombre}"
                    }
                } else {
                    responseService.errorBody()?.let { error ->
                        imageErrorAuth.value = true
                        delay(1500L)
                        imageErrorAuth.value = false
                        error.close()
                    }
                }
                loginRequestLiveData.postValue(responseService.isSuccessful)
                progressBar.value = false
            } catch (e: Exception) {
                if(e.toString()=="com.google.gson.JsonSyntaxException: java.lang.NumberFormatException: empty String"){
                    StringProgres.value = "Verifica usuario y contraseña."
                    Log.d("Logging", "Error Authentication"+ e.toString())
                    progressBar.value = false
                    Error.value = true
                    delay(5000)
                    Error.value = false
                }else{
                    StringProgres.value = "Comprueba tu conexion a internet"
                    progressBar.value = false
                    Error.value = true
                    delay(5000)
                    Error.value = false
                }
            }
        }
    }
}

object RetrofitHelper {

    private val retrofit: Retrofit

    init {
        //val gson = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create()
        val builder = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())

        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .writeTimeout(0, TimeUnit.MILLISECONDS)
            .writeTimeout(2, TimeUnit.MINUTES)
            .connectTimeout(1, TimeUnit.MINUTES).build()
        retrofit = builder.client(okHttpClient).build()
    }

    fun getAuthService() : ServiceApi {
        return retrofit.create(ServiceApi::class.java)
    }
}