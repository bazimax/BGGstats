package com.example.bggstats.view

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bggstats.MainActivity
import com.example.bggstats.R
import com.example.bggstats.atest.log
import com.example.bggstats.atest.logD
import com.example.bggstats.atest.logEnd
import com.example.bggstats.atest.logStart
import com.example.bggstats.const.Constants
import com.example.bggstats.items.ItemsDetailedGameListTemp
import com.example.bggstats.items.ItemsGeneralGameList
import com.example.bggstats.retrofit.ProductAPI
import com.example.bggstats.retrofit.WebService
import com.example.bggstats.roomdb.EntityDataItem
import com.example.bggstats.roomdb.MainDb
import com.example.bggstats.shader.angledGradientBackground
import com.example.bggstats.shader.innerShadow
import com.example.bggstats.ui.theme.*
import com.example.bggstats.vm.ViewModel
import com.example.bggstats.vm.ViewModelFunctions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory

private val lnc = "Views" //logNameClass - для логов


//КОНСТАНТЫ
    //log
    const val TAG = Constants.TAG //разное
    const val TAG_DEBUG = Constants.TAG_DEBUG //запуск функция, активити и тд
    const val TAG_DATA = Constants.TAG_DATA //переменные и данные

    //View
    const val CORNER = Constants.CORNER



//Delete
@Composable
fun TestButton(dataBase: MainDb, viewModel: ViewModel){
    Card(modifier = Modifier
        .padding(bottom = 10.dp)
        .fillMaxWidth()
        .height(45.dp)
        //.fillMaxHeight()
        //.weight(1f)
        .shadow(5.dp, shape = RoundedCornerShape(MainActivity.CORNER.dp)),
        RoundedCornerShape(MainActivity.CORNER.dp),
        elevation = 10.dp
    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
        ) {
            Row(modifier = Modifier
                .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = {
                    Log.d(TAG, "BGGList click")
                    testRetrofitApi()

                }) {
                    Text(text = "BGGList",
                        fontSize = 10.sp)
                }

                Button(onClick = {
                    Log.d(TAG, "xml click")

                    //val a= RepositoryImpl("174430")
                    testRetrofitApiBGG(dataBase = dataBase, viewModel = viewModel)

                }) {
                    Text(text = "xml",
                        fontSize = 10.sp)
                }

                Button(onClick = {
                    Log.d(TAG, "page3-4 click")
                    logD("page3-4 click")

                    RetrofitApiBGG(viewModel = viewModel)
                }) {
                    Text(text = "page3-4",
                        fontSize = 10.sp)
                }
                Button(onClick = { /*TODO*/ }) {
                    Text(text = "gameA",
                        fontSize = 10.sp)
                }
            }
            Text(text = "${viewModel.testErrorMessage.value}")
            //BoxInnerShadowTemp()
        }

    }
}

//Delete
fun testRetrofitApi(){
    val interceptor = HttpLoggingInterceptor()
    interceptor.level = HttpLoggingInterceptor.Level.BODY

    val client = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .build()

    val retrofit = Retrofit.Builder()
        .baseUrl("https://dummyjson.com")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val getProduct = retrofit.create(ProductAPI::class.java)
    CoroutineScope(Dispatchers.IO).launch {
        val bggAPIDetailedGame = getProduct.getProductById()
        Log.d(TAG, "$bggAPIDetailedGame")
    }
}

//Delete?
inline fun <reified T> createWebService(
    okHttpClient: OkHttpClient,
    url: String
): T {

    val retrofit = Retrofit.Builder()
        .baseUrl(url)
        .client(okHttpClient)
        .addConverterFactory( SimpleXmlConverterFactory .create())
        .build()
    return retrofit.create(T::class. java )
}


