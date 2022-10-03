package com.example.listacomponenteqr.presentation.maquinas_en_sala

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
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
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterEnd
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.listacomponenteqr.R
import com.example.listacomponenteqr.data.remote.dto.MaquinasSala.MaquinasSala
import com.example.listacomponenteqr.data.remote.dto.SolicitudRefaccion.RegionesEsp
import com.example.listacomponenteqr.data.remote.dto.SolicitudRefaccion.Salas
import com.example.listacomponenteqr.ui.theme.blackdark
import com.example.listacomponenteqr.ui.theme.blacktransp
import com.example.listacomponenteqr.ui.theme.graydark
import com.example.listacomponenteqr.ui.theme.reds

//---------------------NavContoller-----------------------------------------------------------------
@Composable
fun MaquinasSalaScreen(
    navController: NavController,
    viewModel: MaquinasSalaViewModel = hiltViewModel()
) {
    var region = when (viewModel.getListSimilar.size) {
        0 -> viewModel.regionesEspana
        else -> viewModel.getListSimilar
    }

    var salas = when (viewModel.getListSimilarSalas.size) {
        0 -> viewModel.salasxRegion
        else -> viewModel.getListSimilarSalas
    }
    Scaffold(
        topBar = {
            TopAppBarMaquinasSala(navController = navController)
        }
    ) {
        ListMaquinas(viewModel.maquinasSala, viewModel, region, salas)
    }
}

