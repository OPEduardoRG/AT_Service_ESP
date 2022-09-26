package com.example.zitrocrm.screens.login.components

import android.os.Build.VERSION.SDK_INT
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.rememberImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import coil.size.OriginalSize
import com.example.listacomponenteqr.R
import com.example.listacomponenteqr.common.Constants
import com.example.listacomponenteqr.ui.theme.reds

val progressBar = mutableStateOf(false)
val Loading = mutableStateOf(false)
val Error = mutableStateOf(false)
val StringProgres = mutableStateOf("")

@Composable
fun ProgressBarLoading(
) {
    if (progressBar.value) {
        AlertDialog(
            backgroundColor = Color.Transparent,
            onDismissRequest = {
            },
            confirmButton = {
                Box(modifier = Modifier
                    .fillMaxSize()

                ) {
                    if(Constants.b){
                        val context = LocalContext.current
                        val imageLoader = ImageLoader.Builder(context)
                            .componentRegistry {
                                if (SDK_INT >= 28) {
                                    add(ImageDecoderDecoder(context))
                                } else {
                                    add(GifDecoder())
                                }
                            }
                            .build()
                        Image(
                            painter = rememberImagePainter(
                                ImageRequest.Builder(context).data(data = R.drawable.xy).apply(block = {
                                    size(OriginalSize)
                                }).build(), imageLoader = imageLoader
                            ),
                            contentDescription = null,
                            modifier = Modifier.fillMaxWidth().align(Center).size(180.dp),
                        )
                    }else{
                        val infiniteTransition = rememberInfiniteTransition()
                        val animationValues = (1..5).map { index ->
                            infiniteTransition.animateFloat(
                                initialValue = 0f,
                                targetValue = 12f,
                                animationSpec = infiniteRepeatable(
                                    animation = tween(
                                        durationMillis = 300,
                                        delayMillis = 70 * index,
                                    ),
                                    repeatMode = RepeatMode.Reverse,
                                )
                            )
                        }
                        Row(Modifier.align(Center)) {
                            animationValues.forEach { animatedValue ->
                                Box(
                                    modifier = Modifier
                                        .padding(horizontal = 4.dp)
                                        .offset(y = animatedValue.value.dp)
                                        .clip(RoundedCornerShape(25.dp))
                                        .background(reds)
                                        .align(CenterVertically),
                                ) {
                                    Spacer(modifier = Modifier.size(30.dp))
                                }
                            }
                        }
                        Text(
                            text = "Cargando..",
                            style = MaterialTheme.typography.subtitle2,
                            modifier = Modifier.padding(top = 85.dp).align(Center)
                        )
                    }
                }
            }
        )
    }
    if (Loading.value) {
        AlertDialog(
            onDismissRequest = {
            },
            confirmButton = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.85f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.size(30.dp))
                    CircularProgressIndicator(
                        color = Color.White,
                        strokeWidth = 6.dp,
                        modifier = Modifier.size(60.dp)
                    )
                    Spacer(modifier = Modifier.size(15.dp))
                    Text(
                        text = "¡ Listo !",
                        style = MaterialTheme.typography.subtitle2,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    Spacer(modifier = Modifier.size(30.dp))
                }
            },Modifier.padding(horizontal = 20.dp)
        )
    }
    if (Error.value) {
        AlertDialog(
            onDismissRequest = {
            },
            confirmButton = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.85f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.size(25.dp))
                    /*CircularProgressIndicator(
                        color = Color.Red,
                        strokeWidth = 6.dp,
                        modifier = Modifier.size(60.dp)
                    )*/
                    Icon(
                        Icons.Filled.Cancel, contentDescription = "",
                        Modifier
                            .size(60.dp)
                            .align(Alignment.CenterHorizontally))
                    Spacer(modifier = Modifier.size(15.dp))
                    Text(
                        text = "Error..",
                        style = MaterialTheme.typography.subtitle2,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    Text(
                        text = StringProgres.value,
                        style = MaterialTheme.typography.subtitle2,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(horizontal = 15.dp)
                    )
                    Spacer(modifier = Modifier.size(25.dp))
                }
            },Modifier.padding(horizontal = 20.dp)
        )
    }
}

