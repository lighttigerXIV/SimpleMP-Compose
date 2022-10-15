package com.lighttigerxiv.simple.mp.compose.navigation.screens.setup

import androidx.compose.foundation.Image
import com.lighttigerxiv.simple.mp.compose.R
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material.Text
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun WelcomeScreen(
    onNextClicked: () -> Unit = {}
) {


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .weight(fill = true, weight = 1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = painterResource( id = R.drawable.icon_playstore ),
                contentDescription = "",
                modifier = Modifier
                    .height(100.dp)
                    .width(100.dp)
                    .clip(RoundedCornerShape(percent = 100))
            )

            Spacer(Modifier.height(20.dp))

            Text(
                text = "Welcome to SimpleMP",
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 20.sp,
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(weight = 1f, fill = true)
            )

            Button(
                onClick = {
                    onNextClicked()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {

                Text(
                    text = "Next",
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}