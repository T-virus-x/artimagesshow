package com.example.artimagesshow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.artimagesshow.ui.theme.ArtImagesShowTheme

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

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ArtImagesShowTheme {
        ArtImagesShowLayout()
    }
}