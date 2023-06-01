package com.example.bggstats

import android.os.Build
import android.os.Bundle
import android.util.Log
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
import com.example.bggstats.const.Constants
import com.example.bggstats.retrofit.ProductAPI
import com.example.bggstats.shader.*
import com.example.bggstats.ui.theme.*
import com.example.bggstats.view.*
import com.example.bggstats.vm.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


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

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*val coral = Color(0xFFF3A397)
        val lightYellow = Color(0xFFF8EE94)
        val metricsOld = display?.mode?.physicalHeight
        val metricsR = display?.rotation
        //val metricsRot = windowManager.currentWindowMetrics.windowInsets*/


        setContent {
            /*BGGstatsTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    Greeting("Android")
                }
            }*/

            //ориентация экрана
            /*val displayOrientation = resources.configuration.orientation
            val displayHeight = if (displayOrientation == 1) {
                windowManager.currentWindowMetrics.bounds.height()
            } else {
                windowManager.currentWindowMetrics.bounds.width()
            }*/

            Log.d(TAG, "------------------------------")

            //Open custom dialog
            val openDialog = remember { mutableStateOf(vm.statusDetailedGame.value) }
            //Log.d("TAG", "openDialog: ${openDialog.value}")
            vm.statusDetailedGame.observe(this) {
                if (vm.statusDetailedGame.value == true) openDialog.value = true
                //Log.d("TAG", "openDialog: ${openDialog.value}")
            }
            openDialog.value = CustomDialog(openDialog = openDialog, vm = vm)

            //Angle gradient
            var sliderValue by remember { mutableStateOf(310f) }



            //val lazyListState: LazyListState = rememberLazyListState()

            //Card size
            // Get local density from composable
            val localDensity = LocalDensity.current
            // Create element height in pixel state
            /*var columnHeightPx by remember {
                mutableStateOf(0f)
            }*/
            // Create element height in dp state
            var columnHeightDp by remember { mutableStateOf(0.dp) }


            Box(modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
                .clip(shape = RoundedCornerShape(15.dp))
                .angledGradientBackground(FonGradient, 310f) //animatedAngle()
            )

            Column(modifier = Modifier
                //.fillMaxSize()
                .padding(25.dp)
            ) {
                //test buttons
                TestButton(vm)

                Button(onClick = {
                    Log.d(com.example.bggstats.view.TAG, "BGGList click")
                    val retrofit = Retrofit.Builder()
                        .baseUrl("https://dummyjson.com") //https://dummyjson.com //https://boardgamegeek.com/xmlapi/boardgame/
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()


                    val getProduct = retrofit.create(ProductAPI::class.java)

                    /*GlobalScope.launch {
                        val bggAPIDetailedGame = getProduct.getProductById()
                        Log.d(com.example.bggstats.view.TAG, "$bggAPIDetailedGame")
                    }*/

                    CoroutineScope(Dispatchers.IO).launch {
                        val bggAPIDetailedGame = getProduct.getProductById()
                        val bggtest =
                        Log.d(com.example.bggstats.view.TAG, "$bggAPIDetailedGame")
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
                        //Log.d("TAG", "Px: $columnHeightPx, Dp:$columnHeightDp")
                    }
                    .shadow(5.dp, shape = RoundedCornerShape(CORNER.dp)),
                    RoundedCornerShape(CORNER.dp)
                ) {
                    Graphs(columnHeightDp)
                }

                //General List
                GeneralGameList(openDialog = openDialog, vm = vm)

                //Detailed List
                DetailedGameList(vm = vm)

                /*
                Slider(
                    value = sliderValue,
                    onValueChange = { sliderValue_ ->
                        sliderValue = sliderValue_
                    },
                    onValueChangeFinished = {
                        // this is called when the user completed selecting the value
                        Log.d("TAG", "sliderValue = $sliderValue")
                    },
                    valueRange = 0f..360f,
                    modifier = Modifier.padding(top = 10.dp)
                )
                */
            }
        }
    }
}


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

    Log.d("TAG", "disp: $displayMetrics")

    Text(text = displayMetrics.toString())
}*/
