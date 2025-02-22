package com.example.bggstats.vm

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.asLiveData
import com.example.bggstats.atest.MyLog
import com.example.bggstats.items.DataItemGeneralGame
import com.example.bggstats.items.Feed
import com.example.bggstats.roomdb.EntityDataItem
import com.example.bggstats.roomdb.MainDb
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

const val CN = "ViewModelFunctions" //className - для логов
class ViewModelFunctions(viewModel: ViewModel) {

    //private val CN1 = "ViewModelFunctions" //className - для логов
    private val vm = viewModel

    //следим за изменениями в ViewModel\DataModel
    fun observeVM(dataBase: MainDb, owner: LifecycleOwner) {


        //подключаем observe

        //передаем данные в Room/SQLite
        vm.boardGameFeedFromRetrofit.observe(owner) {
            val logObserve = MyLog(CN, "observeVM",
                msgStart = "следим за изменениями в DataModel(ViewModel) и передаем их в Room" +
                        "< boardGameFeedFromRetrofit.OBSERVE")

            boardGameFeedViewModelToRoom(dataBase)
        }

        //WORK (посмотреть что будет с большим количеством элементов)
        //следим за изменениями в БД, читаем данные из БД и записываем в ViewModel
        boardGameRoomToViewModel(dataBase, owner, vm)
    }

    //переносим полученные подробные данные по играм от BGG из ViewModel в БД-Room
    private fun boardGameFeedViewModelToRoom(dataBase: MainDb){
        val log = MyLog(CN, "boardGameFeedViewModelToRoom")

        CoroutineScope(Dispatchers.IO).launch {
            val logCoroutine = MyLog(log, childFunction = "Coroutine", working = false)

            //если данные есть
            if (vm.boardGameFeedFromRetrofit.value != null){
                val bggAPIDetailedGame = vm.boardGameFeedFromRetrofit.value

                logCoroutine.d("данные есть")

                //если полученные данные не пустые (есть хотя бы одна игра), записываем их в таблицу
                if (bggAPIDetailedGame!!.boardGameList?.size != 0) {

                    logCoroutine.d("данные не пустые")

                    val boardGameList = bggAPIDetailedGame.boardGameList
                    //получаем доступ к БД
                    val db = dataBase.getDao()

                    //WORK
                    //!! Important - очищаем БД перед записью ()
                    db.deleteAll()
                    logCoroutine.error("База очищена!")


                    //каждую настолку проверяем на совпадения и записываем в БД
                    //!! сделать проверку
                    boardGameList?.forEach { item ->
                        logCoroutine.eachData("nameList: ${item.nameList}", childFunction = "boardGameList")

                        val listName: MutableList<String> = mutableListOf()//List<String> = emptyList()
                        item.nameList?.forEach { list ->
                            logCoroutine.eachData("name: ${list.name}", childFunction = "nameList > boardGameList")
                            listName.add(list.name.toString())
                        }
                        val string = listName.joinToString("%%X!!##")
                        logCoroutine.eachData("names as String: $string", childFunction = "boardGameList")

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
                        logCoroutine.eachData("---- $a, $b, $c", childFunction = "boardGameList")
                    }
                }
            }
            logCoroutine.end()
        }
        log.end()
    }

    //записываем полученные подробные данные по играм от BGG во ViewModel
    fun boardGameFeedToViewModel(feed: Feed){
        val log = MyLog(CN, "boardGameFeedToViewModel",
            msgStart = "записываем полученные подробные данные по играм от BGG во ViewModel")
        log.bigData("$feed")

        vm.boardGameFeedFromRetrofit.postValue(feed)
        //vm.boardGameFeedFromRetrofit.value = feed

        log.end("данные во ViewModel записаны")
    }


    //WORK (посмотреть что будет с большим количеством элементов)
    //следим за изменениями в БД, читаем данные из БД и записываем в ViewModel
    fun boardGameRoomToViewModel(dataBase: MainDb, owner: LifecycleOwner, viewModel: ViewModel){
        val log = MyLog(CN, "boardGameRoomToViewModel",
            msgStart = "следим за изменениями в БД, читаем данные из БД и записываем в ViewModel")

        //для каждого элемента БД (EntityDataItem)
        dataBase.getDao().getAllItems().asLiveData().observe(owner){ list ->
            val logDao = MyLog(log, "dataBase.OBSERVE",
                msgStart = "для каждого элемента БД (EntityDataItem)",
                working = false)

            val listName: MutableList<DataItemGeneralGame> = mutableListOf()//List<String> = emptyList()
            //записываем каждый элемент в список (преобразуя список имен)
            list.forEach {
                it.nameList.replace("%%X!!##", " , ")
                val dataItemGeneralGame = DataItemGeneralGame(it.idBGG, it.nameList, "uri", it.yearPublished.toString())
                logDao.eachData("$dataItemGeneralGame")

                listName.add(dataItemGeneralGame)
            }
            //передаем список в ViewModel
            viewModel.generalGameList.postValue(listName)
            //??viewModel.generalGameList.value = listName
            logDao.end()
        }
        log.end()
    }

    fun boardGameRoomToViewModelCoroutine(dataBase: MainDb, owner: LifecycleOwner, viewModel: ViewModel){
        val log = MyLog(CN, "boardGameRoomToViewModelCoroutine",
            msgStart = "следим за изменениями в БД, читаем данные из БД и записываем в ViewModel")

        CoroutineScope(Dispatchers.IO).launch {
            //для каждого элемента БД (EntityDataItem)
            dataBase.getDao().getAllItems().asLiveData().observe(owner){ list ->
                val logDao = MyLog(log, "dataBase.OBSERVE",
                    msgStart = "для каждого элемента БД (EntityDataItem)",
                    working = false)

                val listName: MutableList<DataItemGeneralGame> = mutableListOf()//List<String> = emptyList()
                //записываем каждый элемент в список (преобразуя список имен)
                list.forEach {
                    it.nameList.replace("%%X!!##", " , ")
                    val dataItemGeneralGame = DataItemGeneralGame(it.idBGG, it.nameList, "uri", it.yearPublished.toString())
                    logDao.eachData("$dataItemGeneralGame")

                    listName.add(dataItemGeneralGame)
                }
                //передаем список в ViewModel
                viewModel.generalGameList.postValue(listName)
                //??viewModel.generalGameList.value = listName
                logDao.end()
            }
        }
        log.end()
    }
}

