package com.example.bggstats

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bggstats.atest.MyLog
import com.example.bggstats.atest.logD
//import com.example.bggstats.atest.logEnd
//import com.example.bggstats.atest.logStart
import com.example.bggstats.const.Constants
import com.example.bggstats.daggerhilt.WiFiManager
import com.example.bggstats.daggerhilt.WiFiSettings
import com.example.bggstats.retrofit.ProductAPI
import com.example.bggstats.roomdb.MainDb
import com.example.bggstats.shader.*
import com.example.bggstats.ui.theme.*
import com.example.bggstats.view.*
import com.example.bggstats.vm.ViewModel
import com.example.bggstats.vm.ViewModelFunctions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine

//private const val lnc = "MainActivity" //logNameClass - для логов
@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    private val vm: ViewModel by viewModels()

    //КОНСТАНТЫ
    companion object {
        //log
        const val TAG = Constants.TAG //разное
        const val TAG_DEBUG = Constants.TAG_DEBUG //запуск функция, активити и тд
        const val TAG_DATA = Constants.TAG_DATA //переменные и данные

        //View
        const val CORNER = Constants.CORNER
    }

    //?? Проверить
    private val lnc = this.javaClass.simpleName //"MainActivity" //logNameClass - для логов


    //TEST >
    @Inject
    lateinit var wiFiManager: WiFiManager




    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //logLaunch(lnc, "onCreate")
        val myLogMain = MyLog(lnc, "onCreate", launch = true)
        myLogMain.d("test > $wiFiManager") //Delete

        /*val coral = Color(0xFFF3A397)
        val lightYellow = Color(0xFFF8EE94)
        val metricsOld = display?.mode?.physicalHeight
        val metricsR = display?.rotation
        //val metricsRot = windowManager.currentWindowMetrics.windowInsets*/

        val db = MainDb.getDb(this) //создаем-открываем Базу Данных (БД) Room
        init(dataBase = db) //стартовые функции

        wiFiManager.connect()
        wiFiManager.sendMessage()

        setContent {

            //Delete
            /*BGGstatsTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    Greeting("Android")
                }
            }*/

            //Delete //ориентация экрана
            /*val displayOrientation = resources.configuration.orientation
            val displayHeight = if (displayOrientation == 1) {
                windowManager.currentWindowMetrics.bounds.height()
            } else {
                windowManager.currentWindowMetrics.bounds.width()
            }*/

            //Delete?
            //val db = MainDb.getDb(this)

            //logD("------------------------------")
            myLogMain.d("------------------------------")

            //Open custom dialog
            val openDialog = remember { mutableStateOf(vm.statusDetailedGame.value) }
            vm.statusDetailedGame.observe(this) {
                if (vm.statusDetailedGame.value == true) openDialog.value = true
            }
            openDialog.value = CustomDialog(openDialog = openDialog, viewModel = vm)

            //Общий список игр
            val generalGameList = remember { mutableStateOf(vm.generalGameList.value) }
            vm.generalGameList.observe(this) {
                if (vm.generalGameList.value != null) {
                    generalGameList.value = vm.generalGameList.value
                }
            }

            /*//WORK - заготовка
            //Подробный список игр
            val detailGameList = remember { mutableStateOf(vm.detailedGameList.value) }
            vm.detailedGameList.observe(this) {
                if (vm.detailedGameList.value !=null) {
                    detailGameList.value = vm.detailedGameList.value
                }
            }*/

            //Angle gradient
            //var sliderValue by remember { mutableStateOf(310f) }

            //??
            //val lazyListState: LazyListState = rememberLazyListState()

            //Card size
            // Get local density from composable
            val localDensity = LocalDensity.current
            // Create element height in pixel state
            //var columnHeightPx by remember { mutableStateOf(0f) }
            // Create element height in dp state
            var columnHeightDp by remember { mutableStateOf(0.dp) }


            Box(modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
                .clip(shape = RoundedCornerShape(15.dp))
                .angledGradientBackground(FonGradient, 310f) //310f //animatedAngle(endAngle = 310f)
            )

            Column(modifier = Modifier
                //.fillMaxSize()
                .padding(25.dp)
            ) {
                //test buttons
                TestButton(dataBase = db, viewModel =  vm)

                //Delete?
                Button(onClick = {
                    logD("BGGList click @@@")

                    val retrofit = Retrofit.Builder()
                        .baseUrl("https://dummyjson.com") //https://dummyjson.com //https://boardgamegeek.com/xmlapi/boardgame/
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                    val getProduct = retrofit.create(ProductAPI::class.java)
                    //GlobalScope.launch { val bggAPIDetailedGame = getProduct.getProductById() logD("$bggAPIDetailedGame") }
                    CoroutineScope(Dispatchers.IO).launch {
                        val bggAPIDetailedGame = getProduct.getProductById()
                        //val bggtest =
                        logD("$bggAPIDetailedGame + @@@@")
                    }
                }) {
                    Text(text = "BGGList",
                        fontSize = 10.sp)
                }

                //Animation
                //LoadingAnimationCircle()

                //Graphs
                Card(modifier = Modifier
                    .fillMaxWidth()
                    //.fillMaxHeight()
                    //.weight(1f)
                    .height(40.dp)
                    .padding(bottom = 10.dp)
                    .onGloballyPositioned { coordinates ->
                        // Set column height using the LayoutCoordinates
                        //columnHeightPx = coordinates.size.width.toFloat()
                        columnHeightDp = with(localDensity) { coordinates.size.width.toDp() - 30.dp }
                        //logD("Px: $columnHeightPx, Dp:$columnHeightDp")
                    }
                    .shadow(5.dp, shape = RoundedCornerShape(CORNER.dp)),
                    RoundedCornerShape(CORNER.dp)
                ) {
                    Graphs(columnHeightDp)
                }

                //General List
                GeneralGameList(openDialog = openDialog, generalGameList, viewModel = vm, owner = this@MainActivity)

                //Detailed List
                DetailedGameList(viewModel = vm)

                //slider()
            }
        }
    }

    //стартовые функции
    private fun init(dataBase: MainDb){
        val logMain = MyLog(lnc, "init")
        ViewModelFunctions(vm).observeVM(dataBase, this)
        logMain.end()
    }
}

