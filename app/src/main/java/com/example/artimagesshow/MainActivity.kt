package com.example.artimagesshow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.artimagesshow.ui.theme.ArtImagesShowTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ArtImagesShowTheme {
                Surface (modifier = Modifier.fillMaxSize()) {
                    ArtImagesShowLayout()
                }
            }
        }
    }
}

data class Artwork(
    @DrawableRes val imageRes: Int,
    @StringRes val titleRes: Int,
    @StringRes val artistRes: Int
)


val artworks = listOf(
    Artwork(R.drawable.lemon_tree, R.string.artwork_title_1, R.string.artwork_artist_1),
    Artwork(R.drawable.lemon_squeeze, R.string.artwork_title_2, R.string.artwork_artist_2),
    Artwork(R.drawable.lemon_drink, R.string.artwork_title_3, R.string.artwork_artist_3),
    Artwork(R.drawable.lemon_restart, R.string.artwork_title_4, R.string.artwork_artist_4)
)

@Composable
fun ArtImagesShowLayout() {
    var currentIndex by remember { mutableIntStateOf(0) }

    val (image, artworkTitle, artworkArtist) = artworks[currentIndex]

    Column (
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .safeDrawingPadding()
            .padding(vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Box(
            modifier = Modifier
                .size(width = 300.dp, height = 450.dp)
                .shadow(
                    elevation = 30.dp,
                    shape = RoundedCornerShape(10.dp),
                    clip = false
                )
                .background(Color.White),
            contentAlignment = Alignment.Center
        ){
            Image(
                painter = painterResource(image),
                contentDescription = null,
                modifier = Modifier.padding(20.dp)
            )
        }

        Column(
            modifier = Modifier.padding(top = 15.dp).size(width = 300.dp, height = 150.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = stringResource(artworkTitle),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis)
            Text(modifier = Modifier.padding(top = 10.dp),
                text = stringResource(artworkArtist),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis)
        }

        Row(
            modifier = Modifier.size(width = 300.dp, height = 50.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ArtImagesShowButton(text = R.string.button_1,
                onClick = {
                    currentIndex = (currentIndex - 1 + artworks.size) % artworks.size
                },
                modifier = Modifier.padding(start = 20.dp))
            ArtImagesShowButton(text = R.string.button_2,
                onClick = {
                    currentIndex = (currentIndex + 1) % artworks.size
                },
                modifier = Modifier.padding(end = 20.dp))
        }
    }
}

@Composable
fun ArtImagesShowButton(@StringRes text: Int, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(modifier = modifier
        .padding(top = 10.dp)
        .size(width = 110.dp, height = 40.dp),
        onClick = onClick) {
        Text(stringResource(text))
    }
}


@Composable
fun RotatingCardSwitcher() {
    // 卡片颜色列表
    val colors = listOf(
        Color(0xFFFFC107), // 黄色
        Color(0xFF03A9F4), // 蓝色
        Color(0xFF8BC34A), // 绿色
        Color(0xFFF06292), // 粉色
        Color(0xFF9575CD)  // 紫色
    )

    // 当前显示的卡片索引
    var currentIndex by remember { mutableStateOf(0) }

    // 控制卡片的水平偏移和旋转动画
    val offsetX = remember { Animatable(0f) }
    val rotationY = remember { Animatable(0f) }

    // 获取协程作用域
    val coroutineScope = rememberCoroutineScope()

    // 切换卡片的函数
    suspend fun switchCard(newIndex: Int) {
        // 设置动画方向，向左或向右滑动
        val direction = if (newIndex > currentIndex) 1 else -1

        // 同步旋转和滑动动画，先滑出当前卡片
        coroutineScope.launch {
            launch {
                offsetX.animateTo(targetValue = 300f * direction, animationSpec = tween(durationMillis = 500))
            }
            launch {
                rotationY.animateTo(targetValue = 90f * direction, animationSpec = tween(durationMillis = 500))
            }
        }.join()

        // 更新索引并重置偏移和旋转
        currentIndex = newIndex
        offsetX.snapTo(-300f * direction) // 立即设置到另一侧的初始位置
        rotationY.snapTo(-90f * direction) // 从另一侧开始旋转

        // 从另一侧滑入新卡片
        coroutineScope.launch {
            launch {
                offsetX.animateTo(targetValue = 0f, animationSpec = tween(durationMillis = 500))
            }
            launch {
                rotationY.animateTo(targetValue = 0f, animationSpec = tween(durationMillis = 500))
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE0E0E0)), // 背景色
        contentAlignment = Alignment.Center
    ) {

        colors.forEachIndexed { index, color ->
            Card(
                modifier = Modifier
                    .size(200.dp)
                    .graphicsLayer(
                        rotationZ = -20f + (index * 10f), // 设置旋转角度
                        translationX = (index - colors.size / 2) * 30f // 水平偏移
                    ),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                colors = CardDefaults.cardColors(containerColor = color)
            ) {}
        }

        Card(
            modifier = Modifier
                .size(200.dp)
                .graphicsLayer(
                    translationX = offsetX.value,
                    rotationY = rotationY.value // 添加围绕 Y 轴的旋转效果
                ),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(containerColor = colors[currentIndex])
        ) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                Text(
                    text = "Card ${currentIndex + 1}",
                    color = Color.White,
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }

        // 切换按钮
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = {
                    if (currentIndex > 0) {
                        coroutineScope.launch { switchCard(currentIndex - 1) }
                    }
                }
            ) {
                Text("Previous")
            }
            Button(
                onClick = {
                    if (currentIndex < colors.size - 1) {
                        coroutineScope.launch { switchCard(currentIndex + 1) }
                    }
                }
            ) {
                Text("Next")
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ArtImagesShowTheme {
        ArtImagesShowLayout()
    }
}