//Game Detailed XML-API (from BGG)
fun testRetrofitApiBGG(dataBase: MainDb, viewModel: ViewModel){
    val interceptor = HttpLoggingInterceptor()
    interceptor.level = HttpLoggingInterceptor.Level.BODY

    val client = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .build()

    val retrofit = Retrofit.Builder()
        .baseUrl("https://boardgamegeek.com") //https://dummyjson.com //https://boardgamegeek.com/xmlapi/boardgame/
        .client(client)
        //.addConverterFactory(GsonConverterFactory.create())
        .addConverterFactory(SimpleXmlConverterFactory.create())
        .build()

    val webService = retrofit.create(WebService::class.java)

    //val boardGameGeekAPI = retrofit.create(BoardGameGeekAPI::class.java)
    CoroutineScope(Dispatchers.IO).launch {
        //получаем данные с сайта
        val bggAPIDetailedGame = webService.getFeed("174430,224517")//boardGameGeekAPI.getDetailedGame() //webService.getFeed(id)
        //val bggAPIDetailedGame2 = a.fetchFeed("174430")

        //val usersResponse = webService.getUsers("174430").execute()

        //записываем полученные данные в таблицу
        if (bggAPIDetailedGame.boardGameList?.size != 0) {
            val boardGameList = bggAPIDetailedGame.boardGameList

            val db = dataBase.getDao()
            //очищаем базу перед записью
            db.deleteAll()

            boardGameList?.forEach { item ->
                Log.d(TAG, "nameList: ${item.nameList}")
                val listName: MutableList<String> = mutableListOf()//List<String> = emptyList()
                item.nameList?.forEach { list ->
                    Log.d(TAG, "list: ${list.name}")
                    listName.add(list.name.toString())
                }
                val string = listName.joinToString("%%X!!##")
                Log.d(TAG, "string: $string")

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
                Log.d(TAG, "---- $a, $b, $c")
            }
        }

        //val successful = usersResponse.isSuccessful
        //val aatt = webService.
        //val parser: XmlPullParser = getResources().getXml(R.xml.contacts)
        //val xpp: XmlPullParser = bggAPIDetailedGame//bggAPIDetailedGame
        Log.d(TAG, "bggAPIDetailedGame: ${bggAPIDetailedGame.boardGameList?.get(0)}, \n successful: ${bggAPIDetailedGame.boardGameList?.size}, \n webService: $")
    }

    /*val boardGameGeekAPI = retrofit.create(BoardGameGeekAPI::class.java)
    CoroutineScope(Dispatchers.IO).launch {
        val bggTest = boardGameGeekAPI.getDetailedGame()
        Log.d(TAG, "$bggTest")
    }*/
}

//Work
fun RetrofitApiBGG(viewModel: ViewModel){
    logStart(lnc, "RetrofitApiBGG")
    val interceptor = HttpLoggingInterceptor()
    interceptor.level = HttpLoggingInterceptor.Level.BODY

    val client = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .build()

    val retrofit = Retrofit.Builder()
        .baseUrl("https://boardgamegeek.com") //https://dummyjson.com //https://boardgamegeek.com/xmlapi/boardgame/
        .client(client)
        //.addConverterFactory(GsonConverterFactory.create())
        .addConverterFactory(SimpleXmlConverterFactory.create())
        .build()

    val webService = retrofit.create(WebService::class.java)

    //val boardGameGeekAPI = retrofit.create(BoardGameGeekAPI::class.java)
    CoroutineScope(Dispatchers.IO).launch {
        //получаем данные с сайта
        val bggAPIDetailedGame = webService.getFeed("174430,224517")

        //записываем полученные данные в viewModel а тот уже в таблицу Room
        ViewModelFunctions(viewModel = viewModel).boardGameFeedToViewModel(bggAPIDetailedGame)

        Log.d(TAG, "bggAPIDetailedGame: ${bggAPIDetailedGame.boardGameList?.get(0)}, \n successful: ${bggAPIDetailedGame.boardGameList?.size}, \n webService: $")
        log(lnc, "RetrofitApiBGG", "bggAPIDetailedGame: ${bggAPIDetailedGame.boardGameList?.get(0)}, \n" +
                " successful: ${bggAPIDetailedGame.boardGameList?.size},")
    }

    /*val boardGameGeekAPI = retrofit.create(BoardGameGeekAPI::class.java)
    CoroutineScope(Dispatchers.IO).launch {
        val bggTest = boardGameGeekAPI.getDetailedGame()
        Log.d(TAG, "$bggTest")
    }*/
    logEnd(lnc, "RetrofitApiBGG")
}



