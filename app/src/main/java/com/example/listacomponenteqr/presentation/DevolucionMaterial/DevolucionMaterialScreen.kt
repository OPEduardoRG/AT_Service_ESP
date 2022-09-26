package com.example.listacomponenteqr.presentation.DevolucionMaterial

import android.media.MediaPlayer
import android.util.Log
import android.view.ViewGroup
import android.widget.Toast
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
import com.example.listacomponenteqr.common.CreateNotification
import com.example.listacomponenteqr.data.remote.dto.InventarioMov.InventarioSh
import com.example.listacomponenteqr.data.remote.dto.SolicitudRefaccion.Refacciones
import com.example.listacomponenteqr.data.remote.dto.SolicitudRefaccion.Solicitud
import com.example.listacomponenteqr.presentation.components.InfoEstatus
import com.example.listacomponenteqr.ui.theme.blackdark
import com.example.listacomponenteqr.ui.theme.blacktransp
import com.example.listacomponenteqr.ui.theme.graydark
import com.example.listacomponenteqr.ui.theme.reds
import com.example.listacomponenteqr.utils.BarcodeAnalyser
import com.example.listacomponenteqr.utils.Utils
import com.example.zitrocrm.screens.login.components.StringProgres
import com.example.zitrocrm.screens.login.components.progressBar
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.delay
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/*
 * Created by Brian Fernando Mtz on 07-2022.
 */