//--------------------------------------------------------------------------------------------------
//------------------------ ToolBar App--------------------------------------------------------------
@Composable
private fun TopAppBarMaquinasSala(
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
//--------------------------------------------------------------------------------------------------
//-----------------------Funcion para girar el button

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
//---------------------------------Title on down ToolBar--------------------------------------------

@Composable
private fun titleMaquinasSalas() {
    Column(
        modifier = Modifier
            .padding(0.dp, 0.dp, 0.dp, 5.dp)
//            .clip(RoundedCornerShape(10.dp))
            .background(blackdark)
            .fillMaxWidth()
            .padding(5.dp),
    )
    {
        Row(
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Image(
                painter = painterResource(R.drawable.nav_sala_icon),
                contentDescription = "",
                modifier = Modifier
                    .padding(0.dp)
                    .size(50.dp)
            )
            Text(
                text = "MAQUINAS EN SALA",
                style = MaterialTheme.typography.subtitle2,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(start = 10.dp)
            )
        }

    }
}
//--------------------------------------------------------------------------------------------------

@SuppressLint("UnrememberedMutableState", "UnusedTransitionTargetStateParameter")
@Composable
fun ListMaquinas(
    maquinasSala: SnapshotStateList<MaquinasSala>,
    viewModel: MaquinasSalaViewModel,
    regionesEspana: List<RegionesEsp>,
    salas: SnapshotStateList<Salas>,
) {

    var idsala = remember { mutableStateOf("") }
    val dropRegion = rememberSaveable { mutableStateOf(false) }
    val dropSala = rememberSaveable { mutableStateOf(false) }
    val inputRegion = rememberSaveable() { mutableStateOf("") }
    val inputSala = rememberSaveable() { mutableStateOf("") }
    val inputSalaID = rememberSaveable() { mutableStateOf("") }
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    var expand_List = rememberSaveable() {
        mutableStateOf(true)
    }

    Column(
        modifier = Modifier
            .padding(10.dp, 10.dp, 10.dp, 0.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(Color.Black)
    ) {
        LazyColumn(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(blacktransp)
        ) {
            item {
                titleMaquinasSalas()
            }
            item {
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
//----------------------------------------------------------------------------------------------------------------
                OutlinedTextField(
                    enabled = true,
                    value = inputRegion.value,
                    onValueChange = {
                        inputRegion.value = it
                        viewModel.getValidarSimilarLista(context, it)
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
                        },
                    label = { Text("Selecciona la región") },
                    trailingIcon = {
                        CardArrow(
                            arrowRotationDegree,
                            { dropRegion.value = !dropRegion.value }
                        )


                    }
                )
            }

            if (dropRegion.value) {
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
                                    .align(Alignment.CenterVertically)
                                    .padding(horizontal = 5.dp)
                            )
                            Text(
                                text = "${item.nombre}",
                                style = MaterialTheme.typography.subtitle2,
                                fontSize = 15.sp,
                                modifier = Modifier
                                    .align(Alignment.CenterVertically)
                                    .padding(horizontal = 10.dp)
                            )
                        }
                    }
                }
            }
//--------------------------------------------------------------------------------------------------------------
            item {
                val transitionState = remember {
                    MutableTransitionState(dropSala.value).apply {
                        targetState = !dropSala.value
                    }
                }
                val transition =
                    updateTransition(targetState = transitionState, label = "transition")
                val arrowRotationDegree by transition.animateFloat({
                    tween(durationMillis = 300)
                }, label = "rotationDegreeTransition") {
                    if (dropSala.value) 0f else 180f
                }
                OutlinedTextField(
                    enabled = inputRegion.value.isNotBlank(),
                    value = inputSala.value,
                    onValueChange = {
                        inputSala.value = it
                        viewModel.getSimilarSala(context, inputSala.value)
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp, horizontal = 10.dp)
                        .clickable {
                            dropSala.value = !dropSala.value
                            dropRegion.value = false
                        }
                        .onFocusChanged { focusState ->
                            if (focusState.isFocused) {
                                dropSala.value = true
                            } else if (focusState.hasFocus) {
                                dropSala.value = false
                                focusManager.clearFocus()
                            }
                        },
                    label = { Text("Selecciona la sala") },
                    trailingIcon = {
                        CardArrow(
                            arrowRotationDegree,
                            { dropSala.value = !dropSala.value }
                        )
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
            if (dropSala.value) {
               itemsIndexed(salas) { idex, item ->
                    Card(
                        Modifier
                            .padding(horizontal = 10.dp, vertical = 3.dp)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(5.dp))
                            .background(color = Color.Transparent)
                            .clickable {
                                inputSala.value = item.nombre.toString()
                                inputSalaID.value = item.salaid.toString()
                                viewModel.getMaquinasSala(item.salaid!!.toInt())
                                dropSala.value = false
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
                                    .align(Alignment.CenterVertically)
                                    .padding(horizontal = 5.dp)
                            )
                            Text(
                                text = "${item.nombre}",
                                style = MaterialTheme.typography.subtitle2,
                                fontSize = 15.sp,
                                modifier = Modifier
                                    .align(Alignment.CenterVertically)
                                    .padding(horizontal = 10.dp)
                            )
                        }
                    }
                }
            }
            if(inputRegion.value.isNotBlank()&&inputSala.value.isNotBlank()){
                item {
                    val n = if(maquinasSala.isNotEmpty()) "N°: ${maquinasSala.size}"
                    else ""
                    val tittla = if(maquinasSala.isNotEmpty()) "Maquinas en sala"
                    else "Sin maquinas en sala"
                    val icon = if(maquinasSala.isNotEmpty()) Icons.Filled.TouchApp
                    else Icons.Filled.Cancel
                    Box(
                        modifier =
                        Modifier
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .fillMaxWidth()
                            .background(graydark)
                            .clickable {
                                expand_List.value = !expand_List.value
                            }
                    ) {
                        Text(
                            text = n,
                            style = MaterialTheme.typography.subtitle2,
                            fontSize = 12.sp,
                            modifier = Modifier
                                .padding(horizontal = 10.dp, vertical = 10.dp)
                                .align(Alignment.CenterStart)
                        )
                        Text(
                            text = tittla,
                            style = MaterialTheme.typography.subtitle2,
                            fontSize = 15.sp,
                            modifier = Modifier
                                .padding(horizontal = 10.dp, vertical = 10.dp)
                                .align(Alignment.Center)
                        )
                        Icon(
                            icon,
                            contentDescription = "",
                            tint = Color.White,
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .padding(horizontal = 10.dp)
                        )
                    }
                }
            }

            itemsIndexed(maquinasSala) { index, item ->

                AnimatedVisibility(visible = expand_List.value ) {
                    var bol = rememberSaveable {
                        mutableStateOf(false)
                    }
                    Card(
                        modifier = Modifier
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                            .fillMaxWidth()
                    ) {

                        Column(modifier = Modifier.clickable { bol.value = !bol.value }) {
                            val indexx = index + 1
                            Row() {
                                Image(
                                    painter = painterResource(R.drawable.maquinas_icon),
                                    contentDescription = "",
                                    modifier = Modifier
                                        .size(45.dp)
                                        .padding(10.dp)
                                )
                                Column() {
                                    Text(
                                        text = "Maquina no. ${indexx}",
                                        fontSize = 14.sp,
                                        color = Color.White,
                                        modifier = Modifier
                                    )
                                    Text(
                                        text = "Nombre: ${item.mueble}",
                                        fontSize = 14.sp,
                                        color = Color.White,
                                        modifier = Modifier
                                    )
                                    Text(
                                        text = "Serie: ${item.serie}",
                                        fontSize = 14.sp,
                                        color = Color.White,
                                        modifier = Modifier
                                    )
                                }
                            }
                            if (bol.value) {
                                PopupWindowDialog(bol, item)
                            }
                        }
                    }
                }

            }

        }
    }
}



