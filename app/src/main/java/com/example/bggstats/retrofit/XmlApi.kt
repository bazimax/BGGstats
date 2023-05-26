package com.example.bggstats.retrofit

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "boardgames", strict = false) //rss
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
)

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
