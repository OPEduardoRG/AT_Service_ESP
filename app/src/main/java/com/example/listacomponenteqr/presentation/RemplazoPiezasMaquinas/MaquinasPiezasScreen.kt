package com.example.listacomponenteqr.presentation.RemplazoPiezasMaquinas

import android.media.MediaPlayer
import android.os.Build
import android.util.Log
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.listacomponenteqr.R
import com.example.listacomponenteqr.ui.theme.blackdark
import com.example.listacomponenteqr.ui.theme.reds
import com.example.listacomponenteqr.utils.BarcodeAnalyser
import com.example.zitrocrm.screens.login.components.ProgressBarLoading
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.common.util.concurrent.ListenableFuture
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Composable
fun MaquinasPiezasScreen(
    navController: NavController
){
    var manual = remember {
        mutableStateOf(false)
    }
    Scaffold(
        topBar = {
            CustomTopAppBarPiezas(navController,manual)
        }
    ) {
        if(manual.value){
            Column(
                modifier = Modifier
                    .padding(10.dp, 10.dp, 10.dp, 0.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .background(Color.Black)
            ) {
                tittleRemplazoManual()
                remplazoManual()
            }
        }else{
            ContentQR(manual)
        }
        ProgressBarLoading()
        alertRemplazo()
    }
}
@Composable
private fun tittleRemplazoManual(
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
                    .align(Alignment.CenterHorizontally)
            )
            Text(
                text = "REMPLAZO MANUAL",
                style = MaterialTheme.typography.subtitle2,
                modifier = Modifier.align(Alignment.CenterHorizontally))
        }
    }
}
@Composable
fun remplazoManual(
    viewModel: MaqPiezasViewModel = hiltViewModel()
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
            label = { Text("Ingresa codigo componente a remplazar") },
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
            label = { Text("Ingresa codigo componente nuevo") },
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
                viewModel.n.value = 1
                viewModel.postRemplazoPiezas(
                    manual = true,
                    idMaquina = viewModel.serie_maquina.value,
                    codigo1x = viewModel.componente1.value,
                    codigo2x = viewModel.componente2.value,
                    context = context
                )
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
                text = "Remplazar componente",
                fontSize = 16.sp,
                color = Color.White
            )
        }
    }
}

