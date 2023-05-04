package com.example.bggstats.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bggstats.R
import com.example.bggstats.items.DataItemDetailedGame
import com.example.bggstats.items.DataItemGeneralGame

open class ViewModel : ViewModel() {

    var statusProgressBarGeneralGameList = MutableLiveData(false)
    var statusProgressBarDetailedGameList  = MutableLiveData(false)
    var statusVisibilityChart = MutableLiveData(false)
    var statusVisibilityGeneralGameList = MutableLiveData(false)
    var statusVisibilityDetailedGameList = MutableLiveData(false)


    //Список новостей (NewsItem)
    /*val newsItemArrayAll: MutableLiveData<ArrayList<NewsItem>> by lazy { //Список новостей (NewsItem) - "всех"
        MutableLiveData<ArrayList<NewsItem>>()
    }*/

    //Отслеживаем поворот экрана
    val statusLandscape: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val generalGameList = listOf(
        DataItemGeneralGame(1, "Brass: Birmingham"),
        DataItemGeneralGame(2, "Gloomhaven"),
        DataItemGeneralGame(3, "Ark Nova")
    )

    val detailedGameListDraw = listOf(
        R.drawable.pic3490053,
        R.drawable.pic2437871,
        R.drawable.pic6293412
    )

    val detailedGameList = listOf(
        DataItemDetailedGame(R.drawable.pic3490053, 1, "Brass: Birmingham"),
        DataItemDetailedGame(R.drawable.pic2437871, 2, "Gloomhaven"),
        DataItemDetailedGame(R.drawable.pic6293412, 3, "Ark Nova"),
    )
}