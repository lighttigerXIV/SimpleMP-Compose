package com.lighttigerxiv.simple.mp.compose.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lighttigerxiv.simple.mp.compose.R

@Composable
fun CustomTextField(
    text: String = "",
    placeholder: String = "",
    onValueChanged : (newValue: String) -> Unit,
    textType: String = "text",
    sideIcon : Painter? = null,
    onSideIconClick : () -> Unit = {}
) {

    val clearTextIcon = painterResource(id = R.drawable.icon_x)
    val textFieldType = when(textType){

        "text" -> KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text)
        "number" -> KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        else -> KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text)
    }


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(percent = 100))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .height(50.dp)
    ) {

        if (sideIcon != null) {

            Icon(
                painter = sideIcon,
                contentDescription = "",
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .height(50.dp)
                    .width(50.dp)
                    .padding(15.dp)
                    .clickable(
                        indication = null,
                        interactionSource = remember{ MutableInteractionSource() }
                    ){ onSideIconClick() }
            )
        } else {

            if(text.isNotEmpty()){
                Spacer(modifier = Modifier.width(50.dp))
            }
        }


        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)

        ) {

            if (text.isEmpty()) {

                Text(
                    text = placeholder,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            BasicTextField(
                value = text,
                onValueChange = { onValueChanged(it) },
                textStyle = TextStyle.Default.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                ),
                cursorBrush = Brush.verticalGradient(
                    0.00f to MaterialTheme.colorScheme.primary,
                    1.00f to MaterialTheme.colorScheme.primary
                ),
                maxLines = 1,
                singleLine = true,
                keyboardOptions = textFieldType,
                modifier = Modifier.fillMaxWidth()
            )
        }

        if(sideIcon != null && text.isEmpty()){
            Spacer(modifier = Modifier.width(50.dp))
        }

        if (text.isNotEmpty()) {
            Icon(
                painter = remember { clearTextIcon },
                contentDescription = "",
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .height(50.dp)
                    .width(50.dp)
                    .padding(15.dp)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { onValueChanged("") }
            )
        }
    }
}
