package com.example.bggstats.atest

import android.util.Log
import com.example.bggstats.BuildConfig
import com.example.bggstats.const.Constants

/**
 * Обозначения пометок к комментариям:
 * //!! Important - важный момент, за которым нужно следить
 * //?? - код вроде важен, необходим комментарий
 * //Del - временные данные, удалить после окончания разработки
 * //Delete? - проверить, возможно элемент не нужен и его можно удалить
 * //TEST - тестовый элемент
 * //WORK - элемент еще не готов и находится в разработке (похоже на TO-DO)
 * //BACKUP - код, который может быть полезен и оставлен про запас
*/

/**
 * Инструкция по MyLog и logD/logI:
 *
 * MyLog - расширенная версия Log. logD/logI - укороченная версия Log, с базовым тегом TAG_DEBUG
 *
 *
 * Пример лога и пояснение:
 * logNameClass >f function > childFunction > childFunction > msg
 *
 * logNameClass - имя класса в котором хранится функция
 * function - имя функции
 * childFunction - если есть вложения\ветвления, такие как coroutine, forEach и т.д. - можно дополнить имя функции
 * msg\msgStart - сообщение
 * launch - если true, то сделает сообщение более заметным. Используется обычно при запуске приложения
 * working - показывать или нет лог (и все логи с ним связанные)
 *
 *
 * Виды логов:
 * d - просто сообщение, тег: TAG_DEBUG
 * data - помечает как данные, тег: TAG_DATA
 * eachData - помечает как повторяющиеся данные в циклах (forEach и т.п.), тег: TAG_DATA_EACH
 * bigData - помечает как большие данные (много-строковые данные, парсинг интернет страниц и т.п.), тег: TAG_DATA_BIG
 * end - отметка об окончании функции тег: TAG_DEBUG
 * error - как и Log.e, тег: TAG_ERROR
 * i - как и Log.i, тег: TAG_DEBUG
 * w - как и Log.w, тег: TAG_DEBUG
 * v - как и Log.v, тег: TAG_DEBUG
 *
 *
 * - Лог показывается если BuildConfig.DEBUG и working == true
 * - При первой инициализации помечается "=== START",
 * - Если нужна отметка об окончании, то в конце функции стоит вызвать "end". Пример: val log = MyLog(); log.end()
 * - Есть возможность вложить один MyLog в другой - тогда новый подхватит logNameClass, function и working
 */

//простой и быстрый лог
fun logD(text: String, logNameClass: String = "", function: String = ""){


    // Режим отладки, ведём логи
    if (BuildConfig.DEBUG) {
        val lnc = if (logNameClass == "") "" else "$logNameClass >f "
        val func = if (function == "") "" else "$function > "
        Log.d(Constants.TAG_DEBUG, "$lnc$func$text")
    }
}
fun logI(text: String, logNameClass: String = "", function: String = ""){

    // Режим отладки, ведём логи
    if (BuildConfig.DEBUG) {
        val lnc = if (logNameClass == "") "" else "$logNameClass >f "
        val func = if (function == "") "" else "$function > "
        Log.i(Constants.TAG_DEBUG, "$lnc$func$text")
    }
}

