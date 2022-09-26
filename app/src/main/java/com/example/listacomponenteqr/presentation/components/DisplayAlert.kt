package com.example.listacomponenteqr.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddTask
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.listacomponenteqr.presentation.activar_maquina.ActMaquinasViewModel
import com.example.listacomponenteqr.presentation.maquina_list.MaquinasListViewModel

@Composable
fun DisplayAlert(
    viewModel: MaquinasListViewModel = hiltViewModel(),
    viewModel2: ActMaquinasViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
    onDialogStateChange: ((Boolean) -> Unit)? = null,
    onDismissRequest: (() -> Unit)? = null,
    ){
    val textPaddingAll = 24.dp
    val dialogShape = RoundedCornerShape(18.dp)
    /**Alert Dialog Maquinas Lista Components**/
    if (viewModel.alertstate.value) {
        AlertDialog(
            onDismissRequest = {
                onDialogStateChange?.invoke(false)
                onDismissRequest?.invoke()
            },
            title = null,
            text = null,
            buttons = {
                Column{
                    Row(Modifier.padding(all = textPaddingAll)){
                        if (viewModel.alertstatecolor.value==true){
                            Icon(
                                Icons.Filled.AddTask, "",
                                tint = Color.Green,
                                modifier = Modifier
                                    .align(Alignment.CenterVertically)
                                    .padding(horizontal = 10.dp)
                            )
                            Text(text = viewModel.textalert.value)
                        }
                        else if (viewModel.alertstatecolor.value==false){
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
                    if (viewModel.alertstatecolor.value==true){
                        Divider(color = Color.Green, thickness = 3.dp)
                    }
                    if (viewModel.alertstatecolor.value==false){
                        Divider(color = Color.Red, thickness = 3.dp)
                    }
                }

            },
            properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = false),
            modifier = modifier,
            shape = dialogShape
        )
    }
    /**Alert Dialog Maquinas Actvate Components**/
    if (viewModel2.alertstate.value) {
        AlertDialog(
            onDismissRequest = {
                onDialogStateChange?.invoke(false)
                onDismissRequest?.invoke()
            },
            title = null,
            text = null,
            buttons = {
                Row(Modifier.padding(all = textPaddingAll)){
                    if (viewModel2.alertstatecolor.value==true){
                        Icon(
                            Icons.Filled.Refresh, "",
                            tint = Color.Green,
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .padding(horizontal = 10.dp)
                        )
                        Text(text = viewModel2.textalert.value)
                    }
                    else if (viewModel2.alertstatecolor.value==false){
                        Icon(
                            Icons.Filled.Cancel, "",
                            tint = Color.Red,
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .padding(horizontal = 10.dp)
                        )
                        Text(text = viewModel2.textalert.value)
                    }

                }
                if (viewModel2.alertstatecolor.value==true){
                    Divider(color = Color.Green, thickness = 3.dp)
                }
                if (viewModel2.alertstatecolor.value==false){
                    Divider(color = Color.Red, thickness = 3.dp)
                }
            },
            properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = false),
            modifier = modifier,
            shape = dialogShape
        )
    }
}