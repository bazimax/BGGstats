package com.example.bggstats.items

data class DataItemGeneralGame(
    val id: Int,
    val title: String
)

data class DataItemDetailedGame(
    val imageId: Int, //uri
    val id: Int,
    val title: String
)