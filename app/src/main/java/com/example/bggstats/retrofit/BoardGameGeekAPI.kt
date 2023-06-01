package com.example.bggstats.retrofit

import com.example.bggstats.items.*
import org.xmlpull.v1.XmlPullParser
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


//Retrofit JSON >
/*interface BoardGameGeekParse {
    @GET("1") //https://boardgamegeek.com/browse/boardgame/page/1
    suspend fun getGeneralGameList(): DataItemGeneralGame
}*/

/*interface BoardGameGeekAPI {
        @GET("xmlapi/boardgame/174430") //https://boardgamegeek.com/xmlapi/boardgame/174430
    suspend fun getDetailedGame(): DataItemDetailedGame
}*/

/*
@GET("products/{id}") //https://boardgamegeek.com/xmlapi/boardgame/174430
suspend fun getDetailedGame(@Path("id") id: Int): DataItemDetailedGame
*/

//test
interface ProductAPI {
    @GET("products/1") //https://dummyjson.com/products/1
    suspend fun getProductById(): Product
}
//Retrofit JSON^

//XML >
/*interface TrendingSearchesApi {
    @GET("rss")
    fun getTrendingSearches(@Query("geo") countryCode: String):     Single<TrendingSearchResponseWrapper>
}*/
interface WebService{
    @GET("/xmlapi/boardgame/{id}")
    //@GET("/Integration/StoryRss{id}.xml")
    suspend fun getFeed(@Path("id") id: String): Feed//Feed//XmlPullParser//BoardGame
    //suspend fun getUsers(@Path("id") id: String): Call<Feed>
}

//XML ^
