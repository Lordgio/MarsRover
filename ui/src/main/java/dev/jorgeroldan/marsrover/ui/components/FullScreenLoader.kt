package dev.jorgeroldan.marsrover.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag

@Composable
fun FullScreenLoader(modifier: Modifier = Modifier) {

    Box(
        modifier = modifier
            .fillMaxSize()
            .testTag("LoadingScreen"),
        contentAlignment = Alignment.Center) {

        CircularProgressIndicator()
    }
}