//Info about select game
@Composable
fun CustomDialog(openDialog: MutableState<Boolean?>, viewModel: ViewModel): Boolean? {
    //var openDialog = openDialog1
    if (openDialog.value == true) { //if (vm.statusDetailedGame.value == true) {
        AlertDialog(
            onDismissRequest = {
                openDialog.value = false
                viewModel.statusDetailedGame.value = false
                return@AlertDialog
            },
            title = null, // { Text(text = "${vm.detailedGame.value?.title}") },
            text = null, /*{
                Column(modifier = Modifier.height(300.dp)) {
                    Text("Вы действительно хотите удалить выбранный элемент?")
                    Button(
                        onClick = {
                            openDialog.value = false
                            vm.statusDetailedGame.value = false
                            Log.d("TAG", "statusDetailedGame: ${vm.statusDetailedGame.value}")
                            //return openDialog
                        }
                    ) {
                        Text("OK", fontSize = 22.sp)
                    }
                }

            },*/
            buttons = {
                Column {
                    Text(text = "${viewModel.detailedGame.value?.title}")

                    Text(text = viewModel.detailedGame.value?.title ?: "")
                    //image v1
                    Image(
                        painter = painterResource(
                            id = viewModel.detailedGame.value?.imageId ?: R.drawable.pic2437871_big
                        ),//com.example.bggstats.R.drawable.pic2437871),//vm.detailedGame.value?.imageId ?: R.drawable.pic2437871_big),
                        contentDescription = "in"
                    )
                    //image v2
                    viewModel.detailedGame.value?.imageId?.let { painterResource(id = it) }?.let {
                        Image(
                            painter = it,
                            contentDescription = "in"
                        )
                    }

                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp)
                        .background(LightGrayFon),
                        Alignment.Center) {
                        IconButton(onClick = {
                            openDialog.value = false
                            viewModel.statusDetailedGame.value = false
                        }) {
                            Icon(
                                Icons.Filled.Close,//Filled.Refresh,
                                contentDescription = "Close Button",
                                tint = Color.Gray,
                                modifier = Modifier
                                    .size(40.dp)
                                    .rotate(180f)
                            )
                        }
                    }
                }


                /*Button(modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        openDialog.value = false
                        vm.statusDetailedGame.value = false
                        Log.d("TAG", "statusDetailedGame: ${vm.statusDetailedGame.value}")
                        //return openDialog
                    }
                ) {
                    Text("OK", fontSize = 22.sp)
                }*/
            }
        )
    }
    return openDialog.value
}

//Graphs
/*@Composable
fun GraphsTemp(_columnHeightDp: Dp, localDensity: Density){
    var columnHeightDp by remember {
        mutableStateOf(0.dp)
    }
    //var columnHeightDp = _columnHeightDp
    Card(modifier = Modifier
        .fillMaxWidth()
        //.fillMaxHeight()
        //.weight(1f)
        .height(250.dp)
        .padding(bottom = 10.dp)
        .onGloballyPositioned { coordinates ->
            // Set column height using the LayoutCoordinates
            //columnHeightPx = coordinates.size.width.toFloat()
            columnHeightDp = with(localDensity) { coordinates.size.width.toDp() - 30.dp }
            //Log.d("TAG", "Px: $columnHeightPx, Dp:$columnHeightDp")
        }
        .shadow(5.dp, shape = RoundedCornerShape(MainActivity.CORNER.dp)),
        RoundedCornerShape(MainActivity.CORNER.dp)
    ) {
        Graphs(columnHeightDp)
    }
}*/
@Composable
fun Graphs(columnHeightDp:Dp){
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
                    modifier = Modifier
                        .size(40.dp)
                        .rotate(180f)
                )
            }
        }

        Row(modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
        ) {
            CardTemp("Сеттинг", columnHeightDp)
            CardTemp("Жанр", columnHeightDp)

            /*(0 until 3).forEachIndexed { _, i ->
                populateListItem(i, vm)
            }*/
        }
    }
}

