package com.lighttigerxiv.simple.mp.compose.frontend.composables

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lighttigerxiv.simple.mp.compose.R
import com.lighttigerxiv.simple.mp.compose.frontend.utils.FontSizes
import com.lighttigerxiv.simple.mp.compose.frontend.utils.Sizes

@SuppressLint("RememberReturnType")
@Composable
fun TextField(
    text: String,
    onTextChange: (text: String) -> Unit,
    placeholder: String = "",
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    hasFollowingField: Boolean = false,
    startIcon: Int? = null,
    onStartIconClick: () -> Unit = {},
    requestFocus: Boolean = false,
    onFocusRequested: () -> Unit = {},
    numberField: Boolean = false
) {

    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    val showClearIcon = remember { mutableStateOf(text.isNotEmpty()) }
    val fieldType = if (numberField) KeyboardType.Number else KeyboardType.Text

    LaunchedEffect(requestFocus) {
        if (requestFocus) {
            focusRequester.requestFocus()
            onFocusRequested()
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .clip(CircleShape)
            .background(backgroundColor)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        if (startIcon !== null) {

            Icon(
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable { onStartIconClick() }
                    .size(20.dp),
                painter = painterResource(id = startIcon),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface
            )

            HSpacer(size = Sizes.MEDIUM)
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f, fill = true)
        ) {
            if (text.isEmpty()) {
                Text(
                    text = placeholder,
                    fontSize = FontSizes.LABEL,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            BasicTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester)
                    .onFocusEvent { focusState ->
                        showClearIcon.value = focusState.hasFocus && text.isNotEmpty()
                    },
                value = text,
                onValueChange = {

                    if (it.isEmpty()) {
                        onTextChange(it)

                    } else if (numberField) {

                        if (it.toIntOrNull() != null) {
                            if (it.toInt() >= 0) {
                                onTextChange(it)
                            }
                        }
                    } else {

                        onTextChange(it)
                    }
                },
                textStyle = TextStyle(
                    color = textColor,
                    fontSize = 14.sp
                ),
                maxLines = 1,
                singleLine = true,
                cursorBrush = Brush.verticalGradient(
                    0.00f to MaterialTheme.colorScheme.primary,
                    1.00f to MaterialTheme.colorScheme.primary
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = fieldType,
                    imeAction = if (hasFollowingField) ImeAction.Next else ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                        showClearIcon.value = false
                    },
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Next)
                        showClearIcon.value = false
                    }
                )
            )
        }

        if (showClearIcon.value) {

            HSpacer(size = Sizes.MEDIUM)

            Icon(
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable { onTextChange("") }
                    .size(20.dp),
                painter = painterResource(id = R.drawable.close),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}