package com.lighttigerxiv.simple.mp.compose

import com.lighttigerxiv.simple.mp.compose.responses.Pagination
import com.lighttigerxiv.simple.mp.compose.responses.Result

data class DiscogsResponse(
    val pagination: Pagination,
    val results: List<Result>
)