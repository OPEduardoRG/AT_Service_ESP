package com.example.listacomponenteqr.presentation.solicitud_pedido

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.media.MediaPlayer
import android.util.Log
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
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
import androidx.compose.ui.Alignment.Companion.CenterEnd
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterStart
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
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
import com.example.listacomponenteqr.data.remote.dto.SolicitudRefaccion.Refacciones
import com.example.listacomponenteqr.data.remote.dto.SolicitudRefaccion.RegionesEsp
import com.example.listacomponenteqr.data.remote.dto.SolicitudRefaccion.Salas
import com.example.listacomponenteqr.data.remote.dto.SolicitudRefaccion.Solicitud
import com.example.listacomponenteqr.ui.theme.blackdark
import com.example.listacomponenteqr.ui.theme.blacktransp
import com.example.listacomponenteqr.ui.theme.graydark
import com.example.listacomponenteqr.ui.theme.reds
import com.example.listacomponenteqr.utils.BarcodeAnalyser
import com.example.listacomponenteqr.utils.Utils
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.delay
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.collections.ArrayList

/**
 * Created by Brian Fernando Mtz on 07/2022.
 */
@SuppressLint("UnrememberedMutableState")
@Composable
fun SolicitudPedidoScreen(
    viewModel: SolicitudViewModel = hiltViewModel(),
    navController: NavController,
) {
    val arraydrop = mutableStateListOf<Boolean>(false,false,false,false,true)
    val inputRegion = rememberSaveable() { mutableStateOf("") }
    val inputSala = rememberSaveable() { mutableStateOf("") }
    val inputSalaID = rememberSaveable() { mutableStateOf("") }
    val inputSubcentro = rememberSaveable() { mutableStateOf("") }
    val almacenid = rememberSaveable() { mutableStateOf("") }
    val imputsearch = rememberSaveable() { mutableStateOf("") }
    val descrip = rememberSaveable() { mutableStateOf("") }
    val granel = rememberSaveable() { mutableStateOf("") }
    val cantidad = rememberSaveable() { mutableStateOf("") }
    var dropcam = rememberSaveable { mutableStateOf(false) }
    var dropcam2 = rememberSaveable { mutableStateOf(false) }
    var qr_imput = rememberSaveable { mutableStateOf("") }
    var qr_imput_componente = rememberSaveable { mutableStateOf("") }
    val fecha = remember{mutableStateOf("")}
    val hora = remember{ mutableStateOf("")}
    val context = LocalContext.current
    if(viewModel.delate.value) {
        viewModel.listSolicitud.clear()
        inputRegion.value = ""
        inputSala.value = ""
        inputSalaID.value = ""
        inputSubcentro.value = ""
        almacenid.value = ""
        imputsearch.value = ""
        descrip.value = ""
        granel.value = ""
        cantidad.value = ""
        viewModel.codigo = mutableStateListOf<Refacciones>()
        viewModel.salasxRegion = mutableStateListOf<Salas>()
        qr_imput.value = ""
        qr_imput_componente.value = ""
        fecha.value = ""
        hora.value = ""
        viewModel.delate.value = false
    }
    val regionesEspana = rememberSaveable{
        listOf<RegionesEsp>(
            RegionesEsp("30","Alava"),
            RegionesEsp("36","Albacete"),
            RegionesEsp("7","Alicante"),
            RegionesEsp("5","Almeria"),
            RegionesEsp("37","Asturias"),
            RegionesEsp("12","Avila"),
            RegionesEsp("13","Badajoz"),
            RegionesEsp("31","Baleares"),
            RegionesEsp("40","Barcelona"),
            RegionesEsp("1","Burgos"),
            RegionesEsp("17","Caceres"),
            RegionesEsp("19","Cadiz"),
            RegionesEsp("34","Cantabria"),
            RegionesEsp("51","Castellon"),
            RegionesEsp("52","Ceuta"),
            RegionesEsp("46","Ciudad Real"),
            RegionesEsp("15","Cordoba"),
            RegionesEsp("44","Cuenca"),
            RegionesEsp("50","Gerona"),
            RegionesEsp("18","Granada"),
            RegionesEsp("28","Guadalajara"),
            RegionesEsp("32","Guipuzcoa"),
            RegionesEsp("33","Huelva"),
            RegionesEsp("27","Huesca"),
            RegionesEsp("14","Jaen"),
            RegionesEsp("38","La Coruña"),
            RegionesEsp("21","Las Palmas"),
            RegionesEsp("43","Leon"),
            RegionesEsp("41","Lerida"),
            RegionesEsp("35","Logroño"),
            RegionesEsp("53","Lugo"),
            RegionesEsp("6","Madrid"),
            RegionesEsp("2","Malaga"),
            RegionesEsp("42","Melilla"),
            RegionesEsp("3","Murcia"),
            RegionesEsp("23","Navarra"),
            RegionesEsp("29","no-region-0004"),
            RegionesEsp("49","Orense"),
            RegionesEsp("4","Palencia"),
            RegionesEsp("39","Pontevedra"),
            RegionesEsp("16","Salamanca"),
            RegionesEsp("24","Santa Cruz Tenerife"),
            RegionesEsp("22","Segovia"),
            RegionesEsp("20","Sevilla"),
            RegionesEsp("45","Soria"),
            RegionesEsp("8","Tarragona"),
            RegionesEsp("26","Teruel"),
            RegionesEsp("48","Toledo"),
            RegionesEsp("11","Valencia"),
            RegionesEsp("9","Valladolid"),
            RegionesEsp("10","Vizcaya"),
            RegionesEsp("47","Zamora"),
            RegionesEsp("25","Zaragoza"),
        )
    }
    val mYear: Int
    val mMonth: Int
    val mDay: Int
    val mCalendar = Calendar.getInstance()
    val mHour = mCalendar.get(Calendar.HOUR_OF_DAY)
    val mMinute = mCalendar.get(Calendar.HOUR_OF_DAY)
    mYear = mCalendar.get(Calendar.YEAR)
    mMonth = mCalendar.get(Calendar.MONTH)
    mDay = mCalendar.get(Calendar.DAY_OF_MONTH)
    mCalendar.time = Date()
    val mDatePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
            var mount = ""
            var day = ""
            if(mMonth<10){ mount = "0${mMonth+1}" }else{ mount = "${mMonth+1}" }
            if(mDayOfMonth<10){ day = "0${mDayOfMonth}" }else{ day = "${mDayOfMonth}" }
            fecha.value = "$mYear-${mount}-$day"
        }, mYear, mMonth, mDay
    )
    val mHourPickerDialog = TimePickerDialog(
        context,
        {_, mHour : Int, mMinute: Int ->
            if(mHour<10){
                hora.value = "0$mHour:00"
            }else{
                hora.value = "$mHour:00"
            }
        }, mHour, mMinute, true
    )

    LaunchedEffect(key1 = imputsearch.value, block = {
        if (imputsearch.value.isBlank()) return@LaunchedEffect
        delay(2000)
        if (descrip.value.isEmpty()){
            viewModel.getCodSolicitud(imputsearch.value,context)
            arraydrop[0] = true
        }
    })
    Scaffold(
        topBar = {
            CustomTopAppBarSolicitud(
                navController,
                viewModel.listSolicitud,
                inputSalaID.value,
                almacenid.value,
                fecha.value,
                hora.value,
            )
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
                    item {
                        titleHome(viewModel)
                    }
                    if(viewModel.`QR-Manual`.value) {
                        item {
                            OutlinedTextField(
                                enabled = false,
                                value = inputRegion.value,
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
                                        arraydrop[1] = !arraydrop[1]
                                        arraydrop[2] = false
                                        //dropRegion.value = !dropRegion.value
                                        //dropSala.value = false
                                        arraydrop[3] = false
                                        //dropSubCentro.value = false
                                    },
                                label = { Text("Selecciona la región") },
                                trailingIcon = {
                                    IconButton(onClick = {
                                        arraydrop[1] = !arraydrop[1]
                                        arraydrop[2] = false
                                        //dropRegion.value = !dropRegion.value
                                        //dropSala.value = false
                                        arraydrop[3] = false
                                        //dropSubCentro.value = false
                                    }) {
                                        Icon(Icons.Filled.ExpandLess, "contentDescription")
                                    }
                                }
                            )
                        }
                        if (arraydrop[1]) {
                            itemsIndexed(regionesEspana) { index, item ->
                                Card(
                                    Modifier
                                        .padding(horizontal = 10.dp, vertical = 3.dp)
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(5.dp))
                                        .clickable {
                                            inputRegion.value = item.nombre.toString()
                                            arraydrop[1] =false
                                            //dropRegion.value = false
                                            viewModel.getSalas(item.regionidx.toString())
                                            inputSala.value = ""
                                        }
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .align(CenterHorizontally)
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
                                        arraydrop[2] = !arraydrop[2]
                                        //dropSala.value = !dropSala.value
                                        arraydrop[1] = false
                                        //dropRegion.value = false
                                        arraydrop[3] = false
                                        //dropSubCentro.value = false
                                    },
                                label = { Text("Selecciona la sala") },
                                trailingIcon = {
                                    IconButton(onClick = {
                                        arraydrop[2] = !arraydrop[2]
                                        //dropSala.value = !dropSala.value
                                        arraydrop[1] = false
                                        //dropRegion.value = false
                                        arraydrop[3] = false
                                        //dropSubCentro.value = false
                                    }) {
                                        Icon(Icons.Filled.ExpandLess, "contentDescription")
                                    }
                                }
                            )
                        }
                        item {
                            AnimatedVisibility(
                                visible = arraydrop[2]
                            ) {
                                if (viewModel.salasxRegion.isEmpty()) {
                                    Card(
                                        Modifier
                                            .padding(horizontal = 10.dp, vertical = 3.dp)
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(10.dp))
                                    ) {
                                        Text(
                                            text = "No hay resultados.",
                                            style = MaterialTheme.typography.subtitle2,
                                            fontSize = 12.sp,
                                            modifier = Modifier
                                                .align(CenterHorizontally)
                                                .padding(horizontal = 10.dp, vertical = 15.dp)
                                        )
                                    }
                                }
                            }
                        }
                        if (arraydrop[2]) {
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
                                            arraydrop[2] = false
                                            //dropSala.value = false
                                            arraydrop[3] = false
                                            //dropSubCentro.value = false
                                        }
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .align(CenterHorizontally)
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
                                enabled = false,
                                value = inputSubcentro.value,
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
                                        arraydrop[3] = !arraydrop[3]
                                        //dropSubCentro.value = !dropSubCentro.value
                                        arraydrop[2] = false
                                        //dropSala.value = false
                                        arraydrop[1] = false
                                        //dropRegion.value = false
                                    },
                                label = { Text("Selecciona el almacen a solicitar el pedido") },
                                trailingIcon = {
                                    Icon(Icons.Filled.ExpandLess, "contentDescription")
                                }
                            )
                        }
                        if(arraydrop[3]){
                            itemsIndexed(viewModel.subcentros){index,item->
                                Card(
                                    Modifier
                                        .padding(horizontal = 10.dp, vertical = 3.dp)
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(5.dp))
                                        .background(color = Color.Transparent)
                                        .clickable {
                                            inputSubcentro.value = item.nombre.toString()
                                            almacenid.value = item.idSub.toString()
                                            arraydrop[3] = false
                                            //dropSubCentro.value = false
                                        }
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .align(CenterHorizontally)
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
                            Row() {
                                OutlinedTextField(
                                    enabled = false,
                                    value = fecha.value /*inputSubcentro.value*/,
                                    onValueChange = {
                                    },
                                    keyboardOptions = KeyboardOptions.Default.copy(
                                        keyboardType = KeyboardType.Ascii,
                                        imeAction = ImeAction.Done
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth(.49f)
                                        .padding(vertical = 5.dp, horizontal = 10.dp)
                                        .clickable {
                                            mDatePickerDialog.show()
                                        },
                                    label = { Text("Fecha entrega estimada") },
                                    trailingIcon = {
                                        Icon(Icons.Filled.ExpandLess, "contentDescription")
                                    }
                                )
                                OutlinedTextField(
                                    enabled = false,
                                    value = hora.value /*inputSubcentro.value*/,
                                    onValueChange = {
                                    },
                                    keyboardOptions = KeyboardOptions.Default.copy(
                                        keyboardType = KeyboardType.Ascii,
                                        imeAction = ImeAction.Done
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 5.dp, horizontal = 10.dp)
                                        .clickable {
                                            mHourPickerDialog.show()
                                        },
                                    label = { Text("Hora de entrega estimada") },
                                    trailingIcon = {
                                        Icon(Icons.Filled.ExpandLess, "contentDescription")
                                    }
                                )
                            }

                        }

                        item {
                            Divider(
                                color = colorResource(R.color.graydark),
                                thickness = 1.dp,
                                modifier = Modifier.padding(
                                    top = 9.dp,
                                    start = 8.dp,
                                    end = 8.dp
                                )
                            )
                            OutlinedTextField(
                                value = imputsearch.value,
                                onValueChange = {
                                    imputsearch.value = it
                                    descrip.value = ""
                                },
                                keyboardOptions = KeyboardOptions.Default.copy(
                                    keyboardType = KeyboardType.Ascii,
                                    imeAction = ImeAction.Done
                                ),
                                keyboardActions = KeyboardActions(
                                    onDone = {
                                        if (imputsearch.value.isNotBlank()) {
                                            viewModel.getCodSolicitud(
                                                imputsearch.value,
                                                context
                                            )
                                        }
                                    }
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 2.dp, horizontal = 10.dp),
                                label = { Text("Ingresa código o nombre del material") },
                                trailingIcon = {
                                    IconButton(onClick = {
                                        arraydrop[0] = !arraydrop[0]
                                    }) {
                                        Icon(Icons.Filled.ExpandLess, "contentDescription")
                                    }
                                }
                            )
                        }
                        if (arraydrop[0]) {
                            item {
                                AnimatedVisibility(
                                    visible = arraydrop[0]
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
                                                    .align(CenterHorizontally)
                                                    .padding(
                                                        horizontal = 10.dp,
                                                        vertical = 15.dp
                                                    )
                                            )
                                        }
                                    }
                                }
                            }
                            itemsIndexed(viewModel.codigo) { index, item ->
                                Card(
                                    Modifier
                                        .padding(horizontal = 10.dp, vertical = 3.dp)
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(10.dp))
                                        .clickable {
                                            imputsearch.value = item.codigo.toString()
                                            descrip.value = item.nombre.toString()
                                            granel.value = item.granel.toString()
                                            if (granel.value == "0") {
                                                cantidad.value = "1"
                                            }
                                            arraydrop[0] = !arraydrop[0]
                                        }
                                ) {
                                    Text(
                                        text = "Código: " + item.codigo.toString() + "\nDescripción: " + item.nombre.toString(),
                                        style = MaterialTheme.typography.subtitle2,
                                        fontSize = 12.sp,
                                        modifier = Modifier
                                            .align(CenterHorizontally)
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
                            AnimatedVisibility(visible = granel.value == "1") {
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
                                animationSpec = tween(
                                    durationMillis = 500,
                                    easing = FastOutLinearInEasing
                                )

                            )
                            Button(
                                enabled = isValidate,
                                onClick = {
                                    viewModel.listSolicitud.add(
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
                                    },
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
                        if (viewModel.listSolicitud.isNotEmpty()) {
                            item {
                                Box(
                                    modifier =
                                    Modifier
                                        .padding(horizontal = 10.dp, vertical = 5.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .fillMaxWidth()
                                        .background(graydark)
                                        .clickable {
                                            //lista_soli.value = !lista_soli.value
                                            arraydrop[4] = !arraydrop[4]
                                        }
                                ) {
                                    Text(
                                        text = "N°: " + viewModel.listSolicitud.size.toString(),
                                        style = MaterialTheme.typography.subtitle2,
                                        fontSize = 12.sp,
                                        modifier = Modifier
                                            .padding(horizontal = 10.dp, vertical = 10.dp)
                                            .align(CenterStart)
                                    )
                                    Text(
                                        text = "Lista de material",
                                        style = MaterialTheme.typography.subtitle2,
                                        fontSize = 15.sp,
                                        modifier = Modifier
                                            .padding(horizontal = 10.dp, vertical = 10.dp)
                                            .align(Center)
                                    )
                                    Icon(
                                        Icons.Filled.TouchApp,
                                        contentDescription = "",
                                        tint = Color.White,
                                        modifier = Modifier
                                            .align(CenterEnd)
                                            .padding(horizontal = 10.dp)
                                    )
                                }
                            }
                        }
                        itemsIndexed(viewModel.listSolicitud) { index, item ->
                            AnimatedVisibility(visible = arraydrop[4]) {
                                val isRotated = rememberSaveable { mutableStateOf(false) }
                                val rotationAngle by animateFloatAsState(
                                    targetValue = if (isRotated.value) 360F else 0F,
                                    animationSpec = tween(
                                        durationMillis = 500,
                                        easing = FastOutLinearInEasing
                                    )
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
                                    Row(
                                        modifier = Modifier
                                            .align(CenterHorizontally)
                                    ) {
                                        Icon(
                                            Icons.Filled.FactCheck,
                                            contentDescription = "",
                                            tint = Color.White,
                                            modifier = Modifier
                                                .align(CenterVertically)
                                                .padding(start = 10.dp)
                                        )
                                        Text(
                                            text = "Código: " + item.codigo.toString() + "\nDescripción: " + item.desc.toString() + "\nCantidad: " + item.cantidad.toString(),
                                            style = MaterialTheme.typography.subtitle2,
                                            fontSize = 12.sp,
                                            modifier = Modifier
                                                .fillMaxWidth(.85f)
                                                .padding(horizontal = 10.dp, vertical = 15.dp)
                                        )
                                        IconButton(
                                            modifier = Modifier.align(CenterVertically),
                                            onClick = {
                                                isRotated.value = !isRotated.value
                                                viewModel.listSolicitud.removeAt(index)
                                            }) {
                                            Icon(
                                                Icons.Filled.Delete,
                                                contentDescription = "",
                                                tint = Color.White
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }else{
                        item {
                            OutlinedTextField(
                                enabled = false,
                                value = inputSubcentro.value,
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
                                        arraydrop[3] = !arraydrop[3]
                                        //dropSubCentro.value = !dropSubCentro.value
                                        arraydrop[2] = false
                                        //dropSala.value = false
                                        arraydrop[1] = false
                                        //dropRegion.value = false
                                    },
                                label = { Text("Selecciona el almacen a solicitar el pedido") },
                                trailingIcon = {
                                    Icon(Icons.Filled.ExpandLess, "contentDescription")
                                }
                            )
                        }
                        if(arraydrop[3]){
                            itemsIndexed(viewModel.subcentros){index,item->
                                Card(
                                    Modifier
                                        .padding(horizontal = 10.dp, vertical = 3.dp)
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(5.dp))
                                        .background(color = Color.Transparent)
                                        .clickable {
                                            inputSubcentro.value = item.nombre.toString()
                                            almacenid.value = item.idSub.toString()
                                            arraydrop[3] = false
                                            //dropSubCentro.value = false
                                        }
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .align(CenterHorizontally)
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
                            Row() {
                                OutlinedTextField(
                                    enabled = false,
                                    value = fecha.value /*inputSubcentro.value*/,
                                    onValueChange = {
                                    },
                                    keyboardOptions = KeyboardOptions.Default.copy(
                                        keyboardType = KeyboardType.Ascii,
                                        imeAction = ImeAction.Done
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth(.49f)
                                        .padding(vertical = 5.dp, horizontal = 10.dp)
                                        .clickable {
                                            mDatePickerDialog.show()
                                        },
                                    label = { Text("Fecha entrega estimada") },
                                    trailingIcon = {
                                        Icon(Icons.Filled.ExpandLess, "contentDescription")
                                    }
                                )
                                OutlinedTextField(
                                    enabled = false,
                                    value = hora.value /*inputSubcentro.value*/,
                                    onValueChange = {
                                    },
                                    keyboardOptions = KeyboardOptions.Default.copy(
                                        keyboardType = KeyboardType.Ascii,
                                        imeAction = ImeAction.Done
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 5.dp, horizontal = 10.dp)
                                        .clickable {
                                            mHourPickerDialog.show()
                                        },
                                    label = { Text("Hora de entrega estimada") },
                                    trailingIcon = {
                                        Icon(Icons.Filled.ExpandLess, "contentDescription")
                                    }
                                )
                            }

                        }
                        item {
                            Divider(
                                color = colorResource(R.color.graydark),
                                thickness = 1.dp,
                                modifier = Modifier.padding(
                                    top = 9.dp,
                                    start = 8.dp,
                                    end = 8.dp
                                )
                            )
                            OutlinedTextField(
                                enabled = false,
                                value = qr_imput.value,
                                onValueChange = {
                                    qr_imput.value = it
                                    dropcam.value = false
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
                                        dropcam.value = !dropcam.value
                                    }
                                ,
                                label = { Text("Escanea QR de la máquina") },
                                leadingIcon ={
                                    IconButton(onClick = {
                                        dropcam.value = !dropcam.value
                                    },) {
                                        Icon(Icons.Filled.QrCode, "contentDescription")
                                    }
                                }
                            )
                        }
                        item {
                            camera(
                                dropcam,
                                qr_imput,
                                1
                            )
                        }
                        item {
                            OutlinedTextField(
                                enabled = false,
                                value = qr_imput_componente.value,
                                onValueChange = {
                                    qr_imput_componente.value = it
                                    dropcam2.value = false
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
                                        dropcam2.value = !dropcam2.value
                                    },
                                label = { Text("Escanea QR del componente") },
                                leadingIcon ={
                                    IconButton(onClick = {
                                        dropcam2.value = !dropcam2.value
                                    },) {
                                        Icon(Icons.Filled.QrCode, "contentDescription")
                                    }
                                }
                            )
                        }
                        item {
                            camera(
                                dropcam2,
                                qr_imput_componente,
                                2
                            )
                        }
                        item {
                            val isRotated = rememberSaveable { mutableStateOf(false) }
                            val rotationAngle by animateFloatAsState(
                                targetValue = if (isRotated.value) 360F else 0F,
                                animationSpec = tween(durationMillis = 500,easing = FastOutLinearInEasing)

                            )
                            val valida = Utils(context)
                            val isValidate by derivedStateOf {
                                valida.getValidation(qr_imput.value)||valida.getValidation2(qr_imput.value)
                                qr_imput_componente.value.isNotBlank()
                                        && qr_imput.value.isNotBlank()
                                        && almacenid.value.isNotBlank()
                                        && fecha.value.isNotBlank()
                                        && hora.value.isNotBlank()
                            }
                            Button(
                                enabled = isValidate,
                                onClick = {
                                    isRotated.value = !isRotated.value
                                    val validationmaquina = qr_imput.value.split(";")
                                    viewModel.SolicitarPorQr(
                                        maquina = validationmaquina[0],
                                        codigo = qr_imput_componente.value,
                                        context = context,
                                        almacen = almacenid.value,
                                        fechahora = "${fecha.value}T${hora.value}:00"
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
                                    text = "Solicitar",
                                    fontSize = 16.sp,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
    }
    alertSolicitud()
}

@Composable
private fun camera(
    dropcam : MutableState<Boolean>,
    string : MutableState<String>,
    intval : Int,
    viewModel: SolicitudViewModel = hiltViewModel(),
){
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
            val mMediaPlayer = MediaPlayer.create(context, R.raw.bip)
            val valida = Utils(context)
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
                                                if(intval==1){
                                                    if(valida.getValidation(barcodeValue)||valida.getValidation2(barcodeValue)) {
                                                        string.value = barcodeValue
                                                        dropcam.value = false
                                                    }else{
                                                        viewModel.alert("QR de la máquina incorrecta.", false)
                                                    }
                                                }else if(intval==2){
                                                    if(valida.getValidationRefaccion(barcodeValue)||valida.getValidationRefaccion2(barcodeValue)||valida.getValidationComponent(barcodeValue)||valida.getValidationComponent2(barcodeValue)){
                                                        string.value = barcodeValue
                                                        dropcam.value = false
                                                    }else{
                                                        viewModel.alert("QR del componente incorrecto.", false)
                                                    }
                                                }
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
private fun titleHome(
    viewModel: SolicitudViewModel
){
    Box(modifier = Modifier
        .padding(0.dp, 0.dp, 0.dp, 5.dp)
        .clip(RoundedCornerShape(10.dp))
        .background(blackdark)
        .fillMaxWidth()
        .padding(5.dp),
        //verticalArrangement = Arrangement.SpaceEvenly,
       // horizontalAlignment = CenterHorizontally
    ) {
        Column(Modifier.align(Center)) {
            Image(
                painter = painterResource(R.drawable.crear_pedido_icon),
                contentDescription = "",
                modifier = Modifier
                    .padding(0.dp)
                    .size(50.dp)
                    .align(CenterHorizontally)
            )
            Text(
                text = "NUEVO PEDIDO DE MATERIAL",
                style = MaterialTheme.typography.subtitle2,
                modifier = Modifier.align(CenterHorizontally))
        }
        if(viewModel.`QR-Manual`.value){
            viewModel.getSubCentros()
            Box(modifier = Modifier
                .align(CenterEnd)
                .padding(10.dp)) {
                IconButton(onClick = {
                    viewModel.`QR-Manual`.value = false
                    viewModel.delate.value = true
                }) {
                    Column() {
                        Icon(Icons.Filled.QrCodeScanner, contentDescription = "QR",modifier = Modifier.align(CenterHorizontally))
                        Text(text = "Pedido por QR",fontSize = 9.sp,modifier = Modifier.align(CenterHorizontally))
                    }
                }
            }
        }else{
            viewModel.getSubCentros()
            Box(modifier = Modifier
                .align(CenterEnd)
                .padding(10.dp)) {
                IconButton(onClick = {
                    viewModel.`QR-Manual`.value = true
                    viewModel.delate.value = true
                    //viewModel.getSubCentros()
                }) {
                    Column() {
                        Icon(Icons.Filled.Keyboard, contentDescription = "QR",modifier = Modifier.align(CenterHorizontally))
                        Text(text = "Pedido Manual",fontSize = 9.sp,modifier = Modifier.align(CenterHorizontally))
                    }
                }
            }
        }
    }
}
@Composable
private fun CustomTopAppBarSolicitud(
    navController: NavController,
    list: MutableList<Solicitud>,
    inputSalaID : String,
    alamacen :String,
    fecha:String,
    hora:String,
    viewModel: SolicitudViewModel = hiltViewModel(),
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
                val isValidate by derivedStateOf {
                    list.isNotEmpty()
                            && inputSalaID.isNotBlank()
                            && alamacen.isNotBlank()
                            && fecha.isNotBlank()
                            && hora.isNotBlank()
                }
                if(isValidate){
                    val isRotated = rememberSaveable { mutableStateOf(false) }
                    val rotationAngle by animateFloatAsState(
                        targetValue = if (isRotated.value) 360F else 0F,
                        animationSpec = tween(durationMillis = 500, easing = FastOutLinearInEasing)
                    )
                    Text(
                        text = "Enviar pedido",
                        fontSize = 11.sp,
                        color = Color.White,
                        modifier = Modifier
                            .align(CenterEnd)
                            .padding(end = 40.dp)
                    )
                    IconButton(modifier = Modifier
                        .align(CenterEnd)
                        .graphicsLayer {
                            rotationX = rotationAngle
                            cameraDistance = 16 * density
                        },
                        onClick = {
                            isRotated.value = !isRotated.value
                            viewModel.postPedido(ArrayList<Solicitud>(list), salaid= inputSalaID, almacen = alamacen, fechahora = "${fecha}T${hora}:00",context = context)
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
private fun alertSolicitud(
    viewModel: SolicitudViewModel = hiltViewModel(),
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
                            text = viewModel.textalert.value,
                            fontSize = 14.sp,
                            color = Color.White,
                            modifier = Modifier
                                .align(CenterVertically)
                        )
                        //Text(text = viewModel.textalert.value)
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
