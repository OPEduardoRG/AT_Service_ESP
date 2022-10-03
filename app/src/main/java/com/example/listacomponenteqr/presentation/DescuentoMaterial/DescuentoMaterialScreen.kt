package com.example.listacomponenteqr.presentation.DescuentoMaterial

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.util.Log
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.listacomponenteqr.R
import com.example.listacomponenteqr.data.remote.dto.MaquinasSala.MaquinasSala
import com.example.listacomponenteqr.data.remote.dto.SolicitudRefaccion.Refacciones
import com.example.listacomponenteqr.data.remote.dto.SolicitudRefaccion.RegionesEsp
import com.example.listacomponenteqr.data.remote.dto.SolicitudRefaccion.Salas
import com.example.listacomponenteqr.data.remote.dto.SolicitudRefaccion.Solicitud
import com.example.listacomponenteqr.presentation.maquinas_en_sala.MaquinasSalaScreen
import com.example.listacomponenteqr.presentation.maquinas_en_sala.MaquinasSalaViewModel
import com.example.listacomponenteqr.ui.theme.blackdark
import com.example.listacomponenteqr.ui.theme.blacktransp
import com.example.listacomponenteqr.ui.theme.graydark
import com.example.listacomponenteqr.ui.theme.reds
import com.example.listacomponenteqr.utils.BarcodeAnalyser
import com.example.listacomponenteqr.utils.SharedPrefence
import com.example.listacomponenteqr.utils.Utils
import com.example.zitrocrm.screens.login.components.progressBar
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.delay
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
/**
 * Created by Brian Fernando Mtz on 07-2022.
 */
