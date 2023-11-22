package com.lighttigerxiv.simple.mp.compose.frontend.composables

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun CollapsableHeader(
    header: @Composable ColumnScope.() -> Unit,
    content: @Composable ColumnScope.() -> Unit
){

    BoxWithConstraints{

        val boxHeight = maxHeight

        LazyColumn(modifier = Modifier.height(boxHeight)) {

            item(key = "header") {
                Column { header() }
            }

            item(key = "tabRow") {

                Column(modifier = Modifier.height(boxHeight)){ content() }
            }
        }
    }
}