package com.example.bggstats.atest

import android.util.Log
import com.example.bggstats.BuildConfig
import com.example.bggstats.const.Constants

/**
 * Обозначения пометок к комментариям:
 * //!! Важно - важный момент, за которым нужно следить
 * //?? - код вроде важен, необходим комментарий
 * //Delete - временные данные, удалить после окончания разработки
 * //Delete? - проверить, возможно элемент не нужен и его можно удалить
 * //TEST - тестовый элемент
 * //WORK - элемент еще не готов и находится в разработке
 * //BACKUP - код, который может быть полезен и оставлен про запас
*/

//Delete?
//private val TAG = this.javaClass.simpleName

/*//Первый вариант логов
//универсальная отметка о чем-то
fun log(logNameClass: String, function: String, text: String = ""){

    // Режим отладки, ведём логи
    if (BuildConfig.DEBUG) {
        //val t = if (text != "") " // $text" else ""
        Log.d(Constants.TAG_DEBUG, "$logNameClass >f $function > $text")
    }
}

//старт программы\жизненного цикла (обычно тут указывается onCreate)
fun logLaunch(logNameClass: String, function: String, text: String = ""){
    //logD("logLaunch > start")

    // Режим отладки, ведём логи
    if (BuildConfig.DEBUG) {
        //val f = if (function != "") ">f $function " else ""
        Log.d(Constants.TAG_DEBUG, "$logNameClass >f $function ======================== >" +
                checkText("$logNameClass >f $function","======") +
                checkText("$logNameClass >f $function","======") +
                checkText("$logNameClass >f $function","======") +
                checkText("$logNameClass >f $function","====== LAUNCH") +
                checkText("$logNameClass >f $function",text))
    }
    //logD("logLaunch > end")
}

//отметка начала работы функции
fun logStart(logNameClass: String, function: String, text: String = ""){

    // Режим отладки, ведём логи
    if (BuildConfig.DEBUG) {
        Log.d(Constants.TAG_DEBUG, "$logNameClass >f $function === START" +
                checkText("$logNameClass >f $function",text))
    }
}

//отметка окончания работы функции
fun logEnd(logNameClass: String, function: String, text: String = ""){

    // Режим отладки, ведём логи
    if (BuildConfig.DEBUG) {
        Log.d(Constants.TAG_DEBUG, "$logNameClass >f $function ----- END" +
                checkText("$logNameClass >f $function",text))
    }
}

//подробности выполняемой функции (как универсальный только с переносом строки) + Log.i
fun logInfo(logNameClass: String, function: String, text: String){

    // Режим отладки, ведём логи
    if (BuildConfig.DEBUG) {
        Log.i(Constants.TAG_DEBUG, "$logNameClass >f $function > Info:" +
                checkText("$logNameClass >f $function",text))
    }
}

//отслеживание данных
fun logData(logNameClass: String, function: String, data: String, text: String = ""){

    // Режим отладки, ведём логи
    if (BuildConfig.DEBUG) {
        Log.d(Constants.TAG_DATA, "$logNameClass >f $function > data:: $data" +
                checkText("$logNameClass >f $function",text)
        )
    }
}

//отслеживание больших данных
fun logDataBig(logNameClass: String, function: String, dataBig: String, text: String = ""){

    // Режим отладки, ведём логи
    if (BuildConfig.DEBUG) {
        Log.d(Constants.TAG_DATA_BIG, "$logNameClass >f $function > dataBig:: $dataBig" +
                checkText("$logNameClass >f $function",text)
        )
    }
}

//отслеживание данных в циклах
fun logDataEach(logNameClass: String, function: String, dataEach: String, text: String = ""){

    // Режим отладки, ведём логи
    if (BuildConfig.DEBUG) {
        Log.d(Constants.TAG_DATA_EACH, "$logNameClass >f $function > dataEach:: $dataEach" +
                checkText("$logNameClass >f $function",text)
        )
    }
}

//сообщение об ошибке + Log.e
fun logError(logNameClass: String, function: String, textError: String){
    // Режим отладки, ведём логи
    if (BuildConfig.DEBUG) {
        Log.e(Constants.TAG_ERROR, "$logNameClass >f $function > ERROR: $textError")
    }
}*/

//простой лог
fun logD(text: String){

    // Режим отладки, ведём логи
    if (BuildConfig.DEBUG) {
        Log.d(Constants.TAG_DEBUG, text)
    }
}
fun logI(text: String){

    // Режим отладки, ведём логи
    if (BuildConfig.DEBUG) {
        Log.i(Constants.TAG_DEBUG, text)
    }
}

//перемещаем текст на новую строку и смещаем на длину tabString //оно же correctText
fun checkText(tabString: String, text: String): String {
    return if (text != "") "\n${"".padStart(tabString.count() - 1, ' ')}* " + text else ""
}

//второй вариант логов //class MyLog(private var logNameClass: String, private var function: String, msgStart: String = "", launch: Boolean = false)
class MyLog(private var logNameClass: String, private var function: String, msgStart: String = "", launch: Boolean = false){

