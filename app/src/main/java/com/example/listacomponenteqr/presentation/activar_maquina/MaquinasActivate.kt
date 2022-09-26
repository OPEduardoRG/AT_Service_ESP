package com.example.listacomponenteqr.presentation.activar_maquina

import android.hardware.camera2.CameraManager
import android.media.MediaPlayer
import android.util.Log
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.listacomponenteqr.R
import com.example.listacomponenteqr.ui.theme.reds
import com.example.listacomponenteqr.utils.BarcodeAnalyser
import com.google.common.util.concurrent.ListenableFuture
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterStart
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.example.listacomponenteqr.domain.model.ActivateMaquina
import com.example.listacomponenteqr.presentation.components.DisplayAlert
import com.example.listacomponenteqr.presentation.maquina_list.components.MaquinasListActivate
import com.example.listacomponenteqr.ui.theme.blackdark
import com.example.zitrocrm.screens.login.components.ProgressBarLoading
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

@Composable
fun MaquinasActivate(
    cameraM:CameraManager,
    navController: NavController,
    viewModel: ActMaquinasViewModel = hiltViewModel(),
){
    val act_manual = rememberSaveable{ mutableStateOf(false) }
    val actmaquinas = viewModel.actmaquinas.value
    Scaffold(
        topBar = {
            CustomTopAppBarMaquina(navController,act_manual)
        }
    ) {
        if(act_manual.value){
            Column(
                modifier = Modifier
                    .padding(10.dp, 10.dp, 10.dp, 0.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .background(Color.Black)
            ) {
                tittleActManual()
                activarManual()
            }
        }else{
            ContentQR(cameraM = cameraM,act_manual,actmaquinas)
        }
        ProgressBarLoading()
        alertActivacion(viewModel)
    }
}
@Composable
private fun activarManual(
    viewModel: ActMaquinasViewModel = hiltViewModel()
) {
    Column() {
        val context = LocalContext.current
        OutlinedTextField(
            value = viewModel.serie_maquina.value,
            onValueChange = {
                viewModel.serie_maquina.value = it
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Ascii,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    /*if (imputsearch.value.isNotBlank()) {
                        viewModel.getCodSolicitud(
                            imputsearch.value,
                            context
                        )
                    }*/
                }
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 2.dp, horizontal = 10.dp),
            label = { Text("Ingresa serie de la maquina") },
        )
        OutlinedTextField(
            value = viewModel.componente1.value,
            onValueChange = {
                viewModel.componente1.value = it
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Ascii,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {

                }
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 2.dp, horizontal = 10.dp),
            label = { Text("Ingresa codigo y serie del Cpu") },
        )
        OutlinedTextField(
            value = viewModel.componente2.value,
            onValueChange = {
                viewModel.componente2.value = it
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Ascii,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {

                }
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 2.dp, horizontal = 10.dp),
            label = { Text("Ingresa codigo y serie del Monitor") },
        )
        val isValidate by derivedStateOf {
            viewModel.serie_maquina.value.isNotBlank()
                    &&viewModel.componente1.value.isNotBlank()
                    &&viewModel.componente2.value.isNotBlank()
        }
        val isRotated = rememberSaveable { mutableStateOf(false) }
        val rotationAngle by animateFloatAsState(
            targetValue = if (isRotated.value) 360F else 0F,
            animationSpec = tween(durationMillis = 500,easing = FastOutLinearInEasing)
        )
        Button(
            enabled = isValidate,
            onClick = {
                isRotated.value = !isRotated.value
                viewModel.getActivateComponents(manual = true,viewModel.serie_maquina.value,viewModel.componente1.value,viewModel.componente2.value,context )
            },
            modifier = Modifier
                .padding(vertical = 10.dp, horizontal = 10.dp)
                .fillMaxWidth()
                .height(55.dp)
                .graphicsLayer {
                    rotationY = rotationAngle
                    cameraDistance = 8 * density
                }
            ,
            shape = RoundedCornerShape(10),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = colorResource(id = R.color.reds)
            )
        ) {
            Text(
                text = "Activar Maquina",
                fontSize = 16.sp,
                color = Color.White
            )
        }
    }
}

@Composable
private fun tittleActManual(
){
    Box(modifier = Modifier
        .padding(0.dp, 0.dp, 0.dp, 5.dp)
        .clip(RoundedCornerShape(10.dp))
        .background(blackdark)
        .fillMaxWidth()
        .padding(5.dp),
    ) {
        Column(Modifier.align(Alignment.Center)) {
            Image(
                painter = painterResource(R.drawable.maquinas_icon),
                contentDescription = "",
                modifier = Modifier
                    .padding(0.dp)
                    .size(50.dp)
                    .align(CenterHorizontally)
            )
            Text(
                text = "ACTIVAR MAQUINA MANUAL",
                style = MaterialTheme.typography.subtitle2,
                modifier = Modifier.align(Alignment.CenterHorizontally))
        }
    }
}

