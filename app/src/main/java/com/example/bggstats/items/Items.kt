package com.example.bggstats.items

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
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
import com.example.bggstats.R

@Composable
fun ItemsGeneralGameList(item: DataItemGeneralGame) {
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
        Text(text = item.title)

        Column(modifier = Modifier.padding(start = 10.dp, top = 15.dp)) {

            /*maxLines = if(isExpandVal.value) Int.MAX_VALUE else 1)*/
        }
    }
}

@Composable
fun ItemsDetailedGameList(item: DataItemDetailedGame) {
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

