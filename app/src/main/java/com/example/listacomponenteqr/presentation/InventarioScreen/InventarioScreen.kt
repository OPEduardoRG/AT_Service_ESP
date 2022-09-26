package com.example.listacomponenteqr.presentation.InventarioScreen

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
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
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
import com.example.listacomponenteqr.data.remote.dto.InventarioMov.InventarioSh
import com.example.listacomponenteqr.data.remote.dto.InventarioMov.Refacciones
import com.example.listacomponenteqr.data.remote.dto.RecibirMat.RecibirMat
import com.example.listacomponenteqr.data.remote.dto.SolicitudRefaccion.Solicitud
import com.example.listacomponenteqr.presentation.components.InfoEstatus
import com.example.listacomponenteqr.ui.theme.blackdark
import com.example.listacomponenteqr.ui.theme.blacktransp
import com.example.listacomponenteqr.ui.theme.reds
import com.example.listacomponenteqr.utils.BarcodeAnalyser
import com.example.listacomponenteqr.utils.Utils
import com.example.zitrocrm.screens.login.components.StringProgres
import com.google.common.util.concurrent.ListenableFuture
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun InventarioScreen(
    navController: NavController,
){
    val viewModel : InventarioViewModel = hiltViewModel()
    val drop = rememberSaveable { mutableStateOf(false) }
    val option = rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current
    var imputsearch by remember {
        mutableStateOf("")
    }
    var infoEstatus = remember{
        mutableStateOf(false)
    }
    val list = listOf<InventarioSh>(
        InventarioSh("Todo",1),
        InventarioSh("Correcto",2),
        InventarioSh("Revision",3),
        InventarioSh("RMA",4),
        InventarioSh("Desechos",5),
        InventarioSh("Nuevo",6)
        )
    val listdevolucion = remember { mutableStateListOf<InventarioSh>(
        InventarioSh("Correcto",2),
        InventarioSh("Revision",3),
        InventarioSh("RMA",4),
        InventarioSh("Desechos",5),
        InventarioSh("Nuevo",6)
    ) }
    var imputsearchDev by remember { mutableStateOf("")}
    var drop2 = remember { mutableStateOf(false)}
    var dropListDev = remember { mutableStateOf(true)}
    if(viewModel.delate.value){
        viewModel.listInventario = listOf<Refacciones>()
        viewModel.listInventarioDevolucion.clear()
        imputsearchDev = ""
        drop2.value = false
        drop.value = false
        imputsearch = ""
        viewModel.listPiezasInventario.clear()
        viewModel.delate.value = false
    }

    Scaffold(
        topBar = {
            TopAppBarInventario(navController)
        }
    ) {
        if(viewModel.a.value){
            LazyColumn(
                modifier = Modifier
                    .padding(top = 10.dp, start = 10.dp, end = 10.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(blacktransp)
            ) {
                item {
                    titleHomeInventario(
                        option,
                        viewModel
                    )
                  }
                if(option.value==false){
                    item {
                        OutlinedTextField(
                            enabled=false,
                            value = imputsearch,
                            onValueChange = {},
                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Ascii,
                                imeAction = ImeAction.Done
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 2.dp, horizontal = 10.dp)
                                .clickable {
                                    drop.value = !drop.value
                                },
                            label = { Text("Selecciona estatus del inventario") },
                            trailingIcon = {
                                IconButton(onClick = {
                                    drop.value = !drop.value
                                }) {
                                    Icon(Icons.Filled.ExpandLess, "contentDescription")
                                }
                            },
                            leadingIcon = {
                                IconButton(onClick = {
                                    infoEstatus.value = true
                                }) {
                                    Icon(Icons.Filled.Info, "contentDescription")
                                }
                            }
                        )
                        Spacer(modifier = Modifier.size(5.dp))
                    }
                    itemsIndexed(list) { index,item->
                        AnimatedVisibility(visible = drop.value) {
                            Card(
                                Modifier
                                    .padding(horizontal = 10.dp, vertical = 3.dp)
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable {
                                        imputsearch = item.string.toString()
                                        drop.value = false
                                        viewModel.getInvetarioMovil(
                                            item.int.toString(),
                                            context = context
                                        )
                                    }
                            ) {
                                Row() {
                                    Icon(Icons.Filled.HorizontalRule, "contentDescription", modifier = Modifier
                                        .align(CenterVertically)
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
                    item {
                        if(viewModel.listInventario.isNotEmpty()){
                            Divider(color = colorResource(id = R.color.graydark), thickness = 1.dp, modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp, vertical = 5.dp))
                            Spacer(modifier = Modifier.size(4.dp))
                            Card(
                                Modifier
                                    .padding(horizontal = 5.dp, vertical = 5.dp)
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                            ) {
                                Column(Modifier.padding(vertical = 2.dp)) {
                                    Row() {
                                        Text(
                                            text = "Código",
                                            style = MaterialTheme.typography.subtitle2,
                                            fontSize = 10.sp,
                                            modifier = Modifier
                                                .padding(horizontal = 4.dp, vertical = 7.dp)
                                                .fillMaxWidth(.27f)
                                        )
                                        Text(
                                            text = "Refacción",
                                            style = MaterialTheme.typography.subtitle2,
                                            fontSize = 10.sp,
                                            modifier = Modifier
                                                .padding(horizontal = 2.dp, vertical = 7.dp)
                                                .fillMaxWidth(.47f)
                                        )
                                        Text(
                                            text = "Cantidad",
                                            style = MaterialTheme.typography.subtitle2,
                                            fontSize = 10.sp,
                                            modifier = Modifier
                                                .padding(horizontal = 2.dp, vertical = 7.dp)
                                                .fillMaxWidth(.54f)
                                        )
                                        Text(
                                            text = "Estatus",
                                            style = MaterialTheme.typography.subtitle2,
                                            fontSize = 10.sp,
                                            modifier = Modifier
                                                .padding(horizontal = 2.dp, vertical = 7.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                    itemsIndexed(viewModel.listInventario){index,item->
                        val color by animateColorAsState(
                            when (item.estatus) {
                                "Correcto" -> Color.Green
                                "Revision" -> Color.Yellow
                                "RMA"-> colorResource(id = R.color.orange)
                                "Desechos"-> Color.Red
                                "Nuevo"-> Color.Blue
                                else -> Color.Gray
                            }
                        )
                        var drop by remember { mutableStateOf(false) }
                        var dropcam by remember { mutableStateOf(false) }
                        var cantidad by remember { mutableStateOf("") }
                        var seriemaquina by remember { mutableStateOf("") }
                        var serie_Qr by remember { mutableStateOf(0) }
                        val qr_val = Utils(context)
                        val focusManager = LocalFocusManager.current
                        val keyboardController = LocalSoftwareKeyboardController.current
                        Card(
                            Modifier
                                .padding(horizontal = 5.dp, vertical = 3.dp)
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .clickable {
                                    if (item.estatus == "Correcto"||item.estatus == "Nuevo") {
                                        drop = !drop
                                        dropcam = false
                                    }
                                }
                        ) {
                            Column(Modifier.padding(vertical = 2.dp)) {
                                Row() {
                                    Text(
                                        text = item.codigo.toString(),
                                        style = MaterialTheme.typography.subtitle2,
                                        fontSize = 10.sp,
                                        modifier = Modifier
                                            .padding(horizontal = 4.dp, vertical = 10.dp)
                                            .fillMaxWidth(.27f)
                                            .align(CenterVertically)
                                    )
                                    Text(
                                        text = item.nombre.toString(),
                                        style = MaterialTheme.typography.subtitle2,
                                        fontSize = 10.sp,
                                        modifier = Modifier
                                            .padding(horizontal = 2.dp, vertical = 10.dp)
                                            .fillMaxWidth(.46f)
                                            .align(CenterVertically)
                                    )
                                    Box(modifier = Modifier
                                        .align(CenterVertically)
                                        .fillMaxSize(.56f)) {
                                        Text(
                                            text = item.cantidad.toString(),
                                            style = MaterialTheme.typography.subtitle2,
                                            fontSize = 10.sp,
                                            modifier = Modifier
                                                .padding(start = 9.dp)
                                                .fillMaxWidth(.5f)
                                                .align(Center)
                                        )
                                    }
                                    Text(
                                        text = item.estatus.toString(),
                                        style = MaterialTheme.typography.subtitle2,
                                        fontSize = 10.sp,
                                        modifier = Modifier
                                            .padding(horizontal = 2.dp, vertical = 10.dp)
                                            .align(CenterVertically)
                                    )
                                }
                                Divider(color = color, thickness = 1.dp, modifier = Modifier.fillMaxWidth())
                                AnimatedVisibility(visible = drop && item.estatus=="Correcto") {
                                    Row() {
                                        Column() {
                                            OutlinedTextField(
                                                value = cantidad,
                                                onValueChange = {
                                                    if (qr_val.getValidationInt(it)){
                                                        cantidad = it
                                                    }
                                                    if( it.isBlank()){
                                                        cantidad = it
                                                    }else{
                                                        if (qr_val.getValidationInt(it)){
                                                            if(cantidad.toInt()>item.cantidad!!.toInt()){
                                                                cantidad = item.cantidad.toString()
                                                            }
                                                        }
                                                    }
                                                },
                                                keyboardOptions = KeyboardOptions.Default.copy(
                                                    keyboardType = KeyboardType.Number,
                                                    imeAction = ImeAction.Done
                                                ),
                                                keyboardActions = KeyboardActions(
                                                    onDone = {
                                                        keyboardController?.hide()
                                                    }
                                                ),
                                                modifier = Modifier
                                                    .padding(vertical = 2.dp, horizontal = 5.dp)
                                                    .fillMaxHeight(.5f)
                                                    .fillMaxWidth(.8f)
                                                    .clickable {
                                                        //drop.value = !drop.value
                                                    },
                                                label = { Text("Selecciona cantidad a descontar", fontSize = 10.sp) },
                                                leadingIcon = {
                                                    Icon(Icons.Filled.LocalHospital, "contentDescription")
                                                }
                                            )
                                            OutlinedTextField(
                                                value = seriemaquina,
                                                onValueChange = {
                                                    seriemaquina  = it
                                                    serie_Qr = 1
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
                                                    .padding(vertical = 2.dp, horizontal = 5.dp)
                                                    .fillMaxHeight(.5f)
                                                    .fillMaxWidth(.8f)
                                                    .clickable {
                                                        //drop.value = !drop.value
                                                    },
                                                label = { Text("Ingresa serie de la máquina", fontSize = 10.sp) },
                                                leadingIcon = {
                                                    IconButton(onClick = {
                                                        dropcam = !dropcam
                                                        focusManager.clearFocus()
                                                        keyboardController?.hide()
                                                    }) {
                                                        Icon(Icons.Filled.QrCode, "contentDescription")
                                                    }
                                                }
                                            )
                                            AnimatedVisibility(
                                                visible = dropcam,
                                            ) {
                                                Box(
                                                    modifier = Modifier
                                                        .height(250.dp)
                                                        .fillMaxWidth(.8f),
                                                    contentAlignment = Alignment.TopCenter
                                                ) {
                                                    val lifecycleOwner = LocalLifecycleOwner.current
                                                    var preview by remember { mutableStateOf<Preview?>(null) }
                                                    val mMediaPlayer = MediaPlayer.create(context, R.raw.bip)
                                                    Column(
                                                        modifier = Modifier
                                                            .fillMaxSize()
                                                    ) {
                                                        Card(
                                                            modifier = Modifier
                                                                .fillMaxWidth()
                                                                .height(282.dp)
                                                                .padding(10.dp)
                                                        ) {
                                                            AndroidView(factory = { AndroidViewContext ->
                                                                PreviewView(AndroidViewContext).apply {
                                                                    this.scaleType =
                                                                        PreviewView.ScaleType.FILL_CENTER
                                                                    layoutParams = ViewGroup.LayoutParams(
                                                                        ViewGroup.LayoutParams.MATCH_PARENT,
                                                                        ViewGroup.LayoutParams.MATCH_PARENT,
                                                                    )
                                                                    implementationMode =
                                                                        PreviewView.ImplementationMode.COMPATIBLE
                                                                }
                                                            },
                                                                modifier = Modifier.fillMaxWidth(),
                                                                update = { previewView ->
                                                                    val cameraSelector: CameraSelector =
                                                                        CameraSelector.Builder()
                                                                            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                                                                            .build()
                                                                    val cameraExecutors: ExecutorService =
                                                                        Executors.newSingleThreadExecutor()
                                                                    val cameraProviderFuture: ListenableFuture<ProcessCameraProvider> =
                                                                        ProcessCameraProvider.getInstance(context)

                                                                    cameraProviderFuture.addListener({
                                                                        preview = Preview.Builder().build().also {
                                                                            it.setSurfaceProvider(previewView.surfaceProvider)
                                                                        }
                                                                        val cameraProvider: ProcessCameraProvider =
                                                                            cameraProviderFuture.get()
                                                                        val barcodeAnalyser =
                                                                            BarcodeAnalyser { barcodes ->
                                                                                barcodes.forEach { barcode ->
                                                                                    barcode.rawValue?.let { barcodeValue ->
                                                                                        if (dropcam) {
                                                                                            serie_Qr = 0
                                                                                            seriemaquina = barcodeValue
                                                                                            mMediaPlayer.start()
                                                                                            dropcam = false
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                        val imageAnalysis: ImageAnalysis =
                                                                            ImageAnalysis.Builder()
                                                                                .setBackpressureStrategy(
                                                                                    ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST
                                                                                )
                                                                                .build()
                                                                                .also {
                                                                                    it.setAnalyzer(
                                                                                        cameraExecutors,
                                                                                        barcodeAnalyser
                                                                                    )
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
                                                                            Log.d(
                                                                                "",
                                                                                "CameraPreview: ${e.localizedMessage}"
                                                                            )
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
                                        Column(modifier = Modifier
                                            .padding(horizontal = 5.dp)
                                            .align(CenterVertically)
                                            .clip(RoundedCornerShape(5.dp))
                                            .clickable {
                                                viewModel.postDescuetoInventario(
                                                    idMaquina = seriemaquina,
                                                    codigo = item.codigo.toString(),
                                                    cantidad = cantidad,
                                                    n = serie_Qr.toString(),
                                                    context = context
                                                )
                                                cantidad = ""
                                                seriemaquina = ""
                                                drop = false
                                                //drop = !drop
                                            }
                                        ) {
                                            Box(modifier = Modifier
                                                .clip(RoundedCornerShape(15.dp))
                                                .background(colorResource(id = R.color.reds))
                                                .size(45.dp)
                                                .align(CenterHorizontally)
                                            ){
                                                Icon(Icons.Filled.Add, "contentDescription", modifier = Modifier.align(Center))
                                            }
                                            Text(
                                                text = "Descontar \n material",
                                                style = MaterialTheme.typography.subtitle2,
                                                fontSize = 9.sp,
                                                modifier = Modifier
                                                    .align(CenterHorizontally)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }else{
                    item {
                        OutlinedTextField(
                            enabled=false,
                            value = imputsearchDev,
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
                                .padding(vertical = 2.dp, horizontal = 10.dp)
                                .clickable {
                                    drop2.value = !drop2.value
                                },
                            label = { Text("Selecciona inventario a devolver") },
                            trailingIcon = {
                                IconButton(onClick = {
                                    drop2.value = !drop2.value
                                }) {
                                    Icon(Icons.Filled.ExpandLess, "contentDescription")
                                }
                            }, leadingIcon = {
                                IconButton(onClick = {
                                    infoEstatus.value = true
                                }) {
                                    Icon(Icons.Filled.Info, "contentDescription")
                                }
                            }
                        )
                        Spacer(modifier = Modifier.size(5.dp))
                    }
                    itemsIndexed(listdevolucion) { index,item->
                        AnimatedVisibility(visible = drop2.value) {
                            Card(
                                Modifier
                                    .padding(horizontal = 10.dp, vertical = 3.dp)
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable {
                                        imputsearchDev = item.string.toString()
                                        drop2.value = false
                                        viewModel.getInvetarioMovil(
                                            item.int.toString(),
                                            context = context
                                        )
                                        viewModel.listPiezasInventario.clear()
                                    }
                            ) {
                                Row() {
                                    Icon(Icons.Filled.HorizontalRule, "contentDescription", modifier = Modifier
                                        .align(CenterVertically)
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
                    item {
                        val isRotated = rememberSaveable { mutableStateOf(false) }
                        val rotationAngle by animateFloatAsState(
                            targetValue = if (isRotated.value) 360F else 0F,
                            animationSpec = tween(durationMillis = 500,easing = FastOutLinearInEasing)
                        )
                        var stringbtn by rememberSaveable { mutableStateOf("") }
                        if(viewModel.listPiezasInventario.isEmpty()){
                            stringbtn = "Devolver todo el material en "+imputsearchDev.lowercase()
                        }else{
                            stringbtn = "Devolver materiales de la lista en "+imputsearchDev.lowercase()
                        }
                        Spacer(modifier = Modifier.size(10.dp))
                        AnimatedVisibility(visible = viewModel.listPiezasInventario.isNotEmpty()||viewModel.listInventarioDevolucion.isNotEmpty()) {
                            Button(
                                onClick = {
                                    val listpost = mutableStateListOf<Solicitud>()
                                    isRotated.value = !isRotated.value
                                    if(viewModel.listPiezasInventario.isNotEmpty()){
                                        viewModel.listPiezasInventario.forEach {
                                            listpost.add(
                                                Solicitud(
                                                    codigo = it.codigo,
                                                    cantidad = it.cantidad,
                                                    desc = it.nombre,
                                                    estatus = it.estatus
                                                )
                                            )
                                        }
                                        viewModel.postDevolucion(
                                            solicitud= ArrayList<Solicitud>(listpost),
                                            context = context
                                        )
                                        viewModel.listPiezasInventario.clear()
                                    }else{
                                        viewModel.listInventarioDevolucion.forEach {
                                            listpost.add(
                                                Solicitud(
                                                    codigo = it.codigo,
                                                    cantidad = it.cantidad,
                                                    desc = it.nombre,
                                                    estatus = it.estatus
                                                )
                                            )
                                        }
                                        viewModel.postDevolucion(
                                            solicitud= ArrayList<Solicitud>(listpost),
                                            context = context
                                        )
                                        viewModel.listInventarioDevolucion.clear()
                                    }
                                    listpost.clear()
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
                                    text = stringbtn,
                                    fontSize = 11.sp,
                                    color = Color.White
                                )
                            }
                        }
                    }
                    item {
                        if(viewModel.listPiezasInventario.isNotEmpty()){
                            Spacer(modifier = Modifier.size(15.dp))
                            Divider(color = colorResource(id = R.color.graydark), thickness = 1.dp, modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp, vertical = 5.dp))
                            Spacer(modifier = Modifier.size(15.dp))

                            Card(
                                Modifier
                                    .padding(horizontal = 5.dp, vertical = 5.dp)
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable {
                                        dropListDev.value = !dropListDev.value
                                    }
                            ) {
                                Box(
                                    modifier =
                                    Modifier
                                        .fillMaxWidth()
                                ) {
                                    Text(
                                        text = "N°: " + viewModel.listPiezasInventario.size.toString(),
                                        style = MaterialTheme.typography.subtitle2,
                                        fontSize = 10.sp,
                                        modifier = Modifier
                                            .padding(horizontal = 10.dp, vertical = 10.dp)
                                            .align(Alignment.CenterStart)
                                    )
                                    Text(
                                        text = "Lista de materiales a devolver",
                                        style = MaterialTheme.typography.subtitle2,
                                        fontSize = 11.sp,
                                        modifier = Modifier
                                            .padding(horizontal = 10.dp, vertical = 10.dp)
                                            .align(Center)
                                    )
                                    Icon(
                                        Icons.Filled.TouchApp,
                                        contentDescription = "",
                                        tint = Color.White,
                                        modifier = Modifier
                                            .align(Alignment.CenterEnd)
                                            .padding(horizontal = 5.dp)
                                    )
                                }
                            }
                        }
                    }
                    itemsIndexed(viewModel.listPiezasInventario){ index, item ->
                        AnimatedVisibility(visible = dropListDev.value) {
                            Card(
                                Modifier
                                    .padding(horizontal = 5.dp, vertical = 3.dp)
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                            ) {
                                Column(Modifier.padding(vertical = 2.dp)) {
                                    Row() {
                                        Text(
                                            text = item.codigo.toString(),
                                            style = MaterialTheme.typography.subtitle2,
                                            fontSize = 10.sp,
                                            modifier = Modifier
                                                .padding(horizontal = 4.dp, vertical = 10.dp)
                                                .fillMaxWidth(.28f)
                                                .align(CenterVertically)
                                        )
                                        Text(
                                            text = item.nombre.toString(),
                                            style = MaterialTheme.typography.subtitle2,
                                            fontSize = 10.sp,
                                            modifier = Modifier
                                                .padding(horizontal = 2.dp, vertical = 10.dp)
                                                .fillMaxWidth(.35f)
                                                .align(CenterVertically)
                                        )
                                        Box(modifier = Modifier
                                            .align(CenterVertically)
                                            .fillMaxWidth(.7f)) {
                                            Text(
                                                text = "Cantidad: "+item.cantidad.toString(),
                                                style = MaterialTheme.typography.subtitle2,
                                                fontSize = 10.sp,
                                                modifier = Modifier
                                                    .padding(start = 9.dp)
                                                    .fillMaxWidth(.7f)
                                                    .align(Center)
                                            )
                                        }
                                        IconButton(onClick = {
                                            viewModel.listPiezasInventario.removeAt(index)
                                        }) {
                                            Icon(Icons.Filled.Delete, contentDescription = "")
                                        }
                                    }
                                    Divider(color = colorResource(R.color.reds), thickness = 2.dp, modifier = Modifier.fillMaxWidth())
                                }
                            }
                        }
                    }
                    item {
                        if(viewModel.listInventarioDevolucion.isNotEmpty()){
                            Spacer(modifier = Modifier.size(15.dp))
                            Divider(color = colorResource(id = R.color.graydark), thickness = 1.dp, modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp, vertical = 5.dp))
                            Spacer(modifier = Modifier.size(15.dp))
                            Card(
                                Modifier
                                    .padding(horizontal = 5.dp, vertical = 5.dp)
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                            ) {
                                Column(Modifier.padding(vertical = 2.dp)) {
                                    Row() {
                                        Text(
                                            text = "Código",
                                            style = MaterialTheme.typography.subtitle2,
                                            fontSize = 10.sp,
                                            modifier = Modifier
                                                .padding(horizontal = 4.dp, vertical = 7.dp)
                                                .fillMaxWidth(.28f)
                                        )
                                        Text(
                                            text = "Refacción",
                                            style = MaterialTheme.typography.subtitle2,
                                            fontSize = 10.sp,
                                            modifier = Modifier
                                                .padding(horizontal = 2.dp, vertical = 7.dp)
                                                .fillMaxWidth(.48f)
                                        )
                                        Text(
                                            text = "Cantidad",
                                            style = MaterialTheme.typography.subtitle2,
                                            fontSize = 10.sp,
                                            modifier = Modifier
                                                .padding(horizontal = 2.dp, vertical = 7.dp)
                                                .fillMaxWidth()
                                        )
                                    }
                                }
                            }
                        }
                    }
                    itemsIndexed(viewModel.listInventarioDevolucion){index,item->
                        val color by animateColorAsState(
                            when (item.estatus) {
                                "Correcto" -> Color.Green
                                "Revision" -> Color.Yellow
                                "RMA"-> colorResource(id = R.color.orange)
                                "Desechos"-> Color.Red
                                "Nuevo"-> Color.Blue
                                else -> Color.Gray
                            }
                        )
                        var cantidad by remember { mutableStateOf("") }
                        val qr_val = Utils(context)
                        val keyboardController = LocalSoftwareKeyboardController.current
                        Card(
                            Modifier
                                .padding(horizontal = 5.dp, vertical = 3.dp)
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                        ) {
                            Column(Modifier.padding(vertical = 2.dp)) {
                                Row() {
                                    Text(
                                        text = item.codigo.toString(),
                                        style = MaterialTheme.typography.subtitle2,
                                        fontSize = 10.sp,
                                        modifier = Modifier
                                            .padding(horizontal = 4.dp, vertical = 10.dp)
                                            .fillMaxWidth(.28f)
                                            .align(CenterVertically)
                                    )
                                    Text(
                                        text = item.nombre.toString(),
                                        style = MaterialTheme.typography.subtitle2,
                                        fontSize = 10.sp,
                                        modifier = Modifier
                                            .padding(horizontal = 2.dp, vertical = 10.dp)
                                            .fillMaxWidth(.35f)
                                            .align(CenterVertically)
                                    )
                                    OutlinedTextField(
                                        value = cantidad,
                                        onValueChange = {
                                            if (qr_val.getValidationInt(it)){
                                                cantidad = it
                                            }
                                            if(it.isBlank()){
                                                cantidad = it
                                            }else{
                                                if (qr_val.getValidationInt(it)){
                                                    if(cantidad!!.toInt()>item.cantidad!!.toInt()){
                                                        cantidad = item.cantidad.toString()
                                                    }
                                                }
                                            }
                                        },
                                        keyboardOptions = KeyboardOptions.Default.copy(
                                            keyboardType = KeyboardType.Number,
                                            imeAction = ImeAction.Done
                                        ),
                                        keyboardActions = KeyboardActions(
                                            onDone = {
                                                keyboardController?.hide()
                                            }
                                        ),
                                        modifier = Modifier
                                            .padding(vertical = 2.dp, horizontal = 5.dp)
                                            .scale(scaleY = 0.8F, scaleX = .9F)
                                        ,
                                        label = { Text("Selecciona cantidad a devolver", fontSize = 10.sp) },
                                        leadingIcon = {
                                            Text("N° ", fontSize = 12.sp)
                                        },
                                        /*trailingIcon = {
                                            //Text(" de: "+item.cantidad.toString(), fontSize = 12.sp)
                                            Icon(Icons.Filled.ConfirmationNumber, "contentDescription")
                                        }*/
                                    )
                                }
                                Row(modifier = Modifier.align(CenterHorizontally)) {
                                    Text(
                                        text = "Cantidad en inventario: ${item.cantidad}",
                                        style = MaterialTheme.typography.subtitle2,
                                        fontSize = 10.sp,
                                        modifier = Modifier
                                            .padding(horizontal = 5.dp, vertical = 2.dp)
                                            .fillMaxWidth(.50f)
                                            .align(CenterVertically)
                                    )
                                    Text(
                                        text = "Agregar a la lista",
                                        style = MaterialTheme.typography.subtitle2,
                                        fontSize = 10.sp,
                                        modifier = Modifier
                                            .padding(horizontal = 5.dp, vertical = 2.dp)
                                            .fillMaxWidth(.6f)
                                            .align(CenterVertically)
                                    )
                                    Icon(
                                        Icons.Filled.LocalHospital,
                                        contentDescription = "",
                                        modifier = Modifier.clickable {
                                            if(cantidad.isNotBlank()){
                                                viewModel.listPiezasInventario.add(
                                                    Refacciones(
                                                        item.codigo,
                                                        item.nombre,
                                                        cantidad,
                                                        item.estatus
                                                    )
                                                )
                                                cantidad = ""
                                                viewModel.listInventarioDevolucion.remove(Refacciones(item.codigo,item.nombre,item.cantidad,item.estatus))
                                            }
                                        }
                                    )
                                }
                                Divider(color = color, thickness = 1.dp, modifier = Modifier.fillMaxWidth())
                            }
                        }
                    }
                }
            }
        }
    }
    alertInventarioVan(listdevolucion)
    InfoEstatus(
        infoEstatus,
        "Informacion de los estatus.",
        "-Correcto: Material usado que se puede utilizar para otra instalación.\n\n" +
                "-Revision: Material que esta dañado pero tiene reparacion.\n\n" +
                "-RMA: Material que requiere aprobacion para devolucion.\n\n" +
                "-Desechos: Material que ya no sirve y va directo a destrucción.\n\n" +
                "-Nuevo: Material que esta recien fabricado y esta listo para usarse.\n"
    )
}

@Composable
private fun TopAppBarInventario(
    navController: NavController,
) {
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
            }
        },
        backgroundColor = reds,
    )
}
@Composable
private fun titleHomeInventario(
    devol_inventario : MutableState<Boolean>,
    viewModel : InventarioViewModel
){
    Box(modifier = Modifier
        .padding(0.dp, 0.dp, 0.dp, 5.dp)
        .clip(RoundedCornerShape(10.dp))
        .background(blackdark)
        .fillMaxWidth()
        .padding(5.dp),
    ) {
        Column(Modifier.align(Center)) {
            Image(
                painter = painterResource(R.drawable.inventario_icon),
                contentDescription = "",
                modifier = Modifier
                    .padding(0.dp)
                    .size(50.dp)
                    .align(CenterHorizontally)
            )
            Text(
                text = "INVENTARIO",
                style = MaterialTheme.typography.subtitle2,
                modifier = Modifier.align(CenterHorizontally))
        }
        if(devol_inventario.value){
            Box(modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(10.dp)) {
                IconButton(onClick = {
                    devol_inventario.value = false
                    viewModel.delate.value = true
                }) {
                    Column() {
                        Icon(Icons.Filled.FormatListBulleted, contentDescription = "QR",modifier = Modifier.align(CenterHorizontally))
                        Text(text = "Ver inventario",fontSize = 9.sp,modifier = Modifier.align(CenterHorizontally))
                    }
                }
            }
        }else{
            Box(modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(10.dp)) {
                IconButton(onClick = {
                    devol_inventario.value = true
                    viewModel.delate.value = true
                }) {
                    Column() {
                        Icon(Icons.Filled.Replay, contentDescription = "QR",modifier = Modifier.align(CenterHorizontally))
                        Text(text = "Devolver material",fontSize = 9.sp,modifier = Modifier.align(CenterHorizontally))
                    }
                }
            }
        }
    }
}
@Composable
private fun alertInventarioVan(
    list : MutableList<InventarioSh>,
    viewModel: InventarioViewModel = hiltViewModel(),
){
    val context = LocalContext.current
    var Status_Material by remember { mutableStateOf("") }
    var Status_Material_ID by remember { mutableStateOf("") }
    val drop2 = remember { mutableStateOf(false) }
    AnimatedVisibility(visible = viewModel.alertstate.value==1) {
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

    AnimatedVisibility(visible = viewModel.alertstate.value==2) {
        AlertDialog(
            onDismissRequest = {
            },
            title = null,
            text = null,
            buttons = {
                Column{
                    Row(Modifier.padding(5.dp)){
                        Icon(Icons.Filled.CheckCircle,"", modifier = Modifier.padding(horizontal = 12.dp))
                        Text(
                            text =  StringProgres.value +"\n¿Desea agregar este material a algún estatus?",
                            style = MaterialTheme.typography.subtitle2,
                            fontSize = 12.sp,
                            modifier = Modifier
                                .align(CenterVertically)
                        )
                    }
                    OutlinedTextField(
                            enabled=false,
                            value = Status_Material,
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
                                .padding(vertical = 1.dp, horizontal = 10.dp)
                                .clickable {
                                    drop2.value = !drop2.value
                                },
                            label = { Text("Selecciona estatus del material") },
                            trailingIcon = {
                                IconButton(onClick = {
                                    drop2.value = !drop2.value
                                }) {
                                    Icon(Icons.Filled.ExpandLess, "contentDescription")
                                }
                            }
                        )
                        Spacer(modifier = Modifier.size(10.dp))
                    Row() {
                        Box(modifier = Modifier
                            .fillMaxWidth(.5F)
                            .height(60.dp)
                            .padding(5.dp)
                            .clip(shape = RoundedCornerShape(15.dp))
                            .background(colorResource(id = R.color.reds))
                            .clickable {
                                if (Status_Material.isNotBlank()) {
                                    viewModel.postRevisionVan(context)
                                    Status_Material = ""
                                    Status_Material_ID = ""
                                }
                            }
                        ){
                            Text(
                                text = "SI",
                                style = MaterialTheme.typography.subtitle2,
                                color = Color.White,
                                fontSize = 15.sp,
                                modifier = Modifier
                                    .align(Center)
                            )
                        }
                        Box(modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                            .padding(5.dp)
                            .clip(shape = RoundedCornerShape(15.dp))
                            .background(colorResource(id = R.color.reds))
                            .clickable {
                                viewModel.alertstate.value = 0
                                Status_Material = ""
                                Status_Material_ID = ""
                                viewModel.getInvetarioMovil("1", context)
                            }
                        ){
                            Text(
                                text = "NO",
                                style = MaterialTheme.typography.subtitle2,
                                color = Color.White,
                                fontSize = 15.sp,
                                modifier = Modifier
                                    .align(Center)
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
    AnimatedVisibility(visible = drop2.value) {
        AlertDialog(
            onDismissRequest = {
            },
            title = null,
            text = null,
            buttons = {
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(colorResource(id = R.color.reds))) {
                    Text(
                        text = "Selecciona el Estatus.",
                        style = MaterialTheme.typography.subtitle2,
                        color = Color.White,
                        fontSize = 15.sp,
                        modifier = Modifier
                            .padding(vertical = 13.dp)
                            .align(CenterHorizontally)
                    )
                }
                Spacer(modifier = Modifier.size(15.dp))
                list.forEachIndexed(){ index,item->
                    AnimatedVisibility(visible = drop2.value) {
                        Card(
                            Modifier
                                .padding(horizontal = 10.dp, vertical = 3.dp)
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .clickable {
                                    Status_Material = item.string.toString()
                                    Status_Material_ID = item.int.toString()
                                    drop2.value = false
                                }
                        ) {
                            Row() {
                                Icon(Icons.Filled.HorizontalRule, "contentDescription", modifier = Modifier
                                    .align(CenterVertically)
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
                Spacer(modifier = Modifier.size(15.dp))
            },
            properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = false),
            modifier = Modifier.clip(RoundedCornerShape(25.dp)),
            shape = RoundedCornerShape(18.dp)
        )
    }
}