@Composable
private fun CustomTopAppBarMaquina(
    navController: NavController,
    act_manual : MutableState<Boolean>,
    viewModel: ActMaquinasViewModel = hiltViewModel(),
) {
    val icon =
        if (act_manual.value) Icons.Filled.QrCodeScanner
        else Icons.Filled.Keyboard
    val text = if(act_manual.value)  "Activacion por QR"
    else "Activacion manual"
    val barCodeValueText = remember { mutableStateOf("") }
    val context = LocalContext.current
    if (viewModel.alertstate.value) {
        LaunchedEffect(Unit) {
            while (true) {
                delay(4.seconds)
                viewModel.alertstate.value=false
            }
        }
    }
    DisplayAlert()
    TopAppBar(
        elevation = 0.dp,
        modifier = Modifier.height(70.dp),
        title = {
            Box(modifier = Modifier.fillMaxSize()) {
                Image(
                    painter = painterResource(R.drawable.back_button),
                    contentDescription = "",
                    modifier = Modifier
                        .clickable {
                            navController.popBackStack()
                        }
                        .align(CenterStart)
                        .size(29.dp)
                )
                Image(
                    painter = painterResource(R.drawable.logo_qr),
                    contentDescription = "",
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(5.dp)
                        .padding(start = 0.dp, end = 15.dp)
                )
                Column(modifier = Modifier
                    .padding(horizontal = 40.dp)
                    .align(CenterStart)
                    .clickable {
                        act_manual.value = !act_manual.value
                    }) {
                    Icon(icon, contentDescription = "QR",modifier = Modifier.align(CenterHorizontally),Color.White)
                    Text(text = text,fontSize = 9.sp,modifier = Modifier.align(CenterHorizontally), color = Color.White)
                }
                if(act_manual.value==false) {
                    TextField(
                        value = barCodeValueText.value,
                        onValueChange = { barCodeValueText.value = it },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done,
                            keyboardType = KeyboardType.Phone),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                viewModel.SerieListSeach(barCodeValueText.value,context)
                            }
                        ),
                        modifier = Modifier
                            .width(175.dp)
                            .align(Alignment.CenterEnd)
                            .padding(end = 0.dp)
                            .scale(scaleY = 0.66F, scaleX = .63F),
                        label = { Text(text = "Buscar por serie") },
                    )
                    Box(modifier = Modifier.align(Alignment.CenterEnd)) {
                        IconButton(
                            onClick = {
                                viewModel.SerieListSeach(barCodeValueText.value,context)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Search,
                                contentDescription = "Icon Search",
                                tint = Color.White,
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .scale(scaleY = 0.7F, scaleX = .7F)
                            )
                        }
                    }
                }
            }
        },
        backgroundColor = reds,
    )
}
@Composable
private fun ContentQR(
    cameraM: CameraManager,
    act_manual: MutableState<Boolean>,
    actmaquinas: ActivateMaquina?,
    ){
    Column() {
        Box(
            Modifier
                .fillMaxWidth()
                .height(300.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            CameraPreview(
                cameraM = cameraM,
                actmaquinas = actmaquinas,
                act_manual = act_manual
            )
        }
        Details()
        MaquinaListScreen()
    }
}

@Composable
private fun CameraPreview(
    viewModel: ActMaquinasViewModel = hiltViewModel(),
    cameraM: CameraManager,
    actmaquinas: ActivateMaquina?,
    act_manual : MutableState<Boolean>
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var preview by remember { mutableStateOf<Preview?>(null) }
    val barCodeVal = remember { mutableStateOf("") }
    Column(modifier = Modifier
        .fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(10.dp, 10.dp, 10.dp, 0.dp),
            verticalAlignment = CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.maquinas_icon),
                contentDescription = "",
                modifier = Modifier
                    .size(50.dp)
                    .padding(0.dp, 0.dp, 10.dp, 0.dp),
                contentScale = ContentScale.Fit
            )
            Text(
                text = viewModel.modelo.value,
                style = MaterialTheme.typography.subtitle2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(0.dp, 0.dp, 0.dp, 0.dp)
            )
            Text(
                text = viewModel.serie.value,
                style = MaterialTheme.typography.subtitle2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(20.dp, 0.dp, 20.dp, 0.dp)
            )

        }
        Card(modifier = Modifier
            .fillMaxWidth()
            .height(252.dp)
            .padding(10.dp)) {
            val mMediaPlayer = MediaPlayer.create(context, R.raw.bip)
            AndroidView(factory = { AndroidViewContext ->
                PreviewView(AndroidViewContext).apply {
                    this.scaleType = PreviewView.ScaleType.FILL_CENTER
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                    )
                    implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                }
            },
                modifier = Modifier.fillMaxWidth(),
                update = { previewView ->
                    val cameraSelector: CameraSelector = CameraSelector.Builder()
                        .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                        .build()
                    val cameraExecutors: ExecutorService = Executors.newSingleThreadExecutor()
                    val cameraProviderFuture: ListenableFuture<ProcessCameraProvider> =
                        ProcessCameraProvider.getInstance(context)

                    cameraProviderFuture.addListener({
                        preview = Preview.Builder().build().also {
                            it.setSurfaceProvider(previewView.surfaceProvider)
                        }

                        val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
                        val barcodeAnalyser = BarcodeAnalyser { barcodes ->
                            barcodes.forEach { barcode ->
                                barcode.rawValue?.let { barcodeValue ->
                                    if(act_manual.value==false){
                                        barCodeVal.value = barcodeValue
                                        if(viewModel.delay.value) {
                                            mMediaPlayer.start()
                                            viewModel.MaquinaComponentValidation(barcodeValue,context)
                                            viewModel.MaquinaActivateComponentes(barcodeValue,context)
                                        }
                                    }
                                }
                            }
                        }
                        val imageAnalysis: ImageAnalysis = ImageAnalysis.Builder()
                            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                            .build()
                            .also {
                                it.setAnalyzer(cameraExecutors, barcodeAnalyser)
                            }
                        try {
                            cameraProvider.unbindAll()
                            cameraProvider.bindToLifecycle(
                                lifecycleOwner,
                                cameraSelector,
                                preview,
                                imageAnalysis
                            )
                        } catch (e: Exception) {
                            Log.d("", "CameraPreview: ${e.localizedMessage}")
                        }
                    }, ContextCompat.getMainExecutor(context))
                }
            )
            Image(
                painter = painterResource(R.drawable.background_camera),
                contentDescription = "",
                modifier = Modifier
                    .fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
    }
    /*Box(
        modifier = Modifier
            .size(110.dp)
            .padding(0.dp, 68.dp, 0.dp, 0.dp)
    ) {
        //FlashLightComposable(cameraM)
    }*/
}

