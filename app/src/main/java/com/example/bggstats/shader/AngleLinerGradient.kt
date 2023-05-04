package com.example.bggstats.shader

import android.util.Log
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import kotlin.math.*

//colors: List<Pair<Float, Color>>
//colors: List<Color>

fun Modifier.angledGradientBackground(colorStops: Array<Pair<Float, Color>>, degrees: Float) = this.then(
    drawBehind {
        //- функция поворота градиента выходящего за границы элемента -

        //координаты высчитывать по двум внешним треугольникам, которые образуются при пересечении двух прямоугольников:
        //1ого от элемента (на котором должен быть градиент) и 2ого от повернутого градиента.
        // *при пересечении образуются 4 треугольника, но так-как противоположные треугольники симметричны мы
        //  будем использовать только два из них, меняя точки начала и конца градиента каждые 90 градусов

        val (x, y) = size

        val degreesNormalised = (degrees % 360).let { if (it < 0) it + 360 else it }

        //угол для расчетов
        val angleN = 90 - (degreesNormalised % 90)
        val angleNRad = Math.toRadians(angleN.toDouble())

        //гипотенуза для 1ого треугольника
        val hypot1 = abs((y * cos(angleNRad)))
        //зная угол поворота и гипотенузу оприделяем катеты - они же координаты 1й точки
        val x1 = (abs((hypot1 * sin(angleNRad)))).toFloat()
        val y1 = (abs((hypot1 * cos(angleNRad)))).toFloat()

        //гипотенуза для 2ого треугольника
        val hypot2 = abs((x * cos(angleNRad)))
        //зная угол поворота и гипотенузу оприделяем катеты - они же координаты 2й точки
        val x2 = (abs((hypot2 * cos(angleNRad)))).toFloat()
        val y2 = (abs((hypot2 * sin(angleNRad)))).toFloat()

        //каждые 90 градусов мы меняем точки начала и конца градиента
        val offset = when  {
            degreesNormalised > 0f && degreesNormalised < 90f -> arrayOf(
                0f - x1, y - y1,
                x - x2, y + y2)
            degreesNormalised == 90f -> arrayOf(0f, 0f, 0f, y)
            degreesNormalised > 90f && degreesNormalised < 180f -> arrayOf(
                0f + x2, 0f - y2,
                0f - x1, y - y1)
            degreesNormalised == 180f -> arrayOf(x, 0f, 0f, 0f)
            degreesNormalised > 180f && degreesNormalised < 270f -> arrayOf(
                x + x1, 0f + y1,
                0f + x2, 0f - y2)
            degreesNormalised == 270f -> arrayOf(x, y, x, 0f)
            degreesNormalised > 270f && degreesNormalised < 360f -> arrayOf(
                x - x2, y + y2,
                x + x1, 0f + y1)
            else -> arrayOf(0f, y, x, y) //когда 0 и 360
        }

        drawRect(
            brush = androidx.compose.ui.graphics.Brush.linearGradient(
                colorStops = colorStops,
                /*colors = colors,*/
                start = Offset(offset[0],offset[1]),
                end = Offset(offset[2], offset[3])
            ),
            size = size
        )
    }
)