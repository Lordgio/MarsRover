package dev.jorgeroldan.marsrover.ui.features.app

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.jorgeroldan.marsrover.ui.features.instructions.SelectInstructionsScreen
import dev.jorgeroldan.marsrover.ui.theme.MarsRoverTheme

@Composable
fun App() {
    MarsRoverTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            val navController = rememberNavController()

            NavHost(
                navController = navController,
                startDestination = Screens.SelectInstructionsScreen.route
            ) {
                composable(Screens.SelectInstructionsScreen.route) {
                    SelectInstructionsScreen(
                        modifier = Modifier.fillMaxSize(),
                        onInstructionSelected = {},
                        onCreateInstructionsSelected = {}
                    )
                }
            }
        }
    }
}

sealed class Screens(val route: String) {
    data object SelectInstructionsScreen : Screens("selectInstructions")
}
