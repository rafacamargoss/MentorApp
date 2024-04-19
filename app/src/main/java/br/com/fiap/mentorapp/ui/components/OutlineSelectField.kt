package br.com.fiap.mentorapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import br.com.fiap.mentorapp.R
import br.com.fiap.mentorapp.ui.theme.OpenSansSemiBold

data class SelectOptionsType(val label: String, val value: String)
@Composable
fun OutlineSelectField(
    items: List<SelectOptionsType>,
    selectedItem: String,
    onSelectedItemChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }
    var textFiledSize by remember { mutableStateOf(Size.Zero) }
    val icon = if (expanded) {
        Icons.Filled.KeyboardArrowUp
    }else {
        Icons.Filled.KeyboardArrowDown
    }

    OutlinedTextField(
        value = selectedItem,
        onValueChange = onSelectedItemChange,
        label = { Text(text = label) },
        modifier = modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded }
            .onGloballyPositioned { coordinates ->
                textFiledSize = coordinates.size.toSize()
            },
        readOnly = true,
        trailingIcon = {
               Icon(icon, "", Modifier.clickable { expanded = !expanded })
        },
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = colorResource(id = R.color.primary_50),
            focusedBorderColor = colorResource(id = R.color.primary_100)
        ),
    )

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 2.dp)
            .background(Color.Transparent)
    ) {
        items.forEach { item ->
            DropdownMenuItem(
                modifier = Modifier
                    .background(Color(0xFFF3C5C5))
                    ,
                text = {
                    Text(
                        text = item.label,
                        fontFamily = OpenSansSemiBold,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp,
                        color = colorResource(id = R.color.background_black)
                    )
                },
                onClick = {
                onSelectedItemChange(item.value)
                expanded = false
            })
            Spacer(modifier = Modifier.height(2.dp))
        }
    }
}
