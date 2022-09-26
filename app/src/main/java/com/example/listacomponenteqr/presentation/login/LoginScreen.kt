package com.example.zitrocrm.screens

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.TopCenter
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.listacomponenteqr.R
import com.example.listacomponenteqr.LoginViewModel
import com.example.listacomponenteqr.ui.theme.blacktransp
import com.example.listacomponenteqr.utils.SharedPrefence
import com.example.zitrocrm.screens.login.components.*
/**
 * Created by Brian Fernando Mtz on 02-2022.
 */
@Composable
fun LoginScreen(
    imageError: Boolean,
    onclickLogin: (usuariox: String, pssx: String,context: Context) -> Unit,
    loginViewModel : LoginViewModel
    )
{
    val context = LocalContext.current
    val datastore = SharedPrefence(LocalContext.current)
    val user = datastore.getUserLogin().toString()
    val psw = datastore.getPassLogin().toString()
    if(datastore.getInicioLogin().toBoolean()){
        loginViewModel.checkInicio.value = true
        loginViewModel.login(user,psw,context)
    }
    Scaffold {
        Image(
            painter = painterResource(R.drawable.backgroud_crmm),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.Crop
        )
        Box(modifier = Modifier
            .padding(0.dp, 55.dp, 0.dp, 0.dp)
            .fillMaxSize()
        ){
            Image(
                painter = painterResource(R.drawable.log_qr),
                contentDescription = null,
                modifier = Modifier
                    .size(200.dp)
                    .align(TopCenter),
                contentScale = ContentScale.Fit
            )
        }

        Box(modifier = Modifier.padding(0.dp,280.dp,0.dp,50.dp))
        {
            Spacer(
                Modifier
                    .matchParentSize()
                    .background(color = blacktransp)
            )

            var usuariox by rememberSaveable { mutableStateOf(value = "") }
            var passx by rememberSaveable { mutableStateOf(value = "") }
            val isValidate by derivedStateOf { usuariox.isNotBlank() && passx.isNotBlank() }
            val focusManager = LocalFocusManager.current

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 30.dp, vertical = 10.dp),

            ) {
                Spacer(Modifier.fillMaxHeight(.1f))
                Text(
                    text = "Iniciar Sesión",
                    color = Color.White,
                    fontSize = 25.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    style = MaterialTheme.typography.subtitle2,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(Modifier.fillMaxHeight(.1f))

                EmailOutTextField(
                    textValue = usuariox,
                    onValueChange = { usuariox = it },
                    onClickButton = { usuariox = "" },
                    onNext = { focusManager.moveFocus(FocusDirection.Down)
                    }
                )

                Spacer(Modifier.fillMaxHeight(.1f))

                PasswordOutTextField(
                    textValue = passx,
                    onValueChange = { passx = it },
                    onDone = {
                        focusManager.clearFocus()
                    }
                )
                Spacer(Modifier.fillMaxHeight(.02f))
                val ctxt = LocalContext.current
                Row(
                    Modifier
                        .padding(vertical = 0.dp)
                        .align(Alignment.End)) {
                    Text(text = "Guardar Inicio de Sesión",
                        modifier =
                        Modifier
                            .padding(
                                horizontal = 3.dp
                            )
                            .align(Alignment.CenterVertically),
                        fontSize = 13.sp,
                        textAlign = TextAlign.Start,
                    )
                    Checkbox(
                        checked = loginViewModel.checkInicio.value ,
                        onCheckedChange = {
                            loginViewModel.checkInicio.value = it
                        },
                        colors = CheckboxDefaults.colors(
                            checkedColor = colorResource(R.color.reds),
                            uncheckedColor = colorResource(R.color.blackdark),
                            checkmarkColor = colorResource(R.color.white)
                        )
                    )
                }
                Spacer(Modifier.fillMaxHeight(.02f))

                ButtonLogin(
                    onclick = {onclickLogin(usuariox,passx,ctxt)},
                    enabled = isValidate
                )
                Spacer(Modifier.fillMaxHeight(.1f))
            }
        }
        ProgressBarLoading()
        ErrorImageAuth(isImageValidate = imageError)
    }
}

