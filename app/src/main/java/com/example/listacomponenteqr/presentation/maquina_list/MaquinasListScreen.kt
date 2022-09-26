package com.example.listacomponenteqr.presentation.maquina_list

import android.hardware.camera2.CameraManager
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
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.listacomponenteqr.R
import com.example.listacomponenteqr.presentation.components.DisplayAlert
import com.example.listacomponenteqr.presentation.maquina_list.components.MaquinasListItem
import com.example.listacomponenteqr.ui.theme.reds
import com.example.listacomponenteqr.utils.BarcodeAnalyser
import com.example.listacomponenteqr.utils.Utils
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.delay
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.time.Duration.Companion.seconds

@RequiresApi(Build.VERSION_CODES.M)
@Composable
fun MaquinasListScreen (cameraM:CameraManager, navController: NavController){
    Scaffold(
        topBar = {
            CustomTopAppBarHome(navController)
        }
    ) {
        ContentQR(cameraM = cameraM)
    }
}
/** TOPBAR **/
@Composable
private fun CustomTopAppBarHome(
    navController: NavController,
    viewModel: MaquinasListViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
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
                val barCodeValueText = remember { mutableStateOf("") }
                val stringtext: String = barCodeValueText.value
                TextField(
                    value = barCodeValueText.value,
                    onValueChange = { barCodeValueText.value = it },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Phone),
                    keyboardActions = KeyboardActions(
                        onDone = {viewModel.SerieListSeach(stringtext,context) }
                    ),
                    modifier = Modifier
                        .width(185.dp)
                        .align(Alignment.CenterEnd)
                        .padding(end = 0.dp)
                        .scale(scaleY = 0.66F, scaleX = .63F),
                    label = { Text(text = "Buscar por serie") },
                    placeholder = {}
                )
                Box(modifier = Modifier.align(Alignment.CenterEnd)) {
                    IconButton(
                        onClick = {viewModel.SerieListSeach(stringtext,context) }
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
        },
        backgroundColor = reds,
    )
}
@RequiresApi(Build.VERSION_CODES.M)
@Composable
private fun ContentQR(
    cameraM: CameraManager,
){
    Column() {
        Box(
            Modifier
                .fillMaxWidth()
                .height(300.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            CameraPreview(cameraM = cameraM)
        }
        Details()
        MaquinaListScreen()
    }
}

@Composable
private fun CameraPreview(
    viewModel: MaquinasListViewModel = hiltViewModel(),
    cameraM: CameraManager,
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var preview by remember { mutableStateOf<Preview?>(null) }

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
                                    mMediaPlayer.start()
                                    viewModel.MaquinaComponentValidation(barcodeValue,context)
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
private fun MaquinaListScreen(
    viewModel: MaquinasListViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val maquinas = viewModel.maquinas.value
    Box(modifier = Modifier.fillMaxSize()) {
        if(state.error.isNotBlank()) {
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
        if(state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(20.dp)
            )
        }
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(maquinas.orEmpty()) { maquina ->
                MaquinasListItem(
                    maquina = maquina,
                )
            }
        }
    }
}

@Composable
private fun Details (
    viewModel: MaquinasListViewModel = hiltViewModel()
) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .height(50.dp)
        .padding(horizontal = 5.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = "Estatus",
                style = MaterialTheme.typography.subtitle2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(5.dp, 0.dp, 0.dp, 0.dp)
                    .align(alignment = Alignment.CenterVertically)
            )
            Text(
                text = "Código",
                style = MaterialTheme.typography.subtitle2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(15.dp, 0.dp, 0.dp, 0.dp)
                    .align(alignment = Alignment.CenterVertically)
            )
            Text(
                text = "Descripción del componente",
                style = MaterialTheme.typography.subtitle2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(20.dp, 0.dp, 0.dp, 0.dp)
                    .align(alignment = Alignment.CenterVertically)
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