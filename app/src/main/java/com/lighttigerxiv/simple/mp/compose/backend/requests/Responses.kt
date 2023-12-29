package com.lighttigerxiv.simple.mp.compose.backend.requests

data class DiscogsResponse(
    val pagination: Pagination,
    val results: List<Result>
){

    data class Pagination(
        val items: Int,
        val page: Int,
        val pages: Int,
        val per_page: Int,
        val urls: Urls
    ){
        data class Urls(
            val last: String,
            val next: String
        )
    }

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
    ){
        data class UserData(
            val in_collection: Boolean,
            val in_wantlist: Boolean
        )
    }
}