@Composable
private fun CustomTopAppBarPiezas(
    navController: NavController,
    manual : MutableState<Boolean>,
    viewModel: MaqPiezasViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
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
                        .align(Alignment.CenterStart)
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
                if(manual.value){
                    /**SE REALIZA EL CAMBIO DE PIEZAS MANUAL**/
                    /*Column(modifier = Modifier
                        .padding(horizontal = 40.dp)
                        .align(Alignment.CenterStart)
                        .clickable {
                            manual.value = !manual.value
                            viewModel.clear()
                        }) {
                        Icon(Icons.Filled.QrCodeScanner, contentDescription = "QR",modifier = Modifier.align(
                            Alignment.CenterHorizontally
                        ),Color.White)

                        Text(text = "Remplazo por QR",fontSize = 9.sp,modifier = Modifier.align(
                            Alignment.CenterHorizontally
                        ), color = Color.White)
                    }*/
                }else{
                    /*Column(modifier = Modifier
                        .padding(horizontal = 40.dp)
                        .align(Alignment.CenterStart)
                        .clickable {
                            manual.value = !manual.value
                            viewModel.clear()
                        }) {
                        Icon(Icons.Filled.Keyboard, contentDescription = "QR",modifier = Modifier.align(
                            Alignment.CenterHorizontally
                        ),Color.White)

                        Text(text = "Remplazo manual",fontSize = 9.sp,modifier = Modifier.align(
                            Alignment.CenterHorizontally
                        ), color = Color.White)
                    }*/
                    val barCodeValueText = remember { mutableStateOf("") }
                    val stringtext: String = barCodeValueText.value
                    TextField(
                        value = barCodeValueText.value,
                        onValueChange = { barCodeValueText.value = it },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done,
                            keyboardType = KeyboardType.Phone),
                        modifier = Modifier
                            .width(175.dp)
                            .align(Alignment.CenterEnd)
                            .padding(end = 0.dp)
                            .scale(scaleY = 0.66F, scaleX = .63F),
                        label = { Text(text = "Buscar por serie") },
                        placeholder = {}
                    )
                    Box(modifier = Modifier.align(Alignment.CenterEnd)) {
                        IconButton(
                            onClick = {
                                viewModel.SerieListSeach(stringtext, context = context)
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

@RequiresApi(Build.VERSION_CODES.M)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun ContentQR(
    manual:MutableState<Boolean>
){
    Column() {
        Box(
            Modifier
                .fillMaxWidth()
                .height(300.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            CameraPreview(manual)
        }
        Details()
        EstatusRemplazo()
    }
}
@Composable
private fun CameraPreview(
    manual:MutableState<Boolean>,
    viewModel: MaqPiezasViewModel = hiltViewModel(),
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
            verticalAlignment = Alignment.CenterVertically
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
                text = "${viewModel.modelo.value}",
                style = MaterialTheme.typography.subtitle2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(0.dp, 0.dp, 0.dp, 0.dp)
            )
            Text(
                text = "${viewModel.serie.value}",
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
                                    if(manual.value==false){
                                        mMediaPlayer.start()
                                        barCodeVal.value = barcodeValue
                                        viewModel.MaquinaActivateComponentes(barcodeValue,context)
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
    Box(
        modifier = Modifier
            .size(110.dp)
            .padding(0.dp, 68.dp, 0.dp, 0.dp)
    ) {
        //FlashLightComposable(cameraM)
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
                    .align(Alignment.CenterVertically)
                    .padding(horizontal = 10.dp)
            )
            Text(
                text = "Detalle del remplazo de componentes.",
                style = MaterialTheme.typography.subtitle2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(10.dp, 0.dp, 0.dp, 0.dp)
                    .align(alignment = Alignment.CenterVertically)
            )
        }
    }
}

@Composable
private fun EstatusRemplazo(
    viewModel: MaqPiezasViewModel = hiltViewModel()
) {
    val color = viewModel.alertstatecolor.value
    val text = viewModel.textalert.value
    val state = viewModel.alertstate.value

    if(state){
        if (color){
            Divider(color = Color.Green, thickness = 3.dp, modifier = Modifier.padding(horizontal = 5.dp))
        }else{
            Divider(color = Color.Red, thickness = 3.dp, modifier = Modifier.padding(horizontal = 5.dp))
        }
        Card(modifier = Modifier
            .height(150.dp)
            .fillMaxWidth()
            .padding(horizontal = 5.dp, vertical = 1.dp)
            .clickable { },
            shape = MaterialTheme.shapes.large)
        {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp, 20.dp, 5.dp, 15.dp)
            ) {
                if (color==false) {
                    Icon(
                        Icons.Filled.Cancel, "",
                        tint = Color.Red,
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(horizontal = 10.dp)
                    )
                    Column(modifier = Modifier.align(Alignment.CenterVertically)) {
                        Text(
                            text = text,
                            style = MaterialTheme.typography.subtitle2,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .padding(horizontal = 10.dp)
                                .align(alignment = Alignment.Start)
                        )
                        Spacer(modifier = Modifier.height(15.dp))
                    }
                } else {
                    Icon(
                        Icons.Filled.AddTask, "",
                        tint = Color.Green,
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(horizontal = 10.dp)
                    )
                    Column(modifier = Modifier.align(Alignment.CenterVertically)) {
                        Text(
                            text = text,
                            style = MaterialTheme.typography.subtitle2,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .padding(horizontal = 10.dp)
                                .align(alignment = Alignment.Start)
                        )
                        Spacer(modifier = Modifier.height(15.dp))
                    }
                }
            }
        }
    }
}
@Composable
private fun alertRemplazo(
    viewModel: MaqPiezasViewModel = hiltViewModel(),
){
    AnimatedVisibility(visible = viewModel.alert_manual.value) {
        AlertDialog(
            onDismissRequest = {
            },
            title = null,
            text = null,
            buttons = {
                Column{
                    Row(Modifier.padding(all = 25.dp)){
                        if (viewModel.alert_color_manual.value){
                            Icon(
                                Icons.Filled.AddTask, "",
                                tint = Color.Green,
                                modifier = Modifier
                                    .align(Alignment.CenterVertically)
                                    .padding(horizontal = 10.dp)
                            )
                        } else{
                            Icon(
                                Icons.Filled.Cancel, "",
                                tint = Color.Red,
                                modifier = Modifier
                                    .align(Alignment.CenterVertically)
                                    .padding(horizontal = 10.dp)
                            )
                        }
                        Text(
                            text = viewModel.text_manual.value,
                            fontSize = 14.sp,
                            color = Color.White,
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                        )
                        //Text(text = viewModel.textalert.value)
                    }
                    if (viewModel.alert_color_manual.value){
                        Divider(color = Color.Green, thickness = 3.dp)
                    }else {
                        Divider(color = Color.Red, thickness = 3.dp)
                    }
                }
            },
            properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = false),
            modifier = Modifier.clip(RoundedCornerShape(25.dp)),
            shape = RoundedCornerShape(18.dp)
        )
    }
}