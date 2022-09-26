package com.example.listacomponenteqr.presentation.maquina_list.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddTask
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.listacomponenteqr.domain.model.ActivateMaquina
import com.example.listacomponenteqr.presentation.activar_maquina.ActMaquinasViewModel

@Composable
fun MaquinasListActivate(
    actmaquinas: ActivateMaquina,
    viewModel: ActMaquinasViewModel = hiltViewModel()
) {
    if (viewModel.n.value==3){
        val color by animateColorAsState(
            when (actmaquinas.n) {
                "0" -> Color.Red
                "1" ->Color.Green
                else ->Color.Red
            }
        )
        val icon =
            if (actmaquinas.n == "0") Icons.Filled.Cancel
            else if(actmaquinas.n == "1") Icons.Filled.AddTask
        else Icons.Filled.Cancel
        val text = if(actmaquinas.n == "0") "¡Por favor intenta escaneando otro QR o ingresando otra serie!"
            else if (actmaquinas.n == "1") "¡Para activar componente escanea QR del componente!"
        else "¡Intenta de nuevo con otra maquina que no se encuentre activada!"
        Divider(color = color, thickness = 3.dp, modifier = Modifier.padding(horizontal = 5.dp))
        Card(modifier = Modifier
            .height(150.dp)
            .fillMaxWidth()
            .padding(horizontal = 5.dp, vertical = 10.dp)
            .clickable { },
            shape = MaterialTheme.shapes.large)
        {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp, 20.dp, 5.dp, 15.dp)
            ) {
                if (actmaquinas.n == "0"||actmaquinas.n == "1"||actmaquinas.n == "2") {
                    Icon(
                        icon, "",
                        tint = color,
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(horizontal = 10.dp)
                    )
                    Column(modifier = Modifier.align(Alignment.CenterVertically)) {
                        Text(
                            text = "${actmaquinas.descripcion}",
                            style = MaterialTheme.typography.subtitle2,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .padding(horizontal = 10.dp)
                                .align(alignment = Alignment.Start)
                        )
                        Spacer(modifier = Modifier.height(15.dp))
                        Text(
                            text = text,
                            style = MaterialTheme.typography.subtitle2,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .padding(horizontal = 10.dp)
                                .align(alignment = Alignment.Start)
                        )
                    }
                }
            }
        }
    }else if (viewModel.n.value==0||viewModel.n.value==1){
        val color by animateColorAsState(
            when (viewModel.n.value) {
                0 -> Color.Red
                else ->Color.Green
            }
        )
        val icon =
            if (viewModel.n.value==0) Icons.Filled.Cancel
            else Icons.Filled.AddTask

        val text = if(viewModel.n.value==0) "¡Por favor intenta escaneando otro QR!"
        else "¡Se activaron correctamente los componentes de la maquina!"

        Divider(color = color, thickness = 3.dp, modifier = Modifier.padding(horizontal = 5.dp))
        Card(modifier = Modifier
            .height(150.dp)
            .fillMaxWidth()
            .padding(horizontal = 5.dp, vertical = 10.dp)
            .clickable { },
            shape = MaterialTheme.shapes.large)
        {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp, 20.dp, 5.dp, 15.dp)
            ) {

                Icon(
                    icon, "",
                    tint = color,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(horizontal = 10.dp)
                )
                Column(modifier = Modifier.align(Alignment.CenterVertically)) {
                    Text(
                        text = viewModel.descripion.value,
                        style = MaterialTheme.typography.subtitle2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .align(alignment = Alignment.Start)
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                    Text(
                        text = text,
                        style = MaterialTheme.typography.subtitle2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .align(alignment = Alignment.Start)
                    )
                }
            }
        }
    }
}