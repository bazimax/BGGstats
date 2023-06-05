package com.example.bggstats.vm

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.asLiveData
import com.example.bggstats.atest.MyLog
import com.example.bggstats.atest.log
import com.example.bggstats.atest.logData
import com.example.bggstats.atest.logDataBig
import com.example.bggstats.atest.logDataEach
import com.example.bggstats.atest.logEnd
import com.example.bggstats.atest.logError
import com.example.bggstats.atest.logInfo
import com.example.bggstats.atest.logStart
import com.example.bggstats.items.DataItemGeneralGame
import com.example.bggstats.items.Feed
import com.example.bggstats.roomdb.EntityDataItem
import com.example.bggstats.roomdb.MainDb
import com.example.bggstats.view.TAG
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

const val lnc = "ViewModelFunctions" //logNameClass - для логов
class ViewModelFunctions(viewModel: ViewModel) {

    //private val lnc1 = "ViewModelFunctions" //logNameClass - для логов
    private val vm = viewModel

    //следим за изменениями в ViewModel\DataModel
    fun observeVM(dataBase: MainDb, owner: LifecycleOwner) {


        //подключаем observe

        //передаем данные в Room/SQLite
        vm.boardGameFeedFromRetrofit.observe(owner) {
            logStart(lnc, "boardGameFeedFromRetrofit.OBSERVE",
            "следим за изменениями в DataModel(ViewModel) и передаем их в Room")
            boardGameFeedViewModelToRoom(dataBase)
        }

        //WORK (посмотреть что будет с большим количеством элементов)
        //следим за изменениями в БД, читаем данные из БД и записываем в ViewModel
        boardGameRoomToViewModel(dataBase, owner, vm)
    }

    //переносим полученные подробные данные по играм от BGG из ViewModel в БД-Room
    private fun boardGameFeedViewModelToRoom(dataBase: MainDb){
        logStart(lnc, "boardGameFeedViewModelToRoom")
        CoroutineScope(Dispatchers.IO).launch {
            logStart(lnc, "boardGameFeedViewModelToRoom > Coroutine")
            //если данные есть
            if (vm.boardGameFeedFromRetrofit.value != null){
                val bggAPIDetailedGame = vm.boardGameFeedFromRetrofit.value
                log(lnc, "boardGameFeedViewModelToRoom > Coroutine", "данные есть")

                //если полученные данные не пустые (есть хотя бы одна игра), записываем их в таблицу
                if (bggAPIDetailedGame!!.boardGameList?.size != 0) {
                    log(lnc, "boardGameFeedViewModelToRoom > Coroutine", "данные не пустые")

                    val boardGameList = bggAPIDetailedGame.boardGameList
                    //получаем доступ к БД
                    val db = dataBase.getDao()
                    //WORK
                    //!! Важно - очищаем БД перед записью ()
                    db.deleteAll()
                    logError(lnc, "boardGameFeedViewModelToRoom > Coroutine", "База очищена!")

                    //каждую настолку проверяем на совпадения и записываем в БД
                    //!! сделать проверку
                    boardGameList?.forEach { item ->
                        logDataEach(lnc, "boardGameFeedViewModelToRoom > Coroutine > boardGameList", "nameList: ${item.nameList}")

                        val listName: MutableList<String> = mutableListOf()//List<String> = emptyList()
                        item.nameList?.forEach { list ->
                            logDataEach(lnc, "boardGameFeedViewModelToRoom > Coroutine > boardGameList > nameList", "name: ${list.name}")
                            listName.add(list.name.toString())
                        }
                        val string = listName.joinToString("%%X!!##")
                        logDataEach(lnc, "boardGameFeedViewModelToRoom > Coroutine > boardGameList", "names as String: $string")

                        val tempItem = EntityDataItem(
                            null,
                            item.objectid ?: -1,
                            item.yearpublished ?: -1,
                            string //?: "listOf("none")" //item.nameList
                        )

                        db.insertItem(tempItem)
                        val a = item.yearpublished
                        val b = item.objectid
                        val c = item.nameList?.size
                        logDataEach(lnc, "boardGameFeedViewModelToRoom > Coroutine > boardGameList", "---- $a, $b, $c")
                    }
                }
            }
            logEnd(lnc, "boardGameFeedViewModelToRoom > Coroutine")
        }
        logEnd(lnc, "boardGameFeedViewModelToRoom")
    }

