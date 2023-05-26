package com.example.bggstats.vm

import androidx.lifecycle.*
import androidx.lifecycle.ViewModel
import com.example.bggstats.R
import com.example.bggstats.items.BoardGame
import com.example.bggstats.items.DataItemDetailedGameTemp
import com.example.bggstats.items.DataItemGeneralGame
import com.example.bggstats.items.Feed
import com.example.bggstats.retrofit.WebService
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

open class ViewModel : ViewModel() {

    var statusProgressBarGeneralGameList = MutableLiveData(false)
    var statusProgressBarDetailedGameList  = MutableLiveData(false)
    var statusVisibilityChart = MutableLiveData(false)
    var statusVisibilityGeneralGameList = MutableLiveData(false)
    var statusVisibilityDetailedGameList = MutableLiveData(false)

    val detailedGame = MutableLiveData<DataItemDetailedGameTemp>()
    var statusDetailedGame = MutableLiveData(false)


    var testErrorMessage = MutableLiveData("")

    //Список новостей (NewsItem)
    /*val newsItemArrayAll: MutableLiveData<ArrayList<NewsItem>> by lazy { //Список новостей (NewsItem) - "всех"
        MutableLiveData<ArrayList<NewsItem>>()
    }*/

    //Отслеживаем поворот экрана
    val statusLandscape: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val generalGameList = listOf(
        DataItemGeneralGame(1, "Brass: Birmingham", "uri", "shortDescription"),
        DataItemGeneralGame(2, "Gloomhaven", "uri", "shortDescription"),
        DataItemGeneralGame(3, "Ark Nova", "uri", "shortDescription")
    )

    val detailedGameListDraw = listOf(
        R.drawable.pic3490053,
        R.drawable.pic2437871,
        R.drawable.pic6293412
    )

    val detailedGameList = listOf(
        DataItemDetailedGameTemp(R.drawable.pic3490053, 1, "Brass: Birmingham"),
        DataItemDetailedGameTemp(R.drawable.pic2437871, 2, "Gloomhaven"),
        DataItemDetailedGameTemp(R.drawable.pic6293412, 3, "Ark Nova"),
    )
}

/*interface Repository {
    val feedLiveData: LiveData<Feed>

    suspend fun fetchFeed(id: String)
}*/

/*@HiltViewModel
class ItemsViewModel  @Inject constructor(
    private val repository: Repository
): ViewModel()
{
    *//*
    Map is 1 to 1 mapping which is easy to understand.
    SwitchMap on the other hand only mapping the most recent value at a time to reduce unnecessary compute.
     *//*

    val feedLiveData: LiveData<List<BoardGame>> =  Transformations.map(repository.feedLiveData) { items ->
        items?.boardGameList
    }

    fun fetchFeed(id: String) {
        viewModelScope.launch {
            repository.fetchFeed(id = id)
        }
    }

    *//*
    * If the user leaves the screen before getList(...) returns a value,
    *  the system calls onCleared and the ViewModel’s CoroutineScope will be canceled.
    *  This will make sure that the movieListData.value will not be modified,
    * and the code will not attempt to update a no longer existing screen.*//*
    override fun onCleared() {
        viewModelScope.cancel()
    }
}*/

/*
@ActivityRetainedScoped
class RepositoryImpl  @Inject constructor(
    private val webService: WebService
): Repository {
    override val feedLiveData: MutableLiveData<Feed> = MutableLiveData()

    override suspend fun fetchFeed(id: String) {
        val feed = try {
            webService.getFeed(id)
        } catch (cause: Throwable) {
            cause.printStackTrace()
            throw cause
        }
        return   feedLiveData.postValue(feed)
    }
}*/
