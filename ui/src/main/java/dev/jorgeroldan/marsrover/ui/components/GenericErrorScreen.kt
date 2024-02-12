package dev.jorgeroldan.marsrover.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.jorgeroldan.marsrover.ui.R

@Composable
fun GenericErrorScreen(
    modifier: Modifier = Modifier,
    errorMessage: String = stringResource(id = R.string.generic_error_message),
    errorButton: String = stringResource(id = R.string.generic_error_button),
    onBackClick: () -> Unit = {},
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(50.dp)
            .testTag("ErrorScreen"),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            modifier = Modifier.size(200.dp),
            painter = painterResource(id = R.mipmap.rover),
            contentDescription = "Error"
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = errorMessage,
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = onBackClick) {
            Text(
                text = errorButton,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