@SuppressLint("UnrememberedMutableState", "UnusedTransitionTargetStateParameter")
@Composable
fun DescuentoMaterialScreen(
    navController: NavController,
) {
    val viewModel : DescuentoViewModel = hiltViewModel()
    val drop = rememberSaveable { mutableStateOf(false) }
    val dropcam = rememberSaveable { mutableStateOf(false) }
    val imputsearch = rememberSaveable() { mutableStateOf("") }
    val granel = rememberSaveable() { mutableStateOf("") }
    val descrip = rememberSaveable() { mutableStateOf("") }
    val cantidad = rememberSaveable() { mutableStateOf("") }
    val droplist = rememberSaveable { mutableStateOf(true) }
    val seriex = rememberSaveable() { mutableStateOf("") }
    val observacionesx = rememberSaveable() { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

//    val regionesEspana = MaquinasSalaViewModel().regionesEspana

    var regionesEspana = when (viewModel.getListSimilar.size) {
        0 -> MaquinasSalaViewModel().regionesEspana
        else -> viewModel.getListSimilar
    }
    val dropRegion = rememberSaveable { mutableStateOf(false) }
    val dropSala = rememberSaveable { mutableStateOf(false) }
    val inputRegion = rememberSaveable() { mutableStateOf("") }
    val inputSala = rememberSaveable() { mutableStateOf("") }
    val inputSalaID = rememberSaveable() { mutableStateOf("") }
    val context = LocalContext.current
    val qr_val = Utils(context)
    val camMaquina = mutableStateOf(false)
    if(viewModel.response.value=="1"){
        descrip.value = viewModel.description.value
        if(viewModel.granel.value=="1"){
            cantidad.value = ""
            granel.value = "1"
        }else{
            cantidad.value = "1"
            granel.value = "0"
        }
        viewModel.response.value="0"
    }
    LaunchedEffect(key1 = imputsearch.value, block = {
        if (imputsearch.value.isBlank()) return@LaunchedEffect
        delay(2000)
        if (descrip.value.isEmpty()){
            viewModel.getMaterial(imputsearch.value,context)
            drop.value = true
        }
    })
    Scaffold(
        topBar = {
            TopAppBarDescuento(navController,viewModel.listDescuento,seriex.value,observacionesx.value,inputSalaID.value)
        }
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 10.dp)
        ) {
            Column() {
                LazyColumn(
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(blacktransp)
                ) {
                    item{
                        titleHomeDescuento()
                    }
                    item{
                        val transitionState = remember {
                            MutableTransitionState(dropRegion.value).apply {
                                targetState = !dropRegion.value
                            }
                        }
                        val transition =
                            updateTransition(targetState = transitionState, label = "transition")
                        val arrowRotationDegree by transition.animateFloat({
                            tween(durationMillis = 300)
                        }, label = "rotationDegreeTransition") {
                            if (dropRegion.value) 0f else 180f
                        }
                        OutlinedTextField(
                            enabled=true,
                            value = inputRegion.value,
                            onValueChange = {
                                            inputRegion.value = it
                                viewModel.getValidarSimilarRegion(context, it)
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Done
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 2.dp, horizontal = 10.dp)
                                .onFocusChanged { focusState ->
                                    if (focusState.isFocused) {
                                        dropRegion.value = true
                                    } else if (focusState.hasFocus) {
                                        dropRegion.value = false
                                        focusManager.clearFocus()
                                    }
                                }
//                                .clickable {
//                                    dropRegion.value = !dropRegion.value
//                                    dropSala.value = false
//                                }
                                ,
                            label = { Text("Selecciona o ingresa la región") },
                            trailingIcon = {
                                CardArrow(
                                    arrowRotationDegree,
                                    { dropRegion.value = !dropRegion.value }
                                )
                            }
                        )
                    }
                    if(dropRegion.value){
                        itemsIndexed(regionesEspana) { index, item ->
                            Card(
                                Modifier
                                    .padding(horizontal = 10.dp, vertical = 3.dp)
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(5.dp))
                                    .clickable {
                                        inputRegion.value = item.nombre.toString()
                                        dropRegion.value = false
                                        viewModel.getSalas(item.regionidx.toString())
                                        inputSala.value = ""
                                        focusManager.clearFocus()
                                        viewModel.maquinasSala.clear()
                                        inputSala.value = ""
                                        viewModel.getListSimilarSalas.clear()
                                    }
                            ) {
                                Row(
                                    modifier = Modifier
                                        .align(Alignment.CenterHorizontally)
                                        .padding(horizontal = 10.dp, vertical = 10.dp)
                                ) {
                                    Icon(
                                        Icons.Filled.AddLocationAlt,
                                        "contentDescription",
                                        modifier = Modifier
                                            .fillMaxWidth(.1f)
                                            .align(CenterVertically)
                                            .padding(horizontal = 5.dp)
                                    )
                                    Text(
                                        text = "${item.nombre}",
                                        style = MaterialTheme.typography.subtitle2,
                                        fontSize = 15.sp,
                                        modifier = Modifier
                                            .align(CenterVertically)
                                            .padding(horizontal = 10.dp)
                                    )
                                }
                            }
                        }
                    }
                    item{
                        OutlinedTextField(
                            enabled = false,
                            value = inputSala.value,
                            onValueChange = {
                            },
                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Ascii,
                                imeAction = ImeAction.Done
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 2.dp, horizontal = 10.dp)
                                .clickable {
                                    dropSala.value = !dropSala.value
                                    dropRegion.value = false
                                },
                            label = { Text("Selecciona la sala") },
                            trailingIcon = {
                                IconButton(onClick = {
                                    dropSala.value = !dropSala.value
                                    dropRegion.value =  false
                                }) {
                                    Icon(Icons.Filled.ExpandLess, "contentDescription")
                                }
                            }
                        )
                    }
                    item {
                        AnimatedVisibility(
                            visible = dropSala.value
                        ) {
                            if (viewModel.salasxRegion.isEmpty()) {
                                Card(
                                    Modifier
                                        .padding(horizontal = 10.dp, vertical = 3.dp)
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(10.dp))
                                ) {
                                    Text(
                                        text = "No hay resultados",
                                        style = MaterialTheme.typography.subtitle2,
                                        fontSize = 12.sp,
                                        modifier = Modifier
                                            .align(Alignment.CenterHorizontally)
                                            .padding(horizontal = 10.dp, vertical = 15.dp)
                                    )
                                }
                            }
                        }
                    }
                    if(dropSala.value){
                        itemsIndexed(viewModel.salasxRegion) { idex, item ->
                            Card(
                                Modifier
                                    .padding(horizontal = 10.dp, vertical = 3.dp)
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(5.dp))
                                    .background(color = Color.Transparent)
                                    .clickable {
                                        inputSala.value = item.nombre.toString()
                                        inputSalaID.value = item.salaid.toString()
                                        dropSala.value = false
                                    }
                            ) {
                                Row(
                                    modifier = Modifier
                                        .align(Alignment.CenterHorizontally)
                                        .padding(horizontal = 10.dp, vertical = 10.dp)
                                ) {
                                    Icon(
                                        Icons.Filled.AddLocationAlt,
                                        "contentDescription",
                                        modifier = Modifier
                                            .fillMaxWidth(.1f)
                                            .align(CenterVertically)
                                            .padding(horizontal = 5.dp)
                                    )
                                    Text(
                                        text = "${item.nombre}",
                                        style = MaterialTheme.typography.subtitle2,
                                        fontSize = 15.sp,
                                        modifier = Modifier
                                            .align(CenterVertically)
                                            .padding(horizontal = 10.dp)
                                    )
                                }
                            }
                        }
                    }

                    item {
                        OutlinedTextField(
                            value = seriex.value,
                            onValueChange = {
                                seriex.value = it
                            },
                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Done,
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                }
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 2.dp, horizontal = 10.dp),
                            label = { Text("Escanea o ingresa la serie de la máquina") },
                            trailingIcon = {
                                Icon(Icons.Filled.Subtitles, "contentDescription")
                            },
                            leadingIcon ={
                                IconButton(onClick = {
                                    drop.value = false
                                    dropcam.value = false
                                    camMaquina.value = !camMaquina.value
                                },) {
                                    Icon(Icons.Filled.QrCode, "contentDescription")
                                }
                            }
                        )
                    }
                    item {
                        cameraOpen(camMaquina,seriex,false)
                    }
                    item {
                        OutlinedTextField(
                            value = observacionesx.value,
                            onValueChange = {
                                observacionesx.value = it
                            },
                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Done,
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                }
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 2.dp, horizontal = 10.dp),
                            label = { Text("Observaciones") },
                            trailingIcon = {
                                Icon(Icons.Filled.Rtt, "contentDescription")
                            },
                        )
                        Divider(color = colorResource(R.color.graydark), thickness = 1.dp, modifier = Modifier.padding(top = 9.dp, start = 8.dp, end = 8.dp))
                    }
                    item {
                        OutlinedTextField(
                            value = imputsearch.value,
                            onValueChange = {
                                imputsearch.value = it
                                descrip.value = ""
                                dropcam.value = false
                            },
                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Ascii,
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    if(imputsearch.value.isNotBlank()){
                                        progressBar.value =true
                                        viewModel.getMaterial(imputsearch.value,context)
                                    }
                                }
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 2.dp, horizontal = 10.dp),
                            label = { Text("Ingresa código o nombre del material") },
                            trailingIcon = {
                                IconButton(onClick = {
                                    dropcam.value = false
                                    camMaquina.value = false
                                    drop.value = !drop.value
                                }) {
                                    Icon(Icons.Filled.ExpandLess, "contentDescription")
                                }
                            },
                            leadingIcon ={
                                IconButton(onClick = {
                                    drop.value = false
                                    camMaquina.value = false
                                    dropcam.value = !dropcam.value
                                },) {
                                    Icon(Icons.Filled.QrCode, "contentDescription")
                                }
                            }
                        )
                        AnimatedVisibility(
                            visible = drop.value
                        ) {
                            if (viewModel.codigo.isEmpty()) {
                                Card(
                                    Modifier
                                        .padding(horizontal = 10.dp, vertical = 3.dp)
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(10.dp))
                                ) {
                                    Text(
                                        text = " ",
                                        style = MaterialTheme.typography.subtitle2,
                                        fontSize = 12.sp,
                                        modifier = Modifier
                                            .align(Alignment.CenterHorizontally)
                                            .padding(horizontal = 10.dp, vertical = 15.dp)
                                    )
                                }
                            }
                        }
                    }
                    item {
                        cameraOpen(dropcam,imputsearch,true)
                    }
                    if(drop.value){
                        itemsIndexed(viewModel.codigo) { index, item ->
                            Card(
                                Modifier
                                    .padding(horizontal = 10.dp, vertical = 3.dp)
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable {
                                        imputsearch.value = item.codigo.toString()
                                        descrip.value = item.nombre.toString()
                                        drop.value = !drop.value
                                        granel.value = item.granel.toString()
                                        if (granel.value == "0") {
                                            cantidad.value = "1"
                                        }
                                    }
                            ) {
                                Text(
                                    text = "Código: " + item.codigo.toString() + "\nDescripción: " + item.nombre.toString(),
                                    style = MaterialTheme.typography.subtitle2,
                                    fontSize = 12.sp,
                                    modifier = Modifier
                                        .align(Alignment.CenterHorizontally)
                                        .padding(horizontal = 10.dp, vertical = 15.dp)
                                )
                            }
                        }
                    }
                    item {
                        OutlinedTextField(
                            enabled = false,
                            value = descrip.value,
                            onValueChange = {
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
                            label = { Text("Descripción del material") },
                            trailingIcon = {
                                Icon(Icons.Filled.Article, "contentDescription")
                            }
                        )
                    }
                    item {
                        AnimatedVisibility(visible = granel.value=="1") {
                            val validationInt = Utils(context)
                            OutlinedTextField(
                                value = cantidad.value,
                                onValueChange = {
                                    if (validationInt.getValidationInt(it) || it.isBlank()) {
                                        cantidad.value = it
                                    }
                                },
                                keyboardOptions = KeyboardOptions.Default.copy(
                                    keyboardType = KeyboardType.Number,
                                    imeAction = ImeAction.Done,
                                ),
                                keyboardActions = KeyboardActions(
                                    onDone = {
                                    }
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 2.dp, horizontal = 10.dp),
                                label = { Text("Cantidad") },
                                trailingIcon = {
                                    Icon(Icons.Filled.Pin, "contentDescription")
                                },
                            )
                        }
                    }
                    val isValidate by derivedStateOf {
                        imputsearch.value.isNotBlank()
                                && descrip.value.isNotBlank()
                                && cantidad.value.isNotBlank()
                    }
                    item {
                        val isRotated = rememberSaveable { mutableStateOf(false) }
                        val rotationAngle by animateFloatAsState(
                            targetValue = if (isRotated.value) 360F else 0F,
                            animationSpec = tween(durationMillis = 500,easing = FastOutLinearInEasing)

                        )
                        Button(
                            enabled = isValidate,
                            onClick = {
                                viewModel.listDescuento.add(
                                    Solicitud(
                                        imputsearch.value,
                                        cantidad.value,
                                        descrip.value
                                    )
                                )
                                imputsearch.value = ""
                                cantidad.value = ""
                                descrip.value = ""
                                granel.value = ""
                                viewModel.codigo = mutableStateListOf<Refacciones>()
                                isRotated.value = !isRotated.value
                            },
                            modifier = Modifier
                                .padding(vertical = 5.dp, horizontal = 10.dp)
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
                                text = "Agregar a la lista",
                                fontSize = 14.sp,
                                color = Color.White
                            )
                        }
                    }
                    if (viewModel.listDescuento.isNotEmpty()) {
                        item {
                            Box(
                                modifier =
                                Modifier
                                    .padding(horizontal = 10.dp, vertical = 5.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .fillMaxWidth()
                                    .background(graydark)
                                    .clickable {
                                        droplist.value = !droplist.value
                                    }
                            ) {
                                Text(
                                    text = "N°: "+viewModel.listDescuento.size.toString(),
                                    style = MaterialTheme.typography.subtitle2,
                                    fontSize = 12.sp,
                                    modifier = Modifier
                                        .padding(horizontal = 10.dp, vertical = 10.dp)
                                        .align(Alignment.CenterStart)
                                )
                                Text(
                                    text = "Lista de materiales a descontar",
                                    style = MaterialTheme.typography.subtitle2,
                                    fontSize = 15.sp,
                                    modifier = Modifier
                                        .padding(horizontal = 10.dp, vertical = 10.dp)
                                        .align(Alignment.Center)
                                )
                                Icon(
                                    Icons.Filled.TouchApp, contentDescription ="",tint = Color.White, modifier = Modifier
                                        .align(Alignment.CenterEnd)
                                        .padding(horizontal = 10.dp))
                            }
                        }
                    }
                    itemsIndexed(viewModel.listDescuento) { index, item ->
                        AnimatedVisibility(visible = droplist.value ) {
                            val isRotated = rememberSaveable { mutableStateOf(false) }
                            val rotationAngle by animateFloatAsState(
                                targetValue = if (isRotated.value) 360F else 0F,
                                animationSpec = tween(durationMillis = 500, easing = FastOutLinearInEasing)
                            )
                            Card(
                                Modifier
                                    .padding(horizontal = 10.dp, vertical = 3.dp)
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .graphicsLayer {
                                        rotationX = rotationAngle
                                        cameraDistance = 16 * density
                                    }
                            ) {
                                Row(modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                                ) {
                                    Icon(
                                        Icons.Filled.FactCheck, contentDescription ="",tint = Color.White,modifier = Modifier
                                            .align(Alignment.CenterVertically)
                                            .padding(start = 10.dp))
                                    Text(
                                        text = "Código: " + item.codigo.toString() + "\nDescripción: " + item.desc.toString() + "\nCantidad: " + item.cantidad.toString(),
                                        style = MaterialTheme.typography.subtitle2,
                                        fontSize = 12.sp,
                                        modifier = Modifier
                                            .fillMaxWidth(.85f)
                                            .padding(horizontal = 10.dp, vertical = 15.dp)
                                    )
                                    IconButton(modifier = Modifier.align(Alignment.CenterVertically),onClick = {
                                        isRotated.value = !isRotated.value
                                        viewModel.listDescuento.removeAt(index)
                                    }) {
                                        Icon(Icons.Filled.Delete, contentDescription ="",tint = Color.White)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    if(viewModel.delate.value) {
        viewModel.listDescuento.clear()
        inputRegion.value = ""
        inputSala.value = ""
        inputSalaID.value = ""
        granel.value = ""
        imputsearch.value = ""
        descrip.value = ""
        cantidad.value = ""
        viewModel.codigo.clear()// = mutableStateListOf<Refacciones>()
        viewModel.salasxRegion.clear()// = mutableStateListOf<Salas>()
        seriex.value = ""
        observacionesx.value = ""

        viewModel.delate.value = false
    }
    alertDescuento()
}

@Composable
fun CardArrow(
    degrees: Float,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        content = {
            Icon(
                Icons.Filled.ArrowDropDown,
                contentDescription = "",
                modifier = Modifier
                    .rotate(degrees)
                    .size(30.dp)
            )
        }
    )
}
@Composable
fun cameraOpen(
    dropcam: MutableState<Boolean>,
    imputsearch: MutableState<String>,
    valor_uno:Boolean
) {
    AnimatedVisibility(
        visible = dropcam.value,
    ) {
        Box(modifier = Modifier
            .height(250.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            val context = LocalContext.current
            val lifecycleOwner = LocalLifecycleOwner.current
            var preview by remember { mutableStateOf<Preview?>(null) }
            val datastore = SharedPrefence(LocalContext.current)
            val mMediaPlayer = MediaPlayer.create(context, R.raw.bip)
            Column(modifier = Modifier
                .fillMaxSize()) {
                Card(modifier = Modifier
                    .fillMaxWidth()
                    .height(282.dp)
                    .padding(10.dp)) {
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
                                            if(dropcam.value) {
                                                mMediaPlayer.start()
                                                val validationmaquina = barcodeValue.split(" ")
                                                if (valor_uno){
                                                    imputsearch.value =validationmaquina[0]
                                                }else{
                                                    imputsearch.value =validationmaquina[1]
                                                }
                                                dropcam.value = false
                                            }
                                        }
                                    }
                                }
                                val imageAnalysis: ImageAnalysis = ImageAnalysis.Builder()
                                    .setBackpressureStrategy(
                                        ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
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
                                        imageAnalysis,
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
        }
    }
}

@Composable
private fun titleHomeDescuento(){
    Column(modifier = Modifier
        .padding(0.dp, 0.dp, 0.dp, 5.dp)
        .clip(RoundedCornerShape(10.dp))
        .background(blackdark)
        .fillMaxWidth()
        .padding(5.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {

        Image(
            painter = painterResource(R.drawable.descuento_mat_icon),
            contentDescription = "",
            modifier = Modifier
                .padding(0.dp)
                .size(50.dp)
        )
        Text(
            text = "DESCUENTO DE MATERIAL",
            style = MaterialTheme.typography.subtitle2,
            modifier = Modifier.align(Alignment.CenterHorizontally))
    }
}
@Composable
private fun TopAppBarDescuento(
    navController: NavController,
    list: MutableList<Solicitud>,
    serie:String,
    observaciones:String,
    sala:String
) {
    val viewModel : DescuentoViewModel = hiltViewModel()
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
                val isValidate by derivedStateOf {
                    list.isNotEmpty()
                            && serie.isNotBlank()
                            && observaciones.isNotBlank()
                            && sala.isNotBlank()
                }
                if(isValidate){
                    val isRotated = rememberSaveable { mutableStateOf(false) }
                    val rotationAngle by animateFloatAsState(
                        targetValue = if (isRotated.value) 360F else 0F,
                        animationSpec = tween(durationMillis = 500, easing = FastOutLinearInEasing)
                    )
                    Text(
                        text = "Enviar descuento",
                        fontSize = 11.sp,
                        color = Color.White,
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(end = 40.dp)
                    )
                    IconButton(modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .graphicsLayer {
                            rotationX = rotationAngle
                            cameraDistance = 16 * density
                        },
                        onClick = {
                            isRotated.value = !isRotated.value
                            viewModel.postDescuentoMaterial(ArrayList<Solicitud>(list), context = context,serie,observaciones, sala = sala)
                        }
                    ) {
                        Icon(Icons.Filled.Send, contentDescription ="",tint = Color.White,)
                    }
                }
            }
        },
        backgroundColor = reds,
    )
}

@Composable
private fun alertDescuento(
    viewModel: DescuentoViewModel = hiltViewModel(),
){
    AnimatedVisibility(visible = viewModel.alertstate.value) {
        AlertDialog(
            onDismissRequest = {
            },
            title = null,
            text = null,
            buttons = {
                Column{
                    Row(Modifier.padding(all = 25.dp)){
                        if (viewModel.alertstatecolor.value){
                            Icon(
                                Icons.Filled.AddTask, "",
                                tint = Color.Green,
                                modifier = Modifier
                                    .align(Alignment.CenterVertically)
                                    .padding(horizontal = 10.dp)
                            )
                            Text(text = viewModel.textalert.value)
                        } else{
                            Icon(
                                Icons.Filled.Cancel, "",
                                tint = Color.Red,
                                modifier = Modifier
                                    .align(Alignment.CenterVertically)
                                    .padding(horizontal = 10.dp)
                            )
                            Text(text = viewModel.textalert.value)
                        }
                    }
                    if (viewModel.alertstatecolor.value){
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