@Composable
fun PopupWindowDialog(x1: MutableState<Boolean>, item: MaquinasSala) {

    AlertDialog(
        onDismissRequest = {
//            clientes.value = false
        },
        title = null,
        buttons = {
            Column(
                Modifier
                    .padding(top = 5.dp)
                    .background(graydark, RoundedCornerShape(10.dp))
                    .border(1.dp, color = Color.Black, RoundedCornerShape(10.dp))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(reds)
                ) {
                    IconButton(onClick = { x1.value = false }, modifier = Modifier.align(CenterEnd)) {
                        Icon(
                            Icons.Filled.Close, contentDescription = "", modifier = Modifier.align(
                                CenterEnd
                            )
                        )
                    }
                    Text(
                        text = "No. Serie: ${item.serie}", style = MaterialTheme.typography.h6,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(top = 30.dp)
                    )

                    Text(
                        text = "Nombre: ${item.mueble}",
                        style = MaterialTheme.typography.subtitle1,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(top = 70.dp)
                    )
                }
                if (item.componentes != null){
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 10.dp)
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        item.componentes.forEachIndexed() { ind, it ->
                            Card(
                                modifier = Modifier
                                    .padding(horizontal = 10.dp, vertical = 5.dp)
                                    .fillMaxWidth(), backgroundColor = colorResource(
                                    id = R.color.blackdark
                                )
                            ) {
                                val indexx = ind + 1
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        Icons.Filled.List,
                                        contentDescription = "",
                                        modifier = Modifier.padding(start = 10.dp)
                                    )
                                    Column() {
                                        Text(
                                            text = "Codigo: ${it.clave}",
                                            fontSize = 14.sp,
                                            color = Color.White,
                                            modifier = Modifier.padding(
                                                start = 15.dp,
                                                top = 10.dp,
                                                end = 10.dp
                                            )
                                        )

                                        Text(
                                            text = "Nombre: ${it.nombre}",
                                            fontSize = 14.sp,
                                            color = Color.White,
                                            modifier = Modifier.padding(
                                                start = 15.dp,
                                                top = 3.dp,
                                                end = 10.dp,
                                                bottom = 10.dp
                                            )
                                        )
                                    }
                                }

                            }
                        }
                    }
                }else{
                    Text(
                        text = "Sin datos en la base",
                        fontSize = 14.sp,
                        color = Color.White,
                        modifier = Modifier.padding(
                            start = 30.dp,
                            top = 6.dp,
                            end = 20.dp,
                            bottom = 20.dp
                        ),
                    )
                }
            }
        },
        shape = RoundedCornerShape(15.dp)
    )


}
