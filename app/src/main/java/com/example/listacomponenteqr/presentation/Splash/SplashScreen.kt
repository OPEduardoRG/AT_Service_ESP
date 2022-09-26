package com.example.listacomponenteqr.presentation.Splash

import android.Manifest
import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.example.listacomponenteqr.R
import com.example.listacomponenteqr.navigation.Destination
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.delay


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SplashScreen(navController: NavController) {
    Image(
        painterResource(id = R.drawable.backgroud_crmm),
        contentDescription = null,
        modifier = Modifier.fillMaxWidth(),
        contentScale = ContentScale.Crop
    )
    val cameraPermissionState =
        rememberPermissionState(
            permission = Manifest.permission.CAMERA)

    val scale = remember {
        Animatable(0.9f)
    }
    /**AnimationEffect**/
    LaunchedEffect(key1 = true) {
        scale.animateTo(
            targetValue = 0.7f,
            animationSpec = tween(
                durationMillis = 1200,
                easing = {
                    OvershootInterpolator(4f).getInterpolation(it)
                })
        )
        delay(3000L)
        /**PERMISSIONS-CAMERA-NAVIGATION**/
        cameraPermissionState.launchPermissionRequest()
        navController.navigate(route = Destination.LoginScreen.route ){
            popUpTo(route = Destination.SplashScreen.route) {
                inclusive = true
            }
        }

    }
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(R.drawable.splash_qr_1),
            contentDescription = "Logo",
            modifier = Modifier.scale(scale.value)
        )
    }
}