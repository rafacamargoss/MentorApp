package br.com.fiap.mentorapp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import br.com.fiap.mentorapp.CheckSignedIn
import br.com.fiap.mentorapp.CommonProgressSpinner
import br.com.fiap.mentorapp.DestinationScreen
import br.com.fiap.mentorapp.MAViewModel
import br.com.fiap.mentorapp.R
import br.com.fiap.mentorapp.navigateTo
import br.com.fiap.mentorapp.ui.components.PasswordInputComponent
import br.com.fiap.mentorapp.ui.theme.OpenSansBold
import br.com.fiap.mentorapp.ui.theme.OpenSansRegular

@Composable
fun LoginScreen (navController: NavController, vm: MAViewModel) {
    CheckSignedIn(vm = vm, navController = navController)

    val emailState = remember { mutableStateOf(TextFieldValue()) }
    val passwordState = remember { mutableStateOf(TextFieldValue()) }

    val focus = LocalFocusManager.current

    Box() {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.5f)
                    .align(Alignment.CenterHorizontally),
                colors = CardDefaults.cardColors(containerColor = Color(0x00FFFFFF))
            ) {
                Image(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 65.dp, start = 35.dp, end = 35.dp),
                    painter = painterResource(id = R.drawable.mentor_logo),
                    contentDescription = "Logo do app"
                )
            }
            Column(modifier = Modifier.fillMaxWidth()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally),
                    colors = CardDefaults.cardColors(containerColor = Color(0x00FFFFFF))
                ) {
                    Column(
                        modifier = Modifier.padding(
                            vertical = 16.dp, horizontal = 32.dp
                        )
                    ) {

                        Text(
                            text = "Insira seu e-mail e senha",
                            modifier = Modifier.fillMaxWidth(),
                            fontSize = 10.sp,
                            color = colorResource(id = R.color.primary_100),
                            textAlign = TextAlign.Start
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        OutlinedTextField(value = emailState.value,
                            onValueChange = {emailState.value = it},
                            modifier = Modifier
                                .fillMaxWidth(),
                            placeholder = {
                                Text(
                                    text = "E-mail",
                                    fontFamily = OpenSansRegular,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 10.sp,
                                    color = colorResource(id = R.color.primary_75)
                                )
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedBorderColor = colorResource(id = R.color.primary_50),
                                focusedBorderColor = colorResource(id = R.color.primary_100)
                            ),
                            shape = RoundedCornerShape(16.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        PasswordInputComponent(placeholder = "Senha", value = passwordState.value.text, updateValue = {passwordState.value = TextFieldValue(it)})

                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                focus.clearFocus(force = true)
                                vm.onLogin(
                                    emailState.value.text,
                                    passwordState.value.text
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.primary_100))
                        ) {
                            Text(
                                text = "Login",
                                fontWeight = FontWeight.Bold,
                                fontFamily = OpenSansBold,
                                color = Color.White,
                                fontSize = 23.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        ClickableText(
                            text = AnnotatedString(text = "Não tem conta? Cadastre-se!"),
                            onClick = {navigateTo(navController, DestinationScreen.Signup.route)},
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                fontFamily = OpenSansBold,
                                color = colorResource(id = R.color.primary_100),
                                fontSize = 12.sp,
                                textDecoration = TextDecoration.Underline
                            )
                        )
                    }
                }
            }
        }
        val isLoading = vm.inProgress.value
        if (isLoading)
            CommonProgressSpinner()
    }

}