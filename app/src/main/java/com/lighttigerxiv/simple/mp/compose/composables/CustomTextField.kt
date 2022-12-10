package com.lighttigerxiv.simple.mp.compose.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lighttigerxiv.simple.mp.compose.R

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CustomTextField(
    text: String = "",
    placeholder: String = "",
    onTextChange : (newValue: String) -> Unit,
    textType: String = "text",
    sideIcon : Int? = null,
    onSideIconClick : () -> Unit = {},
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    contentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant
) {

    val showClearIcon = remember{ mutableStateOf(false)}
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val textFieldType = when (textType) {

        "text" -> KeyboardType.Text
        "number" -> KeyboardType.Number
        "password" -> KeyboardType.Password
        else -> KeyboardType.Text
    }

    if(text.isEmpty())
        showClearIcon.value = false

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(45.dp)
            .clip(RoundedCornerShape(percent = 100))
            .background(backgroundColor)
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        if(sideIcon != null){

            Icon(
                modifier = Modifier
                    .width(20.dp)
                    .height(20.dp)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { onSideIconClick() },
                painter = painterResource(id = sideIcon),
                contentDescription = null,
                tint = contentColor
            )

            Spacer(Modifier.width(5.dp))
        }

        if(sideIcon == null && showClearIcon.value){

            Spacer(modifier = Modifier.width(20.dp))
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
                    color = contentColor,
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }

            BasicTextField(
                value = text,
                onValueChange = {

                    if (textType == "number") {
                        if (it.toIntOrNull() != null)
                            if (it.toInt() >= 0)
                                onTextChange(it)
                        if(it.isEmpty())
                            onTextChange(it)
                    }
                    else{
                        onTextChange(it)
                    }
                },
                textStyle = TextStyle.Default.copy(
                    color = contentColor,
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                ),
                cursorBrush = Brush.verticalGradient(
                    0.00f to MaterialTheme.colorScheme.primary,
                    1.00f to MaterialTheme.colorScheme.primary
                ),
                maxLines = 1,
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = textFieldType,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        showClearIcon.value = false
                        focusManager.clearFocus()
                        keyboardController?.hide()
                    },
                    onNext = {
                        showClearIcon.value = false
                        focusManager.moveFocus(FocusDirection.Next)
                    }
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusEvent { focusState ->
                        if (focusState.hasFocus && text.isNotEmpty())
                            showClearIcon.value = true
                    }
            )
        }

        if(showClearIcon.value){
            Icon(
                modifier = Modifier
                    .height(20.dp)
                    .width(20.dp)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { onTextChange("") },
                painter = painterResource(id = R.drawable.icon_x_solid),
                contentDescription = null,
                tint = contentColor
            )
        }
        if(!showClearIcon.value && text.isNotEmpty() && sideIcon != null){
            Spacer(modifier = Modifier.width(20.dp))
        }
        if(sideIcon != null && text.isEmpty()){
            Spacer(modifier = Modifier.width(20.dp))
        }
    }
}
