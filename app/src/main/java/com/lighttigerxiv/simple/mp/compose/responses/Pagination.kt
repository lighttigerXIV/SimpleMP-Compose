package com.lighttigerxiv.simple.mp.compose.responses

import com.lighttigerxiv.simple.mp.compose.responses.Urls

data class Pagination(
    val items: Int,
    val page: Int,
    val pages: Int,
    val per_page: Int,
    val urls: Urls
)