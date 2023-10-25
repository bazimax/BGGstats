package com.example.bggstats.items

import org.simpleframework.xml.*

//корневой каталог
@Root(name = "boardgames", strict = false)
data class Feed  @JvmOverloads constructor (

    @field:Attribute
    var termsofuse: String? = null,

    @field:ElementList(name = "boardgame", inline = true) //item //inline = true,
    var boardGameList: List<BoardGame>? = null
)

//отдельно по каждой игре
@Root(name = "boardgame", strict = false) //item
data class BoardGame  @JvmOverloads constructor(

    //временно скрыл
    /*@field:Element(name = "image", required = false) //title
    //@field:Path("boardgame")
    var image: String? = null,*/

    @field:Element(name = "yearpublished", required = false) //link
    //@field:Path("boardgame")
    var yearpublished: Int? = null,

    //временно скрыл
    /*@field:Element(name = "description", required = false) //description
    //@field:Path("boardgame")
    var description: String? = null,*/

    @field:Attribute
    var objectid: Int? = null,

    /*@field:ElementList(name = "boardgamehonor", entry = "boardgamehonor", inline = true)
    var boardGameHonorList: List<BoardGameHonor>? = null,*/

    @field:ElementList(name = "name", required = false, entry = "name", inline = true)
    var nameList: List<Name>? = null


)

/*@Root(name = "poll", strict = false)
data class PollName  @JvmOverloads constructor(
    @field:Element(name = "suggested_numplayers", required = false)  var poll: String? = null
)*/

//все что ниже - дополнительное погружение - отдельные списки элментов и тд
@Root(name = "name", strict = false)
data class Name  @JvmOverloads constructor(
    @field:Text
    var name: String? = null
)

@Root(name = "boardgamehonor", strict = false)
data class BoardGameHonor  @JvmOverloads constructor(
    @field:Text
    var boardgamehonor: String? = null
)

//BACKUP

//Delete? >
/*@Root(name = "boardgames", strict = false) //rss
class TrendingSearchResponseWrapper @JvmOverloads constructor(
    @field: Element(name = "boardgame") //channel
    var channel: TrendingSearchResponse? = null
)

@Root(name = "boardgames", strict = false) //channel
class TrendingSearchResponse @JvmOverloads constructor(
    @field: ElementList(inline = true)
    var itemList: List<TrendingSearchItem>? = null
)

@Root(name = "boardgame", strict = false) //item
class TrendingSearchItem @JvmOverloads constructor(
    @field: Element(name = "title")
    var title: String = "",
    @field: Element(name = "description", required = false)
    var description: String = "",
    @field: Element(name = "link")
    var link: String = ""
)*/

/*
@Root(name = "rss", strict = false)
class TrendingSearchResponseWrapper @JvmOverloads constructor(
    @field: Element(name = "channel")
    var channel: TrendingSearchResponse? = null
)

@Root(name = "channel", strict = false)
class TrendingSearchResponse @JvmOverloads constructor(
    @field: ElementList(inline = true)
    var itemList: List<TrendingSearchItem>? = null
)

@Root(name = "item", strict = false)
class TrendingSearchItem @JvmOverloads constructor(
    @field: Element(name = "title")
    var title: String = "",
    @field: Element(name = "description", required = false)
    var description: String = "",
    @field: Element(name = "link")
    var link: String = ""
)*/

