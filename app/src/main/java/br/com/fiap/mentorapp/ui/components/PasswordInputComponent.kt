package br.com.fiap.mentorapp.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.fiap.mentorapp.R
import br.com.fiap.mentorapp.ui.theme.OpenSansSemiBold

@Composable
fun PasswordInputComponent(
    value: String,
    placeholder: String,
    updateValue: (String) -> Unit
) {
    var passwordVisibility by remember { mutableStateOf(false) }

    val icon = if (passwordVisibility)
        painterResource(id = R.drawable.visibility)
    else
        painterResource(id = R.drawable.visibility_off)

    OutlinedTextField(
        value = value,
        onValueChange = updateValue,
        modifier = Modifier.fillMaxWidth(),
        placeholder = {
            Text(
                text = placeholder,
                fontFamily = OpenSansSemiBold,
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
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        trailingIcon = {
            IconButton(onClick = {passwordVisibility = !passwordVisibility}){
                Icon(
                    painter = icon,
                    contentDescription = null
                )
            }
        },
        visualTransformation = if(passwordVisibility) VisualTransformation.None else PasswordVisualTransformation()
    )
}