@Composable
fun DevolucionMaterialScreen(
    navController: NavController,
) {
    val viewModel : DevolucionViewModel = hiltViewModel()
    val drop = rememberSaveable { mutableStateOf(false) }
    val drop2 = rememberSaveable { mutableStateOf(false) }
    val dropcam = rememberSaveable { mutableStateOf(false) }
    val imputsearch = rememberSaveable { mutableStateOf("") }
    val descrip = rememberSaveable { mutableStateOf("") }
    val cantidad = rememberSaveable { mutableStateOf("") }
    val granel = rememberSaveable { mutableStateOf("") }
    val lista_soli = rememberSaveable { mutableStateOf(true) }
    val context = LocalContext.current
    val qr_val = Utils(context)
    val listTipo = remember { mutableStateListOf<InventarioSh>(
        InventarioSh("Correcto",2),
        InventarioSh("Revision",3),
        InventarioSh("RMA",4),
        InventarioSh("Desechos",5),
        InventarioSh("Nuevo",6)
    ) }
    var tipotext by rememberSaveable() { mutableStateOf("") }
    var tipoid by rememberSaveable() { mutableStateOf("") }
    var infoMenu = rememberSaveable() { mutableStateOf(false) }
    val comentarios = remember {
        mutableStateOf("")
    }
    if(viewModel.delate.value) {
        viewModel.listDevolucion.clear()
        imputsearch.value = ""
        descrip.value = ""
        cantidad.value = ""
        granel.value = ""
        drop.value = false
        dropcam.value = false
        viewModel.codigo.clear()
        viewModel.delate.value = false
        tipotext = ""
        comentarios.value = ""
    }
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
            viewModel.getCS(imputsearch.value,context)
            drop.value = true
        }
        if(qr_val.getValidationComponent2(imputsearch.value)){
            viewModel.getvalidationQrMaterial(imputsearch.value,context)
        }
    })
    /*val createNotification = CreateNotification(
        context,
        "Tienes un folio nuevo",
        "Atiende el folio"
    )
    createNotification.showNotification()
    Toast.makeText(context, "Confirmed", Toast.LENGTH_SHORT).show()*/
    Scaffold(
        topBar = {
            TopAppBarDevolucion(navController,viewModel.listDevolucion,/*tipotext,*/comentarios.value)
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
                        titleHomeDevolucion()
                    }
                    item {
                        OutlinedTextField(
                            value = comentarios.value,
                            onValueChange = {
                                comentarios.value = it
                            },
                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Ascii,
                                imeAction = ImeAction.Done
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 2.dp, horizontal = 10.dp),
                            label = { Text("Comentarios") },
                            leadingIcon ={
                                IconButton(onClick = {
                                    drop.value = false
                                    dropcam.value = !dropcam.value
                                },) {
                                    Icon(Icons.Filled.Comment, "contentDescription")
                                }
                            }
                        )
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
                                        viewModel.getCS(imputsearch.value,context)
                                    }
                                    if(qr_val.getValidationComponent2(imputsearch.value)){
                                        viewModel.getvalidationQrMaterial(imputsearch.value,context)
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
                                    drop.value = !drop.value
                                }) {
                                    Icon(Icons.Filled.ExpandLess, "contentDescription")
                                }
                            },
                            leadingIcon ={
                                IconButton(onClick = {
                                    drop.value = false
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
                        AnimatedVisibility(
                            visible = dropcam.value,
                        ) {
                            Box(modifier = Modifier
                                .height(250.dp),
                                contentAlignment = Alignment.TopCenter
                            ) {
                                val lifecycleOwner = LocalLifecycleOwner.current
                                var preview by remember { mutableStateOf<Preview?>(null) }
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
                                                                    descrip.value = ""
                                                                    imputsearch.value = barcodeValue
                                                                    mMediaPlayer.start()
                                                                    if(qr_val.getValidationComponent2(imputsearch.value)){
                                                                        viewModel.getvalidationQrMaterial(imputsearch.value,context)
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
                    item {
                        OutlinedTextField(
                            enabled=false,
                            value = tipotext,
                            onValueChange = {},
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
                                .padding(vertical = 2.dp, horizontal = 10.dp)
                                .clickable {
                                    drop2.value = !drop2.value
                                },
                            label = { Text("Seleccioná el estatus del material") },
                            trailingIcon = {
                                IconButton(onClick = {
                                    drop2.value = !drop2.value
                                }) {
                                    Icon(Icons.Filled.ExpandLess, "contentDescription")
                                }
                            }, leadingIcon = {
                                IconButton(onClick = {
                                    infoMenu.value = true
                                    //drop2.value = !drop2.value
                                }) {
                                    Icon(Icons.Filled.Info, "contentDescription")
                                }
                            }
                        )
                        Spacer(modifier = Modifier.size(5.dp))
                    }
                    itemsIndexed(listTipo) { index, item->
                        AnimatedVisibility(visible = drop2.value) {
                            Card(
                                Modifier
                                    .padding(horizontal = 10.dp, vertical = 3.dp)
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable {
                                        tipotext = item.string.toString()
                                        tipoid = item.int.toString()
                                        drop2.value = false
                                    }
                            ) {
                                Row() {
                                    Icon(Icons.Filled.HorizontalRule, "contentDescription", modifier = Modifier
                                        .align(Alignment.CenterVertically)
                                        .padding(horizontal = 5.dp))
                                    Text(
                                        text = item.string.toString(),
                                        style = MaterialTheme.typography.subtitle2,
                                        fontSize = 12.sp,
                                        modifier = Modifier
                                            .padding(horizontal = 10.dp, vertical = 15.dp)
                                    )
                                }
                            }
                        }
                    }
                    val isValidate by derivedStateOf {
                        imputsearch.value.isNotBlank()
                                && descrip.value.isNotBlank()
                                && cantidad.value.isNotBlank()
                                && tipotext.isNotBlank()
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
                                viewModel.listDevolucion.add(
                                    Solicitud(
                                        codigo = imputsearch.value,
                                        cantidad = cantidad.value,
                                        desc = descrip.value,
                                        estatus = tipotext
                                    )
                                )
                                tipotext = ""
                                imputsearch.value = ""
                                cantidad.value = ""
                                descrip.value = ""
                                granel.value = ""
                                viewModel.codigo.clear()
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
                    if (viewModel.listDevolucion.isNotEmpty()) {
                        item {
                            Box(
                                modifier =
                                Modifier
                                    .padding(horizontal = 10.dp, vertical = 5.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .fillMaxWidth()
                                    .background(graydark)
                                    .clickable {
                                        lista_soli.value = !lista_soli.value
                                    }
                            ) {
                                Text(
                                    text = "N°: " + viewModel.listDevolucion.size.toString(),
                                    style = MaterialTheme.typography.subtitle2,
                                    fontSize = 12.sp,
                                    modifier = Modifier
                                        .padding(horizontal = 10.dp, vertical = 10.dp)
                                        .align(Alignment.CenterStart)
                                )
                                Text(
                                    text = "Lista de materiales a devolver",
                                    style = MaterialTheme.typography.subtitle2,
                                    fontSize = 15.sp,
                                    modifier = Modifier
                                        .padding(horizontal = 10.dp, vertical = 10.dp)
                                        .align(Alignment.Center)
                                )
                                Icon(
                                    Icons.Filled.TouchApp,
                                    contentDescription = "",
                                    tint = Color.White,
                                    modifier = Modifier
                                        .align(Alignment.CenterEnd)
                                        .padding(horizontal = 10.dp)
                                )
                            }
                        }
                    }
                    itemsIndexed(viewModel.listDevolucion) { index, item ->
                        AnimatedVisibility(visible = lista_soli.value ) {
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
                                        text = "Código: " + item.codigo.toString() + "\nDescripción: " + item.desc.toString() + "\nCantidad: " + item.cantidad.toString()+"\nEstatus: "+item.estatus.toString(),
                                        style = MaterialTheme.typography.subtitle2,
                                        fontSize = 12.sp,
                                        modifier = Modifier
                                            .fillMaxWidth(.85f)
                                            .padding(horizontal = 10.dp, vertical = 15.dp)
                                    )
                                    IconButton(modifier = Modifier.align(Alignment.CenterVertically),onClick = {
                                        isRotated.value = !isRotated.value
                                        viewModel.listDevolucion.removeAt(index)
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
    alertDevolucion(viewModel.listDevolucion)
    InfoEstatus(
        infoMenu,
        "Informacion de los estatus.",
        "-Correcto: Material usado que se puede utilizar para otra instalación.\n\n" +
            "-Revision: Material que esta dañado pero tiene reparacion.\n\n" +
            "-RMA: Material que requiere aprobacion para devolucion.\n\n" +
            "-Desechos: Material que ya no sirve y va directo a destrucción.\n\n" +
            "-Nuevo: Material que esta recien fabricado y esta listo para usarse.\n"
    )
}
@Composable
private fun titleHomeDevolucion(){
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
            painter = painterResource(R.drawable.devolucion_icon),
            contentDescription = "",
            modifier = Modifier
                .padding(0.dp)
                .size(50.dp)
        )
        Text(
            text = "DEVOLUCIÓN DE MATERIAL",
            style = MaterialTheme.typography.subtitle2,
            modifier = Modifier.align(Alignment.CenterHorizontally))
    }
}
@Composable
private fun TopAppBarDevolucion(
    navController: NavController,
    list: MutableList<Solicitud>,
    //tipotext:String,
    comentarios : String
) {
    val viewModel : DevolucionViewModel = hiltViewModel()
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
                            &&comentarios.isNotBlank()
                }
                if(isValidate){
                    val isRotated = rememberSaveable { mutableStateOf(false) }
                    val rotationAngle by animateFloatAsState(
                        targetValue = if (isRotated.value) 360F else 0F,
                        animationSpec = tween(durationMillis = 500, easing = FastOutLinearInEasing)
                    )
                    Text(
                        text = "Enviar devolución",
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
                            viewModel.postDevolucion(
                                //estatus = tipotext,
                                solicitud=ArrayList<Solicitud>(list),
                                comentarios = comentarios,
                                context = context
                            )
                        }
                    ) {
                        Icon(Icons.Filled.Send, contentDescription ="",tint = Color.White)
                    }
                }
            }
        },
        backgroundColor = reds,
    )
}

@Composable
private fun alertDevolucion(
    listDevolucion : MutableList<Solicitud>,
    viewModel: DevolucionViewModel = hiltViewModel(),
){
    val context = LocalContext.current
    AnimatedVisibility(visible = viewModel.alertstate.value==1) {
        AlertDialog(
            onDismissRequest = {
            },
            title = null,
            text = null,
            buttons = {
                val color by animateColorAsState(
                    when (viewModel.alertstatecolor.value) {
                        true -> Color.Green
                        false ->Color.Red
                    }
                )
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
                    Divider(color = color, thickness = 3.dp)
                }
            },
            properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = false),
            modifier = Modifier.clip(RoundedCornerShape(25.dp)),
            shape = RoundedCornerShape(18.dp)
        )
    }
    AnimatedVisibility(visible = viewModel.alertstate.value==2) {
        AlertDialog(
            onDismissRequest = {
            },
            title = null,
            text = null,
            buttons = {
                Column{
                    Row(Modifier.padding(all = 25.dp)){
                        Icon(Icons.Filled.CheckCircle,"", modifier = Modifier.padding(horizontal = 12.dp))
                        Text(
                            text =  StringProgres.value +"\n\n¿Desea solicitar este material?",
                            style = MaterialTheme.typography.subtitle2,
                            fontSize = 12.sp,
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                        )
                    }
                    Row() {
                        Box(modifier = Modifier
                            .fillMaxWidth(.5F)
                            .height(60.dp)
                            .padding(5.dp)
                            .clip(shape = RoundedCornerShape(15.dp))
                            .background(colorResource(id = R.color.reds))
                            .clickable {
                                viewModel.solicitarNuevoMaterialdeDevolucion(
                                    ArrayList<Solicitud>(
                                        listDevolucion
                                    ), context = context
                                )
                            }
                        ){
                            Text(
                                text = "SI",
                                style = MaterialTheme.typography.subtitle2,
                                color = Color.White,
                                fontSize = 15.sp,
                                modifier = Modifier
                                    .align(Alignment.Center)
                            )
                        }
                        Box(modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                            .padding(5.dp)
                            .clip(shape = RoundedCornerShape(15.dp))
                            .background(colorResource(id = R.color.reds))
                            .clickable {
                                viewModel.delate.value = true
                                viewModel.alertstate.value = 0
                            }
                        ){
                            Text(
                                text = "NO",
                                style = MaterialTheme.typography.subtitle2,
                                color = Color.White,
                                fontSize = 15.sp,
                                modifier = Modifier
                                    .align(Alignment.Center)
                            )
                        }
                    }
                }
            },
            properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = false),
            modifier = Modifier.clip(RoundedCornerShape(25.dp)),
            shape = RoundedCornerShape(18.dp)
        )
    }

}