/*@Composable
fun slider() {
    Slider(
        value = sliderValue,
        onValueChange = { sliderValue_ ->
            sliderValue = sliderValue_
        },
        onValueChangeFinished = {
            // this is called when the user completed selecting the value
            logD("sliderValue = $sliderValue")
        },
        valueRange = 0f..360f,
        modifier = Modifier.padding(top = 10.dp)
    )
}*/





/*
//BACKUP - доп. вариант
val data = data(viewModel = vm, owner = this)
logMain.d("data.first: ${data.first}")
logMain.d("data.second: ${data.second}")

@Composable
fun data(viewModel: ViewModel, owner: LifecycleOwner): Pair<Boolean?, List<DataItemGeneralGame>?> {
    //Open custom dialog
    val openDialog = remember { mutableStateOf(viewModel.statusDetailedGame.value) }
    viewModel.statusDetailedGame.observe(owner) {
        if (viewModel.statusDetailedGame.value == true) openDialog.value = true
    }
    openDialog.value = CustomDialog(openDialog = openDialog, viewModel = viewModel)

    val generalGameListVM = remember { mutableStateOf(viewModel.generalGameList.value) }

    viewModel.generalGameList.observe(owner) {
        if (viewModel.generalGameList.value !=null) {
            generalGameListVM.value = viewModel.generalGameList.value
        }
    }
    return openDialog.value to generalGameListVM.value
}*/

//??
/*private fun isEditTagItemFullyVisible(lazyListState: LazyListState, editTagItemIndex: Int): Boolean {
    with(lazyListState.layoutInfo) {
        val editingTagItemVisibleInfo = visibleItemsInfo.find { it.index == editTagItemIndex }
        return if (editingTagItemVisibleInfo == null) {
            false
        } else {
            viewportEndOffset - editingTagItemVisibleInfo.offset >= editingTagItemVisibleInfo.size
        }
    }
}*/


/*
//Metric
@Composable
fun Metric(){

    var displayMetrics by remember {
        mutableStateOf(DisplayMetrics().heightPixels)
    }

    logD("disp: $displayMetrics")

    Text(text = displayMetrics.toString())
}*/