    //записываем полученные подробные данные по играм от BGG во ViewModel
    fun boardGameFeedToViewModel(feed: Feed){
        logStart(lnc, "boardGameFeedToViewModel",
            "записываем полученные подробные данные по играм от BGG во ViewModel")
        logDataBig(lnc, "boardGameFeedToViewModel", "$feed")

        vm.boardGameFeedFromRetrofit.postValue(feed)
        //vm.boardGameFeedFromRetrofit.value = feed

        logEnd(lnc, "boardGameFeedToViewModel", "данные во ViewModel записаны")
    }


    //WORK (посмотреть что будет с большим количеством элементов)
    //следим за изменениями в БД, читаем данные из БД и записываем в ViewModel
    fun boardGameRoomToViewModel(dataBase: MainDb, owner: LifecycleOwner, viewModel: ViewModel){
        val logBGRTVM = MyLog(lnc, "boardGameRoomToViewModel", "следим за изменениями в БД, читаем данные из БД и записываем в ViewModel")

        //для каждого элемента БД (EntityDataItem)
        dataBase.getDao().getAllItems().asLiveData().observe(owner){ list ->
            val logBGRTVMDataBase = MyLog(lnc, "boardGameRoomToViewModel > dataBase.OBSERVE", "для каждого элемента БД (EntityDataItem)")

            val listName: MutableList<DataItemGeneralGame> = mutableListOf()//List<String> = emptyList()
            //записываем каждый элемент в список (преобразуя список имен)
            list.forEach {
                it.nameList.replace("%%X!!##", " , ")
                val dataItemGeneralGame = DataItemGeneralGame(it.idBGG, it.nameList, "uri", it.yearPublished.toString())
                logBGRTVMDataBase.eachData("$dataItemGeneralGame")

                listName.add(dataItemGeneralGame)
            }
            //передаем список в ViewModel
            viewModel.generalGameList.postValue(listName)
            //??viewModel.generalGameList.value = listName
            logBGRTVMDataBase.end()
        }
        logBGRTVM.end()
    }

    fun boardGameRoomToViewModelCoroutine(dataBase: MainDb, owner: LifecycleOwner, viewModel: ViewModel){
        val logBGRTVM = MyLog(lnc, "boardGameRoomToViewModelCoroutine", "следим за изменениями в БД, читаем данные из БД и записываем в ViewModel")

        CoroutineScope(Dispatchers.IO).launch {
            //для каждого элемента БД (EntityDataItem)
            dataBase.getDao().getAllItems().asLiveData().observe(owner){ list ->
                val logBGRTVMDataBase = MyLog(lnc, "boardGameRoomToViewModel > dataBase.OBSERVE", "для каждого элемента БД (EntityDataItem)")

                val listName: MutableList<DataItemGeneralGame> = mutableListOf()//List<String> = emptyList()
                //записываем каждый элемент в список (преобразуя список имен)
                list.forEach {
                    it.nameList.replace("%%X!!##", " , ")
                    val dataItemGeneralGame = DataItemGeneralGame(it.idBGG, it.nameList, "uri", it.yearPublished.toString())
                    logBGRTVMDataBase.eachData("$dataItemGeneralGame")

                    listName.add(dataItemGeneralGame)
                }
                //передаем список в ViewModel
                viewModel.generalGameList.postValue(listName)
                //??viewModel.generalGameList.value = listName
                logBGRTVMDataBase.end()
            }
        }
        logBGRTVM.end()
    }
}

