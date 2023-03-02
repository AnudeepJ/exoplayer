package com.example.myapplication.ui.main

data class MediaItemDto(
    val id: Int,
    val url: String,
    val imageUrl: String,
    var state: Int = -1,
    val title: String = ""
)
