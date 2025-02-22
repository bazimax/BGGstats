package com.example.bggstats.items

import android.content.Context
import android.util.Log
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.bggstats.R
import com.example.bggstats.atest.MyLog
import com.example.bggstats.atest.logD
import com.example.bggstats.vm.ViewModel

private const val CN = "items" //className - для логов

//WORK
//элемент DataItemGeneralGame для списка всех найденных игр
@Composable
fun ItemsGeneralGameList(item: DataItemGeneralGame) {
    MyLog(CN, "@Composable: ItemsGeneralGameList", msgStart = "DataItemGeneralGame: $item")

    var isExpanded by remember {
        mutableStateOf(false)
    }

    /*val isExpandVal = remember {
        mutableStateOf(false)
    }*/
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(3.dp)
        .background(Color.White)
    ) {
        Text(text = item.id.toString(),
        modifier = Modifier.padding(end = 15.dp)
            )
        Text(text = item.name)

        Column(modifier = Modifier.padding(start = 10.dp, top = 15.dp)) {

            /*maxLines = if(isExpandVal.value) Int.MAX_VALUE else 1)*/
        }
    }
}

//WORK
//Элемент списка DataItemDetailedGame в котором отображаются подробные сведения по каждой игре
@Composable
fun ItemsDetailedGameList(item: DataItemDetailedGameTemp) {
    /*var isExpanded by remember {
        mutableStateOf(false)
    }*/
    /*val isExpandVal = remember {
        mutableStateOf(false)
    }*/

    val openDialog = remember { mutableStateOf(false) }

    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = {
                openDialog.value = false
            },
            title = { Text(text = "Подтверждение действия") },
            text = { Text("Вы действительно хотите удалить выбранный элемент?") },
            buttons = {
                Button(
                    onClick = { openDialog.value = false }
                ) {
                    Text("OK", fontSize = 22.sp)
                }
            }
        )
    }
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(3.dp)
        .background(Color.White)
        .clickable {

        }
    ) {
        Text(text = item.id.toString())
        Box(modifier = Modifier
            .width(60.dp)
            .height(40.dp)
        ){
            Image(painter = painterResource(id = item.imageId),
                contentDescription = "cover",
                modifier = Modifier
                    .padding(3.dp)
                    .size(64.dp)
            )
        }
        /*Button(onClick = { openDialog.value = true }) {
            Text(text = "t1")
        }*/

        Text(text = item.title)
        /*Column(modifier = Modifier.padding(start = 10.dp, top = 15.dp)) {

            *//*Text(text = item.content,
                modifier = Modifier
                    *//**//*.horizontalScroll(rememberScrollState())*//**//*
                    .clickable {
                        isExpanded = !isExpanded
                        *//**//*isExpandVal.value = !isExpandVal.value*//**//*
                    },
                maxLines = if(isExpanded) Int.MAX_VALUE else 1)*//*
            *//*maxLines = if(isExpandVal.value) Int.MAX_VALUE else 1)*//*
        }*/
    }
}

//WORK
//TEST
//Элемент списка DataItemDetailedGameTemp в котором отображаются подробные сведения по каждой игре
@Composable
fun ItemsDetailedGameListTemp(item: DataItemDetailedGameTemp, viewModel: ViewModel) {

    //??
    val openDialog = remember { mutableStateOf(false) }
    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = {
                openDialog.value = false
            },
            title = { Text(text = "Подтверждение действия") },
            text = { Text("Вы действительно хотите удалить выбранный элемент?") },
            buttons = {
                Button(
                    onClick = { openDialog.value = false }
                ) {
                    Text("OK", fontSize = 22.sp)
                }
            }
        )
    }

    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(3.dp)
        .background(Color.White)
        .clickable {
            logD("Row Click")
            viewModel.detailedGame.value = item
            viewModel.statusDetailedGame.value = true
            //Log.d("TAG", "detailedGame: ${viewModel.detailedGame.value}")
            //Log.d("TAG", "statusDetailedGame: ${viewModel.statusDetailedGame.value}")
        }
    ) {
        Text(text = item.id.toString())
        Box(modifier = Modifier
            .width(60.dp)
            .height(40.dp)
        ){
            Image(painter = painterResource(id = item.imageId),
                contentDescription = "cover",
                modifier = Modifier
                    .padding(3.dp)
                    .size(64.dp)
            )
        }
        Text(text = item.title)
    }
}

//Delete?
@Composable
fun OpenGameInfo(item: DataItemDetailedGameTemp, context: CompositionContext){
    val openDialog = remember { mutableStateOf(false) }

    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = {
                openDialog.value = false
            },
            title = { Text(text = "Подтверждение действия") },
            text = { Text("Вы действительно хотите удалить выбранный элемент?") },
            buttons = {
                Button(
                    onClick = { openDialog.value = false }
                ) {
                    Text("OK", fontSize = 22.sp)
                }
            }
        )
    }
}

//Delete?
@Composable
fun Open(open: MutableState<Boolean>): MutableState<Boolean> {
    open.value = true
    Box(modifier = Modifier
        .padding(5.dp)
        .height(15.dp)
        .width(15.dp)
        .background(Color.White)

    ){
        Text(text = "Gloomhaven")
    }
    return open
}

//WORK
//заготовка для графиков
@Composable
fun ItemsBox(screenSize: Int, value: Int, color: Color) {
    Box(modifier = Modifier
        .padding(5.dp)
        .height(15.dp)
        .width(value.dp)
        .background(color)

    ){
        Text(text = "Gloomhaven")
    }
}

