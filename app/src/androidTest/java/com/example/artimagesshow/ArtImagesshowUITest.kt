package com.example.artimagesshow

import androidx.compose.ui.test.junit4.createComposeRule
import com.example.artimagesshow.ui.theme.ArtImagesShowTheme
import org.junit.Rule
import org.junit.Test


class ArtImagesshowUITest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testArtImagesShow() {
        composeTestRule.setContent {
            ArtImagesShowTheme {
                ArtImagesShowLayout()
            }

        }
    }
}