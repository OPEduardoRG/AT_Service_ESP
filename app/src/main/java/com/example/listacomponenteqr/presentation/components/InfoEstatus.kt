package com.example.listacomponenteqr.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.example.listacomponenteqr.R

@Composable
fun InfoEstatus(
    Info : MutableState<Boolean>,
    tittle : String,
    string : String
){
    AnimatedVisibility(visible = Info.value) {
        AlertDialog(
            onDismissRequest = {
                Info.value = false
            },
            title = null,
            text = null,
            buttons = {
                Column{
                    Row(Modifier.padding(vertical = 15.dp, horizontal = 5.dp)){
                        Icon(Icons.Filled.Info,"", modifier = Modifier.padding(horizontal = 12.dp))
                        Text(
                            text =  tittle,
                            style = MaterialTheme.typography.subtitle2,
                            fontSize = 16.sp,
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                        )
                    }
                    Text(
                        text = string,
                        style = MaterialTheme.typography.subtitle2,
                        color = Color.White,
                        fontSize = 12.sp,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally).padding(horizontal = 15.dp, vertical = 5.dp)
                    )
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .padding(5.dp)
                        .clip(shape = RoundedCornerShape(15.dp))
                        .background(colorResource(id = R.color.reds))
                        .clickable {
                            Info.value = false
                        }
                    ){
                        Text(
                            text = "Cerrar",
                            style = MaterialTheme.typography.subtitle2,
                            color = Color.White,
                            fontSize = 15.sp,
                            modifier = Modifier
                                .align(Alignment.Center)
                        )
                    }
                }
            },
            properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = false),
            modifier = Modifier.clip(RoundedCornerShape(25.dp)),
            shape = RoundedCornerShape(18.dp)
        )
    }

}