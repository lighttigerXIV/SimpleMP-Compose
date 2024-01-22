package com.lighttigerxiv.simple.mp.compose.frontend.composables

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import com.lighttigerxiv.simple.mp.compose.frontend.utils.Sizes
import com.lighttigerxiv.simple.mp.compose.frontend.utils.modifyIf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DoubleTabRow(
    pagerState: PagerState,
    firstTabTitle: String,
    secondTabTitle: String
) {

    val scope = rememberCoroutineScope()

    Row(
        modifier = Modifier.fillMaxWidth(),
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f, fill = true)
                .clip(CircleShape)
                .modifyIf(pagerState.currentPage == 0) {
                    background(MaterialTheme.colorScheme.surfaceVariant)
                }
                .clickable {
                    scope.launch {
                        withContext(Dispatchers.Main) {
                            pagerState.scrollToPage(0)
                        }
                    }
                }
                .padding(Sizes.MEDIUM),
            horizontalArrangement = Arrangement.Center
        ) {

            Text(
                text = firstTabTitle,
                fontWeight = FontWeight.Medium,
                color = if (pagerState.currentPage == 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f, fill = true)
                .clip(CircleShape)
                .modifyIf(pagerState.currentPage == 1) {
                    background(MaterialTheme.colorScheme.surfaceVariant)
                }
                .clickable {
                    scope.launch {
                        withContext(Dispatchers.Main) {
                            pagerState.scrollToPage(1)
                        }
                    }
                }
                .padding(Sizes.MEDIUM),
            horizontalArrangement = Arrangement.Center
        ) {

            Text(
                text = secondTabTitle,
                fontWeight = FontWeight.Medium,
                color = if (pagerState.currentPage == 1) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}