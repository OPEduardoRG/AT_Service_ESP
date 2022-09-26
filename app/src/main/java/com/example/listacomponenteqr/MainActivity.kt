package com.example.listacomponenteqr

import android.hardware.camera2.CameraManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import com.example.listacomponenteqr.navigation.Navigation
import com.example.listacomponenteqr.ui.theme.ListaComponenteQRTheme
import com.example.zitrocrm.screens.login.components.ProgressBarLoading
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@ExperimentalPermissionsApi
class MainActivity : ComponentActivity() {

    private val viewModel: LoginViewModel by viewModels()
    @RequiresApi(Build.VERSION_CODES.M)

    override fun onCreate(savedInstanceState: Bundle?) {
        var cameraM= getSystemService(CAMERA_SERVICE) as CameraManager
        super.onCreate(savedInstanceState)
        setContent {
            ListaComponenteQRTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Box(modifier = Modifier.fillMaxSize()){
                        Navigation(viewModel = viewModel,cameraM = cameraM)
                        ProgressBarLoading()
                    }
                }
            }
        }
    }
}