    //private var logNameClass: String = function2
    //private var function: String
    //отметка начала работы функции

    //Конструктор для вложенных логов
    constructor(myLog: MyLog,
                childFunction: String,
                logNameClass: String = myLog.logNameClass,
                function: String = "${myLog.function} >f $childFunction",
                msgStart: String = "",
                launch: Boolean = false
    ) : this(logNameClass, function, msgStart, launch)

    init {
        //Log.d(Constants.TAG_DEBUG, "Init log")

        // Режим отладки, ведём логи
        if (BuildConfig.DEBUG) {
            //Если это запуск activity
            if (launch) {
                Log.d(Constants.TAG_DEBUG, "$logNameClass >f $function ======================== >" +
                        correctText("$logNameClass >f $function","======") +
                        correctText("$logNameClass >f $function","======") +
                        correctText("$logNameClass >f $function","======") +
                        correctText("$logNameClass >f $function","====== LAUNCH") +
                        correctText("$logNameClass >f $function",msgStart))
            }
            //если обычная функция
            else Log.d(Constants.TAG_DEBUG, "$logNameClass >f $function === START" +
                    correctText("$logNameClass >f $function",msgStart))
        }
    }





    //отметка окончания работы функции
    fun end(msg: String = ""){
        // Режим отладки, ведём логи
        if (BuildConfig.DEBUG) {
            Log.d(Constants.TAG_DEBUG, "$logNameClass >f $function ----- END" +
                    correctText("$logNameClass >f $function",msg))
        }
    }

    //отметка без типа
    fun d(msg: String = ""){
        // Режим отладки, ведём логи
        if (BuildConfig.DEBUG) {
            //val t = if (text != "") " // $text" else ""
            Log.d(Constants.TAG_DEBUG, "$logNameClass >f $function > $msg")
        }
    }

    //отслеживание данных
    fun data(data: String, msg: String = ""){
        // Режим отладки, ведём логи
        if (BuildConfig.DEBUG) {
            Log.d(Constants.TAG_DATA, "$logNameClass >f $function > data:: $data" +
                    correctText("$logNameClass >f $function",msg)
            )
        }
    }

    //отслеживание больших данных
    fun bigData(bigData: String, msg: String = ""){
        // Режим отладки, ведём логи
        if (BuildConfig.DEBUG) {
            Log.d(Constants.TAG_DATA_BIG, "$logNameClass >f $function > dataBig:: $bigData" +
                    correctText("$logNameClass >f $function",msg)
            )
        }
    }

    //отслеживание данных в циклах
    fun eachData(eachData: String, msg: String = ""){
        // Режим отладки, ведём логи
        if (BuildConfig.DEBUG) {
            Log.d(Constants.TAG_DATA_EACH, "$logNameClass >f $function > dataEach:: $eachData" +
                    correctText("$logNameClass >f $function",msg)
            )
        }
    }

    //сообщение об ошибке / Log.e
    fun error(textError: String, msg: String = ""){
        // Режим отладки, ведём логи
        if (BuildConfig.DEBUG) {
            Log.e(Constants.TAG_ERROR, "$logNameClass >f $function > ERROR: $textError" +
                    correctText("$logNameClass >f $function",msg))
        }
    }

    //отметка info / Log.i
    fun i(msg: String = ""){
        // Режим отладки, ведём логи
        if (BuildConfig.DEBUG) {
            //val t = if (text != "") " // $text" else ""
            Log.i(Constants.TAG_DEBUG, "$logNameClass >f $function > $msg")
        }
    }

    //отметка warning / Log.w
    fun w(msg: String = ""){
        // Режим отладки, ведём логи
        if (BuildConfig.DEBUG) {
            //val t = if (text != "") " // $text" else ""
            Log.w(Constants.TAG_DEBUG, "$logNameClass >f $function > $msg")
        }
    }

    //отметка verbose / Log.v
    fun v(msg: String = ""){
        // Режим отладки, ведём логи
        if (BuildConfig.DEBUG) {
            //val t = if (text != "") " // $text" else ""
            Log.v(Constants.TAG_DEBUG, "$logNameClass >f $function > $msg")
        }
    }

    //перемещаем текст на новую строку и смещаем на длину tabString
    private fun correctText(tabString: String, text: String): String {
        return if (text != "") "\n${"".padStart(tabString.count() - 1, ' ')}* " + text else ""

    }
}


