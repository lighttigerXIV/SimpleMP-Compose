package com.lighttigerxiv.simple.mp.compose.responses

data class Result(
    val cover_image: String,
    val id: Int,
    val master_id: Any,
    val master_url: Any,
    val resource_url: String,
    val thumb: String,
    val title: String,
    val type: String,
    val uri: String,
    val user_data: UserData
)