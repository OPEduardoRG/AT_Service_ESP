package com.example.listacomponenteqr.navigation

import android.hardware.camera2.CameraManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.listacomponenteqr.LoginViewModel
import com.example.listacomponenteqr.presentation.RemplazoPiezasMaquinas.MaquinasPiezasScreen
import com.example.listacomponenteqr.presentation.DescuentoMaterial.DescuentoMaterialScreen
import com.example.listacomponenteqr.presentation.DevolucionMaterial.DevolucionMaterialScreen
import com.example.listacomponenteqr.presentation.InventarioScreen.InventarioScreen
import com.example.listacomponenteqr.presentation.RecibirMaterial.RecibirMaterialScreen
import com.example.listacomponenteqr.presentation.Splash.SplashScreen
import com.example.listacomponenteqr.presentation.activar_maquina.MaquinasActivate
import com.example.listacomponenteqr.presentation.maquina_list.MaquinasListScreen
import com.example.listacomponenteqr.presentation.maquinas_en_sala.MaquinasSalaScreen
import com.example.listacomponenteqr.presentation.solicitud_pedido.SolicitudPedidoScreen
import com.example.listacomponenteqr.presentation.solicitud_pedido.SolicitudViewModel
import com.example.zitrocrm.screens.HomeScreen
import com.example.zitrocrm.screens.LoginScreen

/**
 * Created by Brian Fernando Mtz on 02/2022.
 */

/**NAVIGATION**/
@RequiresApi(Build.VERSION_CODES.M)
@Composable
fun Navigation(
    cameraM: CameraManager,
    viewModel: LoginViewModel,
    viewModelSP: SolicitudViewModel = hiltViewModel(),
) {
    val navController = rememberNavController()
    val imageError = viewModel.imageErrorAuth.value

    NavHost(navController = navController,
        startDestination = Destination.getStartDestination()

    ) {
        composable(route = Destination.SplashScreen.route) {
            SplashScreen(navController)
        }
        composable(route = Destination.LoginScreen.route) {
            if (viewModel.isSuccessLoading.value) {
                LaunchedEffect(key1 = Unit) {
                    navController.navigate(route = Destination.HomeScreen.route) {
                        popUpTo(route = Destination.LoginScreen.route) {
                            inclusive = true
                        }
                    }
                }
            } else {
                LoginScreen(
                    onclickLogin = viewModel::login,
                    imageError = imageError,
                    loginViewModel = viewModel
                )
            }
        }

        composable(route = Destination.HomeScreen.route) {
            HomeScreen(navController = navController)
        }
        composable(route = Destination.MaquinasActivate.route) {
            MaquinasActivate(cameraM = cameraM, navController = navController)
        }
        composable(route = Destination.MaquinasListScreen.route) {
            MaquinasListScreen(cameraM = cameraM, navController = navController)
        }
        composable(route = Destination.MaquinasPiezasScreen.route) {
            MaquinasPiezasScreen(navController = navController)
        }
        composable(route = Destination.SolicitudPedidoScreen.route){
            SolicitudPedidoScreen(navController = navController)
        }
        composable(route = Destination.DevolucionMaterialScreen.route){
            DevolucionMaterialScreen(navController = navController)
        }
        composable(route= Destination.DescuentoMaterialScreen.route){
            DescuentoMaterialScreen(navController = navController)
        }
        composable(route= Destination.InventarioScreen.route){
            InventarioScreen(navController=navController)
        }
        composable(route = Destination.RecibirMaterialScreen.route){
            RecibirMaterialScreen(navController=navController)
        }
        composable(route = Destination.MaquinaSalaScreen.route){
            MaquinasSalaScreen(navController = navController)
        }
    }
}