//General List
@Composable
fun GeneralGameList(openDialog: MutableState<Boolean?>, viewModel: ViewModel){
    Card(modifier = Modifier
        .fillMaxWidth()
        //.fillMaxHeight()
        //.weight(1f)
        .height(80.dp)
        .shadow(5.dp, shape = RoundedCornerShape(MainActivity.CORNER.dp)),
        RoundedCornerShape(MainActivity.CORNER.dp),
        elevation = 10.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
        ) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 5.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Список доступных игр")
                Text(text = "")
            }

            //прогрессбар
            /*Box(modifier = Modifier
                .height(6.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(CORNER.dp))
                .border(width = 1.dp, LightGray, shape = RoundedCornerShape(CORNER.dp))
                .angledGradientBackground(FonGradient, sliderValue)
            )*/
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(top = 1.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val context = LocalContext.current
                Button(onClick = {
                    //vm.detailedGameList
                    openDialog.value = true
                    //Toast.makeText(context, "test", Toast.LENGTH_LONG).show()
                },
                    colors = ButtonDefaults.textButtonColors(
                        backgroundColor = Coral,
                        contentColor = Color.White
                    )
                ) {
                    Text(text = "Go")
                }
                //OutlinedTextField(){}
                Text(text = "от")


                Text(text = "1")
                Text(text = "до")
                //OutlinedTextField(value = "", label = { Text(text = "1450")},onValueChange = {})
                Text(text = "1450")
                Text(text = "стр.",
                    color = Color.Black)
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_outline_file_download_24),
                        contentDescription = "Download csv",
                        tint = LightGray,
                        modifier = Modifier
                            .size(30.dp)
                            .rotate(0f)
                    )
                }
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        Icons.Filled.ArrowDropDown,
                        contentDescription = "Refresh Button",
                        tint = LightGray,
                        modifier = Modifier
                            .size(40.dp)
                            .rotate(0f)
                    )
                }
            }
            LazyColumn(modifier = Modifier
                .fillMaxWidth()
            ) {
                itemsIndexed(viewModel.generalGameList
                ) {_, item ->
                    ItemsGeneralGameList(item = item)
                }
            }
        }
    }
}

//Detailed List
@Composable
fun DetailedGameList(viewModel: ViewModel){
    Card(modifier = Modifier
        .padding(top = 10.dp)
        .fillMaxWidth()
        //.fillMaxHeight()
        //.weight(1f)
        .shadow(5.dp, shape = RoundedCornerShape(MainActivity.CORNER.dp)),
        RoundedCornerShape(MainActivity.CORNER.dp),
        elevation = 10.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
        ) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 5.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Детальный список игр")
                Text(text = "99%")
            }
            //прогрессбар
            Box(modifier = Modifier
                .height(6.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(MainActivity.CORNER.dp))
                .border(width = 1.dp, LightGray, shape = RoundedCornerShape(MainActivity.CORNER.dp))
                .angledGradientBackground(FonGradient2, 310F)
            )
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(top = 1.dp),
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
                Text(text = "игр",
                    color = Color.Black)
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_outline_file_download_24),
                        contentDescription = "Download csv",
                        tint = Coral,
                        modifier = Modifier
                            .size(30.dp)
                            .rotate(0f)
                    )
                }
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        Icons.Filled.ArrowDropDown,//Filled.Refresh,
                        contentDescription = "Refresh Button",
                        tint = Coral,
                        modifier = Modifier
                            .size(40.dp)
                            .rotate(180f)
                    )
                }
            }
            LazyColumn(modifier = Modifier
                .fillMaxWidth()
            ) {
                itemsIndexed(viewModel.detailedGameList
                ) {_, item ->
                    ItemsDetailedGameListTemp(item = item, viewModel)
                }
            }
        }
    }
}

@Preview
@Composable
fun CardTempPreview(){
    CardTemp("12345", 400.dp)
}


//Delete
@Composable
fun CardTemp(name: String, columnHeightDp: Dp) {
    Card(modifier = Modifier
        .padding(5.dp)
    ){
        Column(modifier = Modifier
            .width(columnHeightDp)
            .background(LightGrayFon)
        ) {
            Text(
                modifier = Modifier
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

//Delete
@Preview
@Composable
fun BoxInnerShadowTemp(){
    Box(
        modifier = Modifier
            .width(240.dp)
            .height(180.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFF778FCC))
            .innerShadow(
                blur = 5.dp,
                color = Color(0xFF3D5463),
                cornersRadius = 20.dp,
                offsetX = 2.dp,
                offsetY = 1.dp
            )
            /*.innerShadow(
                blur = 15.dp,
                color = Color(0xffff0000),
                cornersRadius = 35.dp,
                offsetX = 1.dp,
                offsetY = 1.dp
            )*/
            .padding(14.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "123)",
            modifier = Modifier,
            color = Color.White
        )
    }
}