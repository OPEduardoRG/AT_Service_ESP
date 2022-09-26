package com.example.zitrocrm.screens

import android.app.Activity
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterEnd
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterStart
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.listacomponenteqr.R
import com.example.listacomponenteqr.navigation.Destination
import com.example.listacomponenteqr.presentation.components.InfoEstatus
import com.example.listacomponenteqr.ui.theme.blackdark
import com.example.listacomponenteqr.ui.theme.reds
import com.example.listacomponenteqr.utils.SharedPrefence

/** SIZE BUTTONS AND SIZE ICONS **/
val SizeButtons = 160.dp
/**
 * Created by Brian Fernando Mtz on 02-2022.
 */
@Composable
fun HomeScreen(
    navController: NavController,
){
    var infoMaquinas = remember {
        mutableStateOf(false)
    }
    var infoMateriales = remember {
        mutableStateOf(false)
    }
    Scaffold( 
        topBar = {
            CustomTopAppBarHome()
        }
    ) {
        ContentHome(
            navController,
            infoMaquinas,
            infoMateriales
        )
        InfoEstatus(
            infoMaquinas,
            "MÁQUINAS.",
            "ACTIVAR MÁQUINA\nEste modulo se utilizará para realizar el proceso de activación de una máquina escaneando CPU y monitor.\n\n" +
                    "LISTA COMPONENTES\nEste módulo, se encarga de mostrar los compoentes que tiene una maquina si el tecnico lo solicita.\n\n"+
                    "REEMPLAZO DE COMPONENTE\nEste módulo se utilizará para hacer un reemplazo de los componentes dañados por nuevos de las máquinas."
        )
        InfoEstatus(
            infoMateriales,
            "MATERIALES.",
            "CREAR PEDIDO DE MATERIAL\nEntramos al siguiente módulo, el cual tendrá la funcionalidad de crear un pedido de material para el almacén de (getafe, furgonetas, almacén central, almacén de alguna comunidad).\n\n" +
                    "DEVOLUCIÓN DE MATERIAL\nIngresamos a este apartado donde se registrará el material que el técnico devuelve para almacén.\n\n"+
                    "DESCUENTO DE MATERIAL\nEsta sección tendrá la función de realizar el descuento de material del inventario del técnico que se instalarán en las máquinas.\n\n"+
                    "INVENTARIO\nEn el siguiente apartado se va a mostrar la información del material que se encuentra en inventario de la van del tecnico, para que el personal lo pueda consultar y así tener un mejor control de materiales, en el cual tambien podra descontar material y devolverlo al almacen desde este modulo.\n\n"+
                    "RECIBIR MATERIAL\nEn el siguiente modulo se le agregara material para el técnico en su inventario."
        )
    }
}
/** TOPBAR **/
@Composable
private fun CustomTopAppBarHome(

) {
    val datastore = SharedPrefence(LocalContext.current)
    val usuID = datastore.getUsuID()
    val usu = datastore.getUsu()
    TopAppBar(
        elevation = 0.dp,
        modifier = Modifier.height(70.dp),
        title = {
            Box(modifier = Modifier.fillMaxSize()) {
                Text(text = "Bienvenido \n"+usu.toString(),modifier = Modifier.align(CenterStart), color = Color.White, fontSize = 14.sp)
                Image(
                    painter = painterResource(R.drawable.logo_qr),
                    contentDescription = "",
                    modifier = Modifier
                        .align(Center)
                        .padding(5.dp)
                        .padding(start = 0.dp, end = 15.dp)
                )
                val activity = (LocalContext.current as? Activity)
                Icon(
                    Icons.Filled.ExitToApp,
                    "contentDescription",
                    tint = Color.White,
                    modifier = Modifier
                        .align(CenterEnd)
                        .padding(10.dp)
                        .size(25.dp)
                        .clickable {
                            activity?.finish()
                            datastore.clearSharedPreference()
                        }
                )
            }
        },
        backgroundColor = reds,
    )
}
/** CONTENT PAGUE COMPLET HOME **/
@Composable
private fun ContentHome(
    navController: NavController,
    infoMaquinas : MutableState<Boolean>,
    infoMateriales : MutableState<Boolean>
){
    Box(
        Modifier
            .fillMaxSize()
            .verticalScroll(state = ScrollState(1))) {
        Column(modifier = Modifier.fillMaxSize()) {
            titleHome()
            subTitleHome(infoMaquinas)
            contentHome(navController)
            subTitleHome2(infoMateriales)
            contentHome2(navController)
        }
    }
}
/** CHILD CONTENT PAGE **/
@Composable
private fun titleHome(){
    Column(modifier = Modifier
        .padding(10.dp, 10.dp, 10.dp, 5.dp)
        .clip(RoundedCornerShape(10.dp))
        .background(blackdark)
        .fillMaxHeight()
        .padding(5.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = CenterHorizontally)
    {
        Image(
            painter = painterResource(R.drawable.maquinas_icon),
            contentDescription = "",
            modifier = Modifier
                .fillMaxSize()
                .padding(0.dp)
                .size(50.dp)
        )
        Text(
            text = "ZITRO - AT SERVICE",
            style = MaterialTheme.typography.subtitle2,
            modifier = Modifier.align(CenterHorizontally))
    }
}

@Composable
private fun subTitleHome(
    infoMaquinas : MutableState<Boolean>
){
    Box(modifier = Modifier
        .padding(10.dp, 5.dp)
        .clip(RoundedCornerShape(10.dp))
        .background(blackdark)
        .fillMaxWidth()
        .padding(5.dp))
    {
        Icon(
            Icons.Filled.QrCode, "",
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(10.dp)
        )
        Text(
            text = "MÁQUINAS",
            style = MaterialTheme.typography.subtitle2,
            modifier = Modifier.align(alignment = Center))

        IconButton(onClick = {
            infoMaquinas.value = true
        }, modifier = Modifier.align(CenterEnd)) {
            Icon(
                Icons.Filled.Info, "",
                modifier = Modifier
            )
        }
    }
}

@Composable
private fun contentHome(
    navController: NavController,
){
    Box(modifier = Modifier
        .padding(10.dp, 5.dp)
        .clip(RoundedCornerShape(10.dp))
        .fillMaxWidth()
        .padding(5.dp))
    {
        Row(modifier = Modifier.horizontalScroll(state = ScrollState(1))) {
            Box(modifier = Modifier
                .padding(horizontal = 5.dp)
                .height(SizeButtons)
                .clip(RoundedCornerShape(13.dp))
                .background(colorResource(R.color.blackdark))
                .clickable {
                    navController.navigate(route = Destination.MaquinasActivate.route)
                }
            ) {
                Column(Modifier.align(Center)) {
                    Image(
                        painter = painterResource(R.drawable.maquinas_icon)
                        , contentDescription = "",
                        modifier = Modifier
                            .padding(33.dp, 24.dp, 33.dp, 17.dp)
                            .size(70.dp)
                            .align(CenterHorizontally)

                    )
                    Text(text = " ACTIVAR\nMÁQUINA",
                        style = MaterialTheme.typography.subtitle2,
                        fontSize = 12.sp,
                        modifier = Modifier
                            .align(CenterHorizontally)
                            .padding(bottom = 5.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.size(10.dp))
            Box(modifier = Modifier
                .padding(horizontal = 5.dp)
                .height(SizeButtons)
                .clip(RoundedCornerShape(13.dp))
                .background(colorResource(R.color.graydark))
                .clickable {
                    navController.navigate(route = Destination.MaquinasListScreen.route)
                }
            ) {
                Column(Modifier.align(Center)) {
                    Image(
                        painter = painterResource(R.drawable.maquinas_icon)
                        , contentDescription = "",
                        modifier = Modifier
                            .padding(33.dp, 24.dp, 33.dp, 17.dp)
                            .size(70.dp)
                            .align(CenterHorizontally)

                    )
                        Text(text = "      LISTA DE\nCOMPONENTES",
                        style = MaterialTheme.typography.subtitle2,
                        fontSize = 12.sp,
                        modifier = Modifier
                            .align(CenterHorizontally)
                            .padding(bottom = 5.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.size(10.dp))
            Box(modifier = Modifier
                .padding(horizontal = 5.dp)
                .height(SizeButtons)
                .clip(RoundedCornerShape(13.dp))
                .background(colorResource(R.color.blackdark))
                .clickable {
                    navController.navigate(route = Destination.MaquinasPiezasScreen.route)
                }
            ) {
                Column(Modifier.align(Center)) {
                    Image(
                        painter = painterResource(R.drawable.maquinas_cambio)
                        , contentDescription = "",
                        modifier = Modifier
                            .padding(20.dp, 18.dp, 22.dp, 4.dp)
                            .size(95.dp)

                    )
                    Text(text = "REMPLAZO DE\nCOMPONENTE",
                        style = MaterialTheme.typography.subtitle2,
                        fontSize = 12.sp,
                        modifier = Modifier
                            .align(CenterHorizontally)
                            .padding(bottom = 10.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun subTitleHome2(
    infoMateriales : MutableState<Boolean>
){
    Box(modifier = Modifier
        .padding(10.dp, 5.dp)
        .clip(RoundedCornerShape(10.dp))
        .background(blackdark)
        .fillMaxWidth()
        .padding(5.dp))
    {
        Icon(
            Icons.Filled.Window, "",
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(10.dp)
        )
        Text(
            text = "MATERIALES",
            style = MaterialTheme.typography.subtitle2,
            modifier = Modifier.align(alignment = Alignment.Center)
        )
        IconButton(onClick = {
            infoMateriales.value = true
        }, modifier = Modifier.align(CenterEnd)) {
            Icon(
                Icons.Filled.Info, "",
                modifier = Modifier
            )
        }
    }
}

@Composable
private fun contentHome2(
    navController: NavController,
){
    Box(modifier = Modifier
        .padding(10.dp, 5.dp)
        .clip(RoundedCornerShape(10.dp))
        .fillMaxWidth()
        .padding(5.dp))
    {
        Row(modifier = Modifier.horizontalScroll(state = ScrollState(1))) {
            Box(modifier = Modifier
                .padding(horizontal = 5.dp)
                .height(SizeButtons)
                .clip(RoundedCornerShape(13.dp))
                .background(colorResource(R.color.graydark))
                .clickable {
                    navController.navigate(route = Destination.SolicitudPedidoScreen.route)
                }
            ) {
                Column(Modifier.align(Center)) {
                    Image(
                        painter = painterResource(R.drawable.crear_pedido_icon)
                        , contentDescription = "",
                        modifier = Modifier
                            .padding(20.dp, 18.dp, 22.dp, 4.dp)
                            .size(95.dp)
                            .align(CenterHorizontally)

                    )
                    Text(text = "CREAR PEDIDO\n DE MATERIAL",
                        style = MaterialTheme.typography.subtitle2,
                        fontSize = 12.sp,
                        modifier = Modifier
                            .align(CenterHorizontally)
                            .padding(bottom = 5.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.size(10.dp))
            Box(modifier = Modifier
                .padding(horizontal = 5.dp)
                .height(SizeButtons)
                .clip(RoundedCornerShape(13.dp))
                .background(colorResource(R.color.blackdark))
                .clickable {
                    navController.navigate(route = Destination.DevolucionMaterialScreen.route)
                }
            ) {
                Column(Modifier.align(Center)) {
                    Image(
                        painter = painterResource(R.drawable.devolucion_icon)
                        , contentDescription = "",
                        modifier = Modifier
                            .padding(20.dp, 18.dp, 22.dp, 4.dp)
                            .size(95.dp)

                    )
                    Text(text = "DEVOLUCIÓN\nDE MATERIAL",
                        style = MaterialTheme.typography.subtitle2,
                        fontSize = 12.sp,
                        modifier = Modifier
                            .align(CenterHorizontally)
                            .padding(bottom = 5.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.size(10.dp))
            Box(modifier = Modifier
                .padding(horizontal = 5.dp)
                .height(SizeButtons)
                .clip(RoundedCornerShape(13.dp))
                .background(colorResource(R.color.graydark))
                .clickable {
                    navController.navigate(route = Destination.DescuentoMaterialScreen.route)
                }
            ) {
                Column(Modifier.align(Center)) {
                    Image(
                        painter = painterResource(R.drawable.descuento_mat_icon)
                        , contentDescription = "",
                        modifier = Modifier
                            .padding(20.dp, 18.dp, 22.dp, 4.dp)
                            .size(95.dp)

                    )
                    Text(text = "DESCUENTO\nDE MATERIAL",
                        style = MaterialTheme.typography.subtitle2,
                        fontSize = 12.sp,
                        modifier = Modifier
                            .align(CenterHorizontally)
                            .padding(bottom = 5.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.size(10.dp))
            Box(modifier = Modifier
                .padding(horizontal = 5.dp)
                .height(SizeButtons)
                .clip(RoundedCornerShape(13.dp))
                .background(colorResource(R.color.blackdark))
                .clickable {
                    navController.navigate(route = Destination.InventarioScreen.route)
                }
            ) {
                Column(Modifier.align(Center)) {
                    Image(
                        painter = painterResource(R.drawable.inventario_icon)
                        , contentDescription = "",
                        modifier = Modifier
                            .padding(20.dp, 18.dp, 22.dp, 4.dp)
                            .size(95.dp)

                    )
                    Text(text = "INVENTARIO",
                        style = MaterialTheme.typography.subtitle2,
                        fontSize = 12.sp,
                        modifier = Modifier
                            .align(CenterHorizontally)
                            .padding(bottom = 5.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.size(10.dp))
            Box(modifier = Modifier
                .padding(horizontal = 5.dp)
                .height(SizeButtons)
                .clip(RoundedCornerShape(13.dp))
                .background(colorResource(R.color.graydark))
                .clickable {
                    navController.navigate(route = Destination.RecibirMaterialScreen.route)
                }
            ) {
                Column(Modifier.align(Center)) {
                    Image(
                        painter = painterResource(R.drawable.receptor)
                        , contentDescription = "",
                        modifier = Modifier
                            .padding(20.dp, 18.dp, 22.dp, 4.dp)
                            .size(95.dp)

                    )
                    Text(text = " RECEPCIÓN \nDE MATERIAL",
                        style = MaterialTheme.typography.subtitle2,
                        fontSize = 12.sp,
                        modifier = Modifier
                            .align(CenterHorizontally)
                            .padding(bottom = 5.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.size(10.dp))
            Box(modifier = Modifier
                .padding(horizontal = 5.dp)
                .height(SizeButtons)
                .clip(RoundedCornerShape(13.dp))
                .background(colorResource(R.color.blackdark))
                .clickable {
                    navController.navigate(route = Destination.MaquinaSalaScreen.route)
                }
            ) {
                Column(Modifier.align(Center)) {
                    Image(
                        painter = painterResource(R.drawable.nav_sala_icon)
                        , contentDescription = "",
                        modifier = Modifier
                            .padding(20.dp, 18.dp, 22.dp, 4.dp)
                            .size(95.dp)

                    )
                    Text(text = "INVENTARIO \nMAQUINAS",
                        style = MaterialTheme.typography.subtitle2,
                        fontSize = 12.sp,
                        modifier = Modifier
                            .align(CenterHorizontally)
                            .padding(bottom = 5.dp)
                    )
                }
            }
        }
    }
}