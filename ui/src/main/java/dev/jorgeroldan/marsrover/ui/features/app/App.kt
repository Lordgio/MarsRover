package dev.jorgeroldan.marsrover.ui.features.app

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.jorgeroldan.marsrover.domain.mapper.toRoverDirection
import dev.jorgeroldan.marsrover.domain.mapper.toRoverMovement
import dev.jorgeroldan.marsrover.domain.model.Coordinates
import dev.jorgeroldan.marsrover.domain.model.InstructionItem
import dev.jorgeroldan.marsrover.domain.model.RoverDirection
import dev.jorgeroldan.marsrover.ui.features.builder.InstructionsBuilderScreen
import dev.jorgeroldan.marsrover.ui.features.instructions.SelectInstructionsScreen
import dev.jorgeroldan.marsrover.ui.features.viewer.InstructionsViewerScreen
import dev.jorgeroldan.marsrover.ui.theme.MarsRoverTheme

@Composable
fun App(
    modifier: Modifier = Modifier
) {
    MarsRoverTheme {
        Surface(
            modifier = modifier.fillMaxSize(),
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
                        onInstructionSelected = { instruction ->
                            navController.navigate(Screens.InstructionsViewerScreen.route + "/${instruction.urlPath}") },
                        onCreateInstructionsSelected = {
                            navController.navigate(Screens.InstructionsBuilderScreen.route)
                        },
                        onBack = {
                            navController.popBackStack()
                        }
                    )
                }
                composable(Screens.InstructionsViewerScreen.route + "/{path}") { backStackEntry ->
                    InstructionsViewerScreen(
                        modifier = Modifier.fillMaxSize(),
                        instructionPath = backStackEntry.arguments?.getString("path") ?: "",
                        onBack = { navController.popBackStack() }
                    )
                }
                composable(Screens.InstructionsViewerScreen.route + "/{fieldX}/{fieldY}/{roverX}/{roverY}/{direction}/{movements}") { backStackEntry ->
                    val item = InstructionItem(
                        topRightCorner = Coordinates(
                            x = backStackEntry.arguments?.getString("fieldX")?.toIntOrNull() ?: 0,
                            y = backStackEntry.arguments?.getString("fieldY")?.toIntOrNull() ?: 0,
                        ),
                        roverPosition = Coordinates(
                            x = backStackEntry.arguments?.getString("roverX")?.toIntOrNull() ?: 0,
                            y = backStackEntry.arguments?.getString("roverY")?.toIntOrNull() ?: 0,
                        ),
                        roverDirection = backStackEntry.arguments?.getString("direction")?.toRoverDirection() ?: RoverDirection.NORTH,
                        encodedMovements = backStackEntry.arguments?.getString("movements") ?: "",
                        movements = (backStackEntry.arguments?.getString("movements") ?: "").toRoverMovement()
                    )
                    InstructionsViewerScreen(
                        modifier = Modifier.fillMaxSize(),
                        instruction = item,
                        onBack = { navController.popBackStack() }
                    )
                }
                composable(Screens.InstructionsBuilderScreen.route) {
                    InstructionsBuilderScreen(
                        modifier = Modifier.fillMaxSize(),
                        onBack = { navController.popBackStack() },
                        onNavigate = { item ->
                            navController.navigate(Screens.InstructionsViewerScreen.route +
                                    "/${item.topRightCorner.x}/${item.topRightCorner.y}" +
                                    "/${item.roverPosition.x}/${item.roverPosition.y}" +
                                    "/${item.roverDirection}/${item.encodedMovements}"
                            )
                        }
                    )
                }
            }
        }
    }
}

sealed class Screens(val route: String) {
    data object SelectInstructionsScreen : Screens("selectInstructions")
    data object InstructionsViewerScreen : Screens("instructionsViewer")
    data object InstructionsBuilderScreen : Screens("instructionsBuilder")
}
