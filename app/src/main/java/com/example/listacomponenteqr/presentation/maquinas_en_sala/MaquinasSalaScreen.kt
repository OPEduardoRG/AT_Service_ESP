package com.example.listacomponenteqr.presentation.maquinas_en_sala

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddLocationAlt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterEnd
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterStart
import androidx.compose.ui.Alignment.Companion.End
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
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
import com.example.listacomponenteqr.ui.theme.blackdark
import com.example.listacomponenteqr.ui.theme.blacktransp
import com.example.listacomponenteqr.ui.theme.graydark
import com.example.listacomponenteqr.ui.theme.reds

@Composable
fun MaquinasSalaScreen(
    navController: NavController,
    viewModel: MaquinasSalaViewModel = hiltViewModel()
) {
    val regionesEspana = rememberSaveable {
        listOf<RegionesEsp>(
            RegionesEsp("30", "Alava"),
            RegionesEsp("36", "Albacete"),
            RegionesEsp("7", "Alicante"),
            RegionesEsp("5", "Almeria"),
            RegionesEsp("37", "Asturias"),
            RegionesEsp("12", "Avila"),
            RegionesEsp("13", "Badajoz"),
            RegionesEsp("31", "Baleares"),
            RegionesEsp("40", "Barcelona"),
            RegionesEsp("1", "Burgos"),
            RegionesEsp("17", "Caceres"),
            RegionesEsp("19", "Cadiz"),
            RegionesEsp("34", "Cantabria"),
            RegionesEsp("51", "Castellon"),
            RegionesEsp("52", "Ceuta"),
            RegionesEsp("46", "Ciudad Real"),
            RegionesEsp("15", "Cordoba"),
            RegionesEsp("44", "Cuenca"),
            RegionesEsp("50", "Gerona"),
            RegionesEsp("18", "Granada"),
            RegionesEsp("28", "Guadalajara"),
            RegionesEsp("32", "Guipuzcoa"),
            RegionesEsp("33", "Huelva"),
            RegionesEsp("27", "Huesca"),
            RegionesEsp("14", "Jaen"),
            RegionesEsp("38", "La Coruña"),
            RegionesEsp("21", "Las Palmas"),
            RegionesEsp("43", "Leon"),
            RegionesEsp("41", "Lerida"),
            RegionesEsp("35", "Logroño"),
            RegionesEsp("53", "Lugo"),
            RegionesEsp("6", "Madrid"),
            RegionesEsp("2", "Malaga"),
            RegionesEsp("42", "Melilla"),
            RegionesEsp("3", "Murcia"),
            RegionesEsp("23", "Navarra"),
            RegionesEsp("29", "no-region-0004"),
            RegionesEsp("49", "Orense"),
            RegionesEsp("4", "Palencia"),
            RegionesEsp("39", "Pontevedra"),
            RegionesEsp("16", "Salamanca"),
            RegionesEsp("24", "Santa Cruz Tenerife"),
            RegionesEsp("22", "Segovia"),
            RegionesEsp("20", "Sevilla"),
            RegionesEsp("45", "Soria"),
            RegionesEsp("8", "Tarragona"),
            RegionesEsp("26", "Teruel"),
            RegionesEsp("48", "Toledo"),
            RegionesEsp("11", "Valencia"),
            RegionesEsp("9", "Valladolid"),
            RegionesEsp("10", "Vizcaya"),
            RegionesEsp("47", "Zamora"),
            RegionesEsp("25", "Zaragoza"),
        )
    }
    Scaffold(
        topBar = {
            TopAppBarMaquinasSala(navController = navController)
        }
    ) {
        ListMaquinas(viewModel.maquinasSala, viewModel, regionesEspana)
    }
}

