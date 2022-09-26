package com.example.listacomponenteqr.presentation.maquina_list.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.listacomponenteqr.domain.model.Maquina
import com.example.listacomponenteqr.presentation.components.DisplayAlert
import com.example.listacomponenteqr.presentation.maquina_list.MaquinasListViewModel

@Composable
fun MaquinasListItem(
    maquina: Maquina,
    viewModel: MaquinasListViewModel = hiltViewModel()
    ) {
    val checkedState = remember { mutableStateOf(false) }
    DisplayAlert()
    if (viewModel.act.value){
        Card(modifier = Modifier
            .height(110.dp)
            .fillMaxWidth()
            .padding(5.dp)
            .clickable { },
            shape = MaterialTheme.shapes.large)
        {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp, 20.dp, 5.dp, 15.dp)
            ) {
                if (maquina.codigo!! < "1"){
                    Icon(
                        Icons.Filled.Cancel, "",
                        tint = Color.Red,
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(horizontal = 10.dp)
                    )

                    Text(
                        text = "${maquina.Refaccion}",
                        style = MaterialTheme.typography.subtitle2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .align(alignment = Alignment.CenterVertically)
                    )
                }
                if (maquina.codigo > "1") {
                    Checkbox(
                        checked = maquina.Status,
                        onCheckedChange = { checkedState.value = maquina.Status },
                        modifier = Modifier
                            .size(25.dp)
                            .align(alignment = Alignment.CenterVertically)
                    )
                    Text(
                        text = "${maquina.codigo}",
                        style = MaterialTheme.typography.subtitle2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .padding(28.dp, 0.dp, 10.dp, 0.dp)
                            .align(alignment = Alignment.CenterVertically)
                    )
                    Text(
                        text = "${maquina.Refaccion}",
                        style = MaterialTheme.typography.subtitle2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .align(alignment = Alignment.CenterVertically)
                    )
                }
            }
        }
    }
}