/*

//BACKUP идея на будущее
//val log = log(lnc, "function") - start (первый лог всегда пишем так, далее передаем переменную "log" в другие логи и они сами понимают )
//log(log, "comment") - info
//log(log, data = "$data") - data
//log(log, bigData = "$bigData") - gig data
//log(log, eachData = "$data") - data if / data Each
//log(log) - end

*/
/*val log = MyLog(lnc, "boardGameFeedViewModelToRoom")
log.end()
log.bigData("$dataBase")*/
/*



//logUniversal(log)
fun logUniversal(log: Pair<String, String> = "" to "",
              lnc: String = "",
              function: String = "",
              text: String = "",
              data: String = "",
              bigData: String = "",
              eachData: String = "",
              launch: String = ""
): Pair<String, String> {

    // Режим отладки, ведём логи
    if (BuildConfig.DEBUG) {

        var tag = Constants.TAG_DEBUG
        var endLogStatus = false
        var startLogStatus = false
        val functionCorrect = if (function != "") " >f $function" else ""
        val t = if (text != "") "\n // " else ""

        //если передаем lnc\logNameClass, то это стартовый лог
        if (lnc != "") {
            startLogStatus = true
        }
        //если передаем log - то уже точно не стартовый лог
        else if (log.first != "") {
            startLogStatus = false
            endLogStatus = true
        }
        else if (text != "") {
            endLogStatus = false
        }
        else if (data != "") {
            endLogStatus = false
            tag += Constants.TAG_DATA
        }
        else if (bigData != "") {
            endLogStatus = false
            tag += Constants.TAG_DATA_BIG
        }
        else if (eachData != "") {
            endLogStatus = false
            tag += Constants.TAG_DATA_EACH
        }

        val startLog = if (startLogStatus) " === START" else ""
        val endLog = if (endLogStatus) " ----- END" else ""
        Log.d(tag, lnc +
                functionCorrect +
                " // $text$endLog")
        //Log.d(tag, "$lnc >f $function === START")
    }

    return lnc to function
}

*/
//BACKUP Delete
/*//Первый вариант логов
//универсальная отметка о чем-то
fun log(logNameClass: String, function: String, text: String = ""){

    // Режим отладки, ведём логи
    if (BuildConfig.DEBUG) {
        //val t = if (text != "") " // $text" else ""
        Log.d(Constants.TAG_DEBUG, "$logNameClass >f $function > $text")
    }
}

//старт программы\жизненного цикла (обычно тут указывается onCreate)
fun logLaunch(logNameClass: String, function: String, text: String = ""){
    //logD("logLaunch > start")

    // Режим отладки, ведём логи
    if (BuildConfig.DEBUG) {
        //val f = if (function != "") ">f $function " else ""
        Log.d(Constants.TAG_DEBUG, "$logNameClass >f $function ======================== >" +
                checkText("$logNameClass >f $function","======") +
                checkText("$logNameClass >f $function","======") +
                checkText("$logNameClass >f $function","======") +
                checkText("$logNameClass >f $function","====== LAUNCH") +
                checkText("$logNameClass >f $function",text))
    }
    //logD("logLaunch > end")
}

//отметка начала работы функции
fun logStart(logNameClass: String, function: String, text: String = ""){

    // Режим отладки, ведём логи
    if (BuildConfig.DEBUG) {
        Log.d(Constants.TAG_DEBUG, "$logNameClass >f $function === START" +
                checkText("$logNameClass >f $function",text))
    }
}

//отметка окончания работы функции
fun logEnd(logNameClass: String, function: String, text: String = ""){

    // Режим отладки, ведём логи
    if (BuildConfig.DEBUG) {
        Log.d(Constants.TAG_DEBUG, "$logNameClass >f $function ----- END" +
                checkText("$logNameClass >f $function",text))
    }
}

//подробности выполняемой функции (как универсальный только с переносом строки) + Log.i
fun logInfo(logNameClass: String, function: String, text: String){

    // Режим отладки, ведём логи
    if (BuildConfig.DEBUG) {
        Log.i(Constants.TAG_DEBUG, "$logNameClass >f $function > Info:" +
                checkText("$logNameClass >f $function",text))
    }
}

//отслеживание данных
fun logData(logNameClass: String, function: String, data: String, text: String = ""){

    // Режим отладки, ведём логи
    if (BuildConfig.DEBUG) {
        Log.d(Constants.TAG_DATA, "$logNameClass >f $function > data:: $data" +
                checkText("$logNameClass >f $function",text)
        )
    }
}

//отслеживание больших данных
fun logDataBig(logNameClass: String, function: String, dataBig: String, text: String = ""){

    // Режим отладки, ведём логи
    if (BuildConfig.DEBUG) {
        Log.d(Constants.TAG_DATA_BIG, "$logNameClass >f $function > dataBig:: $dataBig" +
                checkText("$logNameClass >f $function",text)
        )
    }
}

//отслеживание данных в циклах
fun logDataEach(logNameClass: String, function: String, dataEach: String, text: String = ""){

    // Режим отладки, ведём логи
    if (BuildConfig.DEBUG) {
        Log.d(Constants.TAG_DATA_EACH, "$logNameClass >f $function > dataEach:: $dataEach" +
                checkText("$logNameClass >f $function",text)
        )
    }
}

//сообщение об ошибке + Log.e
fun logError(logNameClass: String, function: String, textError: String){
    // Режим отладки, ведём логи
    if (BuildConfig.DEBUG) {
        Log.e(Constants.TAG_ERROR, "$logNameClass >f $function > ERROR: $textError")
    }
}*/