@Composable
private fun MaquinaListScreen(
    viewModel: ActMaquinasViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val actmaquinas = viewModel.actmaquinas.value
    Box(modifier = Modifier.fillMaxSize()) {
        if(actmaquinas == null) { }
        else{
           MaquinasListActivate(
                actmaquinas = actmaquinas,
            )
        }
        if(state.error.isNotBlank()) {
            Card{
                Text(
                    text = state.error,
                    color = MaterialTheme.colors.error,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                        .align(Alignment.TopCenter)
                )
            }
        }
        if(state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(20.dp)
            )
        }
    }
}

@Composable
private fun Details () {
    Card(modifier = Modifier
        .fillMaxWidth()
        .height(50.dp)
        .padding(horizontal = 5.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Icon(Icons.Filled.ShortText, "",
                modifier = Modifier
                    .align(CenterVertically)
                    .padding(horizontal = 10.dp)
            )
            Text(
                text = "Detalle de la activación",
                style = MaterialTheme.typography.subtitle2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(10.dp, 0.dp, 0.dp, 0.dp)
                    .align(alignment = CenterVertically)
            )
        }
    }
}

@Composable
private fun FlashLightComposable(cameraM: CameraManager){
    val state = remember{
        mutableStateOf(false)
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
    ){
        Image(painter = if (state.value) painterResource(
            id = R.drawable.flashlight_icon_on) else painterResource(
            id = R.drawable.flashlight_icon_off
        ), contentDescription = "power-off", modifier = Modifier
            .fillMaxSize()
            .fillMaxWidth()
            .clickable {
                if (!state.value) {
                    val rearCamera = cameraM.cameraIdList[0]
                    cameraM.setTorchMode(rearCamera, true)
                    state.value = true
                } else {
                    val rearCamera = cameraM.cameraIdList[0]
                    cameraM.setTorchMode(rearCamera, false)
                    state.value = false
                }
            }
        )
    }
}
@Composable
private fun alertActivacion(
    viewModel: ActMaquinasViewModel
){
    val alert = viewModel.alert_manual.value
    val color_m = viewModel.alert_color_manual.value
    val color by animateColorAsState(
        when (color_m) {
            true -> Color.Green
            else ->Color.Red
        }
    )
    val icon =
        if (color_m) Icons.Filled.AddTask
        else Icons.Filled.Cancel

    AnimatedVisibility(visible = alert) {
        AlertDialog(
            onDismissRequest = {},
            buttons = {
                Column{
                    Row(Modifier.padding(all = 25.dp)){
                        Icon(
                            icon, "",
                            tint = Color.Green,
                            modifier = Modifier
                                .align(CenterVertically)
                                .padding(horizontal = 10.dp)
                        )
                        Text(
                            text = viewModel.text_manual.value,
                            fontSize = 14.sp,
                            color = Color.White,
                            modifier = Modifier
                                .align(CenterVertically)
                        )
                    }
                    Divider(color = color, thickness = 3.dp)
                }
            },
            properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = false),
            modifier = Modifier.clip(RoundedCornerShape(25.dp)),
            shape = RoundedCornerShape(18.dp)
        )
    }
}