@Composable
private fun titleMaquinasSalas() {
    Column(
        modifier = Modifier
            .padding(0.dp, 0.dp, 0.dp, 5.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(blackdark)
            .fillMaxWidth()
            .padding(5.dp),
    )
    {
        Image(
            painter = painterResource(R.drawable.nav_sala_icon),
            contentDescription = "",
            modifier = Modifier
                .padding(0.dp)
                .size(50.dp)
                .align(CenterHorizontally)
        )
        Text(
            text = "MAQUINAS EN SALA",
            style = MaterialTheme.typography.subtitle2,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}

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

@Composable
fun ListMaquinas(
    maquinasSala: SnapshotStateList<MaquinasSala>,
    viewModel: MaquinasSalaViewModel,
    regionesEspana: List<RegionesEsp>,
) {
    var idsala = remember {
        mutableStateOf("")
    }
    val dropRegion = rememberSaveable { mutableStateOf(false) }
    val dropSala = rememberSaveable { mutableStateOf(false) }
    val inputRegion = rememberSaveable() { mutableStateOf("") }
    val inputSala = rememberSaveable() { mutableStateOf("") }
    val inputSalaID = rememberSaveable() { mutableStateOf("") }
    val context = LocalContext.current
    val index = rememberSaveable() {
        mutableStateOf(0)
    }
    Column(
        modifier = Modifier
            .padding(10.dp, 10.dp, 10.dp, 0.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(Color.Black)
    ) {
        LazyColumn(
            modifier = Modifier
                //.padding(top = 10.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(blacktransp)
        ) {
            item {
                titleMaquinasSalas()
            }
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
                            dropRegion.value = !dropRegion.value
                            dropSala.value = false
                        },
                    label = { Text("Selecciona la región") },
                    trailingIcon = {
                        IconButton(onClick = {
                            dropRegion.value = !dropRegion.value
                            dropSala.value = false
                        }) {
                            Icon(Icons.Filled.ExpandLess, "contentDescription")
                        }
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
                            dropSala.value = !dropSala.value
                            dropRegion.value = false
                        },
                    label = { Text("Selecciona la sala") },
                    trailingIcon = {
                        IconButton(onClick = {
                            dropSala.value = !dropSala.value
                            dropRegion.value = false
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
            if (dropSala.value) {
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
                                viewModel.getMaquinasSala(item.salaid!!.toInt())
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
            item {
                OutlinedTextField(
                    value = idsala.value,
                    onValueChange = {
                        idsala.value = it
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Ascii,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            viewModel.getMaquinasSala(
                                idsala.value.toInt()
                            )
                        }
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp, horizontal = 10.dp),
                    label = { Text("Ingresa código o nombre del material") },
                    /*trailingIcon = {
                    IconButton(onClick = {
                        dropcam.value = false
                        drop.value = !drop.value
                    }) {
                        Icon(Icons.Filled.ExpandLess, "contentDescription")
                    }
                },*/
                )

            }
            itemsIndexed(maquinasSala) { index, item ->
                var bol = rememberSaveable {
                    mutableStateOf(false)
                }
                Card(
                    modifier = Modifier
                        .padding(5.dp)
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

// on below line we are creating a pop up window dialog method
@Composable
fun PopupWindowDialog(x1: MutableState<Boolean>, item: MaquinasSala) {
    val openDialog = remember { mutableStateOf(false) }

    Popup(
        alignment = Alignment.TopCenter,
        properties = PopupProperties(),
        ) {
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
                IconButton(onClick = { x1.value = false }, modifier = Modifier.align(CenterEnd) ) {
                    Icon(
                        Icons.Filled.Close, contentDescription = "", modifier = Modifier.align(
                            CenterEnd
                        )
                    )
                }
                Text(text = "No. Serie: ${item.serie}", style = MaterialTheme.typography.h6,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(top = 30.dp))

                Text(text = "Nombre: ${item.mueble}",
                    style = MaterialTheme.typography.subtitle1,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(top = 70.dp))
            }
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
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.List, contentDescription = "", modifier = Modifier.padding(start = 10.dp))
                            Column() {
                                Text(
                                    text = "Codigo: ${it.clave}",
                                    fontSize = 14.sp,
                                    color = Color.White,
                                    modifier = Modifier.padding(start = 15.dp, top = 10.dp, end = 10.dp))

                                Text(
                                    text = "Nombre: ${it.nombre}",
                                    fontSize = 14.sp,
                                    color = Color.White,
                                    modifier = Modifier.padding(start = 15.dp, top = 3.dp, end = 10.dp, bottom = 10.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
