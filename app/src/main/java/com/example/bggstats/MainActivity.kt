package com.example.bggstats

import android.os.Build
import android.os.Bundle
import android.service.quicksettings.Tile
import android.text.BoringLayout
import android.util.DisplayMetrics
import android.util.Log
import android.view.WindowInsets
import android.widget.EditText
import android.widget.HorizontalScrollView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.Bottom
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bggstats.const.Constants
import com.example.bggstats.items.ItemsDetailedGameList
import com.example.bggstats.items.ItemsGeneralGameList
import com.example.bggstats.shader.*
import com.example.bggstats.ui.theme.*
import com.example.bggstats.vm.ViewModel
import kotlin.math.sqrt


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

            var sliderValue by remember {
                mutableStateOf(310f) // pass the initial value
            }

            //val lazyListState: LazyListState = rememberLazyListState()


            // Get local density from composable
            val localDensity = LocalDensity.current
            // Create element height in pixel state
            var columnHeightPx by remember {
                mutableStateOf(0f)
            }
            // Create element height in dp state
            var columnHeightDp by remember {
                mutableStateOf(0.dp)
            }
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
                .clip(shape = RoundedCornerShape(15.dp))
                .angledGradientBackground(FonGradient, sliderValue)
                //.background(LightGray)
            )

            Column(modifier = Modifier
                .fillMaxSize()
                .padding(25.dp)
            ) {
                //Графики
                Card(modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .weight(1f)
                    .padding(bottom = 10.dp)
                    .onGloballyPositioned { coordinates ->
                        // Set column height using the LayoutCoordinates
                        columnHeightPx = coordinates.size.width.toFloat()
                        columnHeightDp = with(localDensity) { coordinates.size.width.toDp() - 30.dp }
                        Log.d("TAG", "Px: $columnHeightPx, Dp:$columnHeightDp")
                    }
                    .shadow(5.dp, shape = RoundedCornerShape(CORNER.dp)),
                    RoundedCornerShape(CORNER.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp)

                    ) {
                        Text(text = "Графики")
                        Row (modifier = Modifier
                            .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            Text(text = "ТОП: 100")
                            IconButton(onClick = { /*TODO*/ }) {
                                Icon(
                                    Icons.Filled.ArrowDropDown,
                                    contentDescription = "Refresh Button",
                                    tint = Coral,
                                    modifier = Modifier.size(40.dp).rotate(180f)
                                )
                            }
                        }

                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                        ) {
                            cardTemp("Сеттинг", columnHeightDp)
                            cardTemp("Жанр", columnHeightDp)

                            /*(0 until 3).forEachIndexed { _, i ->
                                populateListItem(i, vm)
                            }*/
                        }
                    }
                }

                //General List
                Card(modifier = Modifier
                    .fillMaxWidth()
                    //.fillMaxHeight()
                    //.weight(1f)
                    .height(75.dp)
                    .shadow(5.dp, shape = RoundedCornerShape(CORNER.dp))
                    ,
                    RoundedCornerShape(CORNER.dp),
                    elevation = 10.dp
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp)
                    ) {
                        Text(text = "Список доступных игр")
                        //прогрессбар
                        Box(modifier = Modifier
                            .height(6.dp)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(CORNER.dp))
                            .border(width = 1.dp, LightGray, shape = RoundedCornerShape(CORNER.dp))
                            .angledGradientBackground(FonGradient, sliderValue)
                        )
                        Row(modifier = Modifier
                            .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(onClick = { /*TODO*/ },
                                colors = ButtonDefaults.textButtonColors(
                                    backgroundColor = Coral,
                                    contentColor = Color.White
                                )
                            ) {
                                Text(text = "Go")
                            }
                            Text(text = "1")
                            Text(text = ">")
                            Text(text = "1450")
                            IconButton(onClick = { /*TODO*/ }) {
                                Icon(
                                    Icons.Filled.ArrowDropDown,
                                    contentDescription = "Refresh Button",
                                    tint = Coral,
                                    modifier = Modifier.size(40.dp).rotate(0f)
                                )
                            }
                        }
                        LazyColumn(modifier = Modifier
                            .fillMaxWidth()
                        ) {
                            itemsIndexed(vm.generalGameList
                            ) {_, item ->
                                ItemsGeneralGameList(item = item)
                            }
                        }
                    }
                }

                //Detailed List
                Card(modifier = Modifier
                    .padding(top = 10.dp)
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .weight(1f)
                    .shadow(5.dp, shape = RoundedCornerShape(CORNER.dp)),
                    RoundedCornerShape(CORNER.dp),
                    elevation = 10.dp
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp)
                    ) {
                        Text(text = "Детальный список игр")
                        //прогрессбар
                        Box(modifier = Modifier
                            .height(6.dp)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(CORNER.dp))
                            .border(width = 1.dp, LightGray, shape = RoundedCornerShape(CORNER.dp))
                            .angledGradientBackground(FonGradient, sliderValue)
                        )
                        Row(modifier = Modifier
                            .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(onClick = { /*TODO*/ },
                                colors = ButtonDefaults.textButtonColors(
                                    backgroundColor = Coral,
                                    contentColor = Color.White
                                )
                            ) {
                                Text(text = "Go")
                            }
                            Text(text = "1")
                            Text(text = ">")
                            Text(text = "145000")
                            IconButton(onClick = { /*TODO*/ }) {
                                Icon(
                                    Icons.Filled.ArrowDropDown,//Filled.Refresh,
                                    contentDescription = "Refresh Button",
                                    tint = Coral,
                                    modifier = Modifier.size(40.dp).rotate(180f)
                                )
                            }
                        }
                        LazyColumn(modifier = Modifier
                            .fillMaxWidth()
                        ) {
                            itemsIndexed(vm.detailedGameList
                            ) {_, item ->
                                ItemsDetailedGameList(item = item)
                            }
                        }
                    }
                }


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

@Composable
fun cardTemp(name: String, columnHeightDp: Dp) {
    Card(modifier = Modifier
        .padding(5.dp)
    ){
        Column(modifier = Modifier
            .width(columnHeightDp)
            .background(LightGrayFon)
        ) {
            Text(modifier = Modifier
                .padding(5.dp),
                text = "$name",
                fontSize = 15.sp,
            )
            Box(modifier = Modifier
                .padding(5.dp)
                .height(15.dp)
                .width(55.dp)
                .background(Coral)

            ){
                Text(text = "Brass")
            }
            Box(modifier = Modifier
                .padding(5.dp)
                .height(15.dp)
                .width(155.dp)
                .background(LightYellow)

            ){
                Text(text = "Gloomhaven")
            }
        }
    }
}


/*@Composable
fun Metric(){

    var displayMetrics by remember {
        mutableStateOf(DisplayMetrics().heightPixels)
    }

    Log.d("TAG", "disp: $displayMetrics")

    Text(text = displayMetrics.toString())
}*/
