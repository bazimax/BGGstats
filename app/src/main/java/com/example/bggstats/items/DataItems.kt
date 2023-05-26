package com.example.bggstats.items

data class DataItemGeneralGame(
    val id: Int,
    val name: String,
    val link: String,
    val shortDescription: String
)

data class DataItemDetailedGameTemp(
    val imageId: Int, //uri
    val id: Int,
    val title: String
)

data class DataItemDetailedGame(
    val objectId: Int, //...
    val link: String,
    val rank: Int //3
    /*val image: Int, //uri
    val name: String, //Gloomhaven
    val shortDescription: String,
    val description: String, //Vanquish monsters with strategic cardplay. Fulfill your quest to leave your legacy!
    val yearPublished: Int, //2017 yearPublished //yearReleased
    val category: List<String>, //Adventure/Exploration/Fantasy/Fighting/Miniatures
    val mechanism: List<String>, //Grid Movement/Hand Management/Hexagon Grid/Legacy Game/Modular Board
    val family: List<String>, //Category: Dungeon Crawler/Components: Miniatures/Mechanism: Legacy
    val minPlayers: Int, //1-4
    val maxPlayers: Int,
    val playingTime: Int, //60–120 Min
    val minPlaytime: Int,
    val maxPlaytime: Int,
    val age: Int, //14+
    val price: Int,
    val designer: String,
    val artist: String,
    val publisher: String,
    val EXPANSION: Boolean*/
)

data class Product(
    val id: Int,
    val title: String,
    val description : String,
    val price: Int,
    val discountPercentage: Float,
    val rating: Float,
    val stock: Int,
    val brand: String,
    val category: String,
    val thumbnail: String,
    val images: List<String>
)