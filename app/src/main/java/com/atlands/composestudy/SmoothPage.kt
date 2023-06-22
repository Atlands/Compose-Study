import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.Text
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.atlands.composestudy.R
import kotlin.math.absoluteValue
import kotlin.math.sqrt

@Preview
@Composable
private fun Pre() {
    CircleRevealPager()
}

// ACTUAL OFFSET
@OptIn(ExperimentalFoundationApi::class)
fun PagerState.offsetForPage(page: Int) = (currentPage - page) + currentPageOffsetFraction

// OFFSET ONLY FROM THE LEFT
@OptIn(ExperimentalFoundationApi::class)
fun PagerState.startOffsetForPage(page: Int): Float {
    return offsetForPage(page).coerceAtLeast(0f)
}

// OFFSET ONLY FROM THE RIGHT
@OptIn(ExperimentalFoundationApi::class)
fun PagerState.endOffsetForPage(page: Int): Float {
    return offsetForPage(page).coerceAtMost(0f)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CircleRevealPager() {
    val images = listOf(R.mipmap.img1, R.mipmap.img2, R.mipmap.img3, R.mipmap.img4)
    val state = rememberPagerState { 100 }
    HorizontalPager(
        modifier = Modifier
            .padding(horizontal = 32.dp, vertical = 64.dp)
            .clip(RoundedCornerShape(25.dp))
            .background(Color.Black),
        state = state,
        flingBehavior = PagerDefaults.flingBehavior(state = state, snapPositionalThreshold = 0f)
    ) { page ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    val pageOffset = state.offsetForPage(page)
                    translationX = size.width * pageOffset

                    val endOffset = state.endOffsetForPage(page)

                    shadowElevation = 20f
                    shape = customShape(1f - endOffset.absoluteValue)
                    clip = true

                    val absoluteOffset = state.offsetForPage(page).absoluteValue
                    val scale = 1f + (absoluteOffset.absoluteValue * .4f)

                    scaleX = scale
                    scaleY = scale

                    val startOffset = state.startOffsetForPage(page)
                    alpha = (2f - startOffset) / 2f

                },
            contentAlignment = Alignment.Center,
        ) {
            Image(
                painter = painterResource(id = images[page % images.size]),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

private fun customShape(progress: Float) = GenericShape { size, _ ->
    addRect(
        Rect(
            offset = Offset(size.width * (1 - progress), 0f),
            size = Size(size.width * progress, size.height)
        )
    )
}
