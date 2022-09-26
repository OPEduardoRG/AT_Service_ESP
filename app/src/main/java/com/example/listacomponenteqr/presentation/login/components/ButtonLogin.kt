package com.example.zitrocrm.screens.login.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.listacomponenteqr.R

@Composable
fun ButtonLogin(
    onclick: () -> Unit,
    enabled: Boolean
) {
    Button(
        onClick = onclick,
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        elevation = ButtonDefaults.elevation(defaultElevation = 5.dp),
        enabled = enabled,
        shape = RoundedCornerShape(10),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = colorResource(id = R.color.reds)
        )
    ) {
        Text(
            text = "Iniciar",
            fontSize = 20.sp,
            color = Color.White
        )
    }
}
