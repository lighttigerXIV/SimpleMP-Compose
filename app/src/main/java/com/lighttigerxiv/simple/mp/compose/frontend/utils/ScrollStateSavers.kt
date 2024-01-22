package com.lighttigerxiv.simple.mp.compose.frontend.utils

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect

@Composable
fun customRememberLazyListState(index: Int, onKill: (index: Int) -> Unit): LazyListState {
    val scrollState = rememberLazyListState(index)
    DisposableEffect(key1 = null) {
        onDispose {
            onKill(scrollState.firstVisibleItemIndex)
        }
    }
    return scrollState
}

@Composable
fun customRememberLazyGridState(index: Int, onKill: (index: Int) -> Unit): LazyGridState {
    val scrollState = rememberLazyGridState(index)
    DisposableEffect(key1 = null) {
        onDispose {
            onKill(scrollState.firstVisibleItemIndex)
        }
    }
    return scrollState
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun customRememberPagerState(
    index: Int,
    pageCount: Int = 2,
    onKill: (index: Int) -> Unit
): PagerState {
    val state = rememberPagerState(pageCount = { pageCount }, initialPage = index)

    DisposableEffect(key1 = null) {
        onDispose {
            onKill(state.settledPage)
        }
    }

    return state
}