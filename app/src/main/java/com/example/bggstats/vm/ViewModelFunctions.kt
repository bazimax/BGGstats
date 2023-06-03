package com.example.bggstats.vm

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import com.example.bggstats.atest.log
import com.example.bggstats.atest.logData
import com.example.bggstats.atest.logDataBig
import com.example.bggstats.atest.logDataEach
import com.example.bggstats.atest.logEnd
import com.example.bggstats.atest.logError
import com.example.bggstats.atest.logInfo
import com.example.bggstats.atest.logStart
import com.example.bggstats.items.Feed
import com.example.bggstats.roomdb.EntityDataItem
import com.example.bggstats.roomdb.MainDb
import com.example.bggstats.view.TAG
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ViewModelFunctions(viewModel: ViewModel) {

    private val lnc = "ViewModelFunctions" //logNameClass - для логов
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



}