//второй вариант логов
class MyLog(
    private var logNameClass: String,
    private var function: String,
    private val working: Boolean = true,
    msgStart: String = "",
    launch: Boolean = false
){

    var tag1 = ""

    private val checkWorking = BuildConfig.DEBUG && working

    //private var logNameClass: String = function2
    //private var function: String
    //отметка начала работы функции
    init {

        // Режим отладки, ведём логи
        if (checkWorking) {
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

    //Конструктор для вложенных логов
    constructor(myLog: MyLog,
                childFunction: String,
                working: Boolean = myLog.working,
                logNameClass: String = myLog.logNameClass,
                function: String = "${myLog.function} > $childFunction",
                msgStart: String = "",
                launch: Boolean = false
    ) : this(logNameClass, function, working, msgStart, launch)

    //отметка окончания работы функции
    fun end(msg: String = "", childFunction: String = ""){
        // Режим отладки, ведём логи
        if (checkWorking) {
            val childF = if (childFunction == "") "" else " > $childFunction"

            Log.d(Constants.TAG_DEBUG, "$logNameClass >f $function$childF ----- END" +
                    correctText("$logNameClass >f $function$childF",msg))
        }
    }

    //отметка без типа
    fun d(msg: String = "", childFunction: String = ""){
        // Режим отладки, ведём логи
        if (checkWorking) {
            val childF = if (childFunction == "") "" else " > $childFunction"

            //val t = if (text != "") " // $text" else ""
            Log.d(Constants.TAG_DEBUG, "$logNameClass >f $function$childF > $msg")
        }
    }

    //отслеживание данных
    fun data(data: String, msg: String = "", childFunction: String = ""){
        // Режим отладки, ведём логи
        if (checkWorking) {
            val childF = if (childFunction == "") "" else " > $childFunction"

            Log.d(Constants.TAG_DATA, "$logNameClass >f $function$childF > data:: $data" +
                    correctText("$logNameClass >f $function$childF",msg)
            )
        }
    }

    //отслеживание больших данных
    fun bigData(bigData: String, msg: String = "", childFunction: String = ""){
        // Режим отладки, ведём логи
        if (checkWorking) {
            val childF = if (childFunction == "") "" else " > $childFunction"

            Log.d(Constants.TAG_DATA_BIG, "$logNameClass >f $function$childF > dataBig:: $bigData" +
                    correctText("$logNameClass >f $function$childF",msg)
            )
        }
    }

    //отслеживание данных в циклах
    fun eachData(eachData: String, msg: String = "", childFunction: String = ""){
        // Режим отладки, ведём логи
        if (checkWorking) {
            val childF = if (childFunction == "") "" else " > $childFunction"

            Log.d(Constants.TAG_DATA_EACH, "$logNameClass >f $function$childF > dataEach:: $eachData" +
                    correctText("$logNameClass >f $function$childF",msg)
            )
        }
    }

    //сообщение об ошибке / Log.e
    fun error(textError: String, msg: String = "", childFunction: String = ""){
        // Режим отладки, ведём логи
        if (checkWorking) {
            val childF = if (childFunction == "") "" else " > $childFunction"

            Log.e(Constants.TAG_ERROR, "$logNameClass >f $function$childF > ERROR: $textError" +
                    correctText("$logNameClass >f $function$childF",msg))
        }
    }

    //отметка info / Log.i
    fun i(msg: String = "", childFunction: String = ""){
        // Режим отладки, ведём логи
        if (checkWorking) {
            val childF = if (childFunction == "") "" else " > $childFunction"

            //val t = if (text != "") " // $text" else ""
            Log.i(Constants.TAG_DEBUG, "$logNameClass >f $function$childF > $msg")
        }
    }

    //отметка warning / Log.w
    fun w(msg: String = "", childFunction: String = ""){
        // Режим отладки, ведём логи
        if (checkWorking) {
            val childF = if (childFunction == "") "" else " > $childFunction"

            //val t = if (text != "") " // $text" else ""
            Log.w(Constants.TAG_DEBUG, "$logNameClass >f $function$childF > $msg")
        }
    }

    //отметка verbose / Log.v
    fun v(msg: String = "", childFunction: String = ""){
        // Режим отладки, ведём логи
        if (checkWorking) {
            val childF = if (childFunction == "") "" else " > $childFunction"

            //val t = if (text != "") " // $text" else ""
            Log.v(Constants.TAG_DEBUG, "$logNameClass >f $function$childF > $msg")
        }
    }

    //перемещаем текст на новую строку и смещаем на длину tabString
    private fun correctText(tabString: String, text: String): String {
        return if (text != "") "\n${"".padStart(tabString.count() - 1, ' ')}* " + text else ""

    }
}
