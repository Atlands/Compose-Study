package com.atlands.composestudy

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.view.MotionEvent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.palette.graphics.Palette

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PaletteBackground() {
    val context = LocalContext.current
    val imgs = listOf(R.mipmap.img1, R.mipmap.img2, R.mipmap.img3, R.mipmap.img4)

    var mouseIndex by remember { mutableStateOf(-1) }

    val palette by derivedStateOf {
        if (mouseIndex == -1) {
            null
        } else {
            val bitmap = BitmapFactory.decodeResource(context.resources, imgs[mouseIndex])
            val palette = Palette.from(bitmap).generate()
            palette
        }
    }

    Box(
        modifier = Modifier
            .background(
                brush = Brush.verticalGradient(
                    if (palette == null) listOf(Color.White,Color.White)
                    else listOf(
                        Color(palette!!.lightVibrantSwatch!!.rgb),
                        Color(palette!!.lightMutedSwatch!!.rgb),
                        Color(palette!!.dominantSwatch!!.rgb),
                    )
                )
            )
            .fillMaxHeight()
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(vertical = 50.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
//            userScrollEnabled = false,
        ) {
            items(4) { index ->
                val selected = index == mouseIndex
                val scale by animateFloatAsState(targetValue = if (selected) 1.2f else 1f)
                Image(painter = painterResource(id = imgs[index]),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .aspectRatio(1f)
                        .scale(scale)
                        .border(
                            width = 2.dp,
                            color = if (selected) Color.White else Color.Transparent
                        )

                        .clickable {
                            mouseIndex = index
                        }
                        .pointerInteropFilter {
                            if (it.action == MotionEvent.ACTION_HOVER_MOVE) mouseIndex = index
                            else if (it.action == MotionEvent.ACTION_HOVER_EXIT) mouseIndex = -1
                            false
                        })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(
    backgroundColor = 0xFFFFFFFF, showBackground = true,
    device = "id:pixel_3a_xl"
)
@Composable
fun PalettePreview() {
    Scaffold {
        it
        PaletteBackground()
    }
}