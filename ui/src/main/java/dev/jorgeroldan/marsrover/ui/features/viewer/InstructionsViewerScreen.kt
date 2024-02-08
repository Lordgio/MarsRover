package dev.jorgeroldan.marsrover.ui.features.viewer

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.jorgeroldan.marsrover.domain.model.Coordinates
import dev.jorgeroldan.marsrover.domain.model.InstructionItem
import dev.jorgeroldan.marsrover.domain.model.InstructionResolution
import dev.jorgeroldan.marsrover.domain.model.RoverDirection
import dev.jorgeroldan.marsrover.ui.components.FullScreenLoader
import dev.jorgeroldan.marsrover.ui.components.GenericErrorScreen
import dev.jorgeroldan.marsrover.ui.theme.MarsRoverTheme
import dev.jorgeroldan.marsrover.ui.util.PreviewMockData
import dev.jorgeroldan.marsrover.ui.util.ScreenPreview
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun InstructionsViewerScreen(
    modifier: Modifier = Modifier,
    instructionPath: String,
    onBack: () -> Unit,
) {

    val viewModel: InstructionsViewerViewModel = koinViewModel { parametersOf(instructionPath) }

    val state = viewModel.state.collectAsStateWithLifecycle()

    when (val value = state.value) {
        is InstructionsViewerViewModel.InstructionsViewerState.Data -> {
            InstructionsViewerScreenContent(
                report = value.result,
                modifier = modifier,
                onBack = onBack,
            )
        }
        InstructionsViewerViewModel.InstructionsViewerState.Error -> GenericErrorScreen()
        InstructionsViewerViewModel.InstructionsViewerState.Idle -> { /* no-op */ }
        InstructionsViewerViewModel.InstructionsViewerState.Loading -> FullScreenLoader()
    }
}

@Composable
private fun InstructionsViewerScreenContent(
    report: InstructionResolution,
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
) {
    Column(
        modifier = modifier
    ) {
        IconButton(
            onClick = onBack
        ) {
            Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "")
        }
        InitialConditionsSection(report.initial)
        FinalResultSection(
            finalPosition = report.finalPosition,
            finalOrientation = report.finalOrientation,
            movementCode = report.finalResolution
        )
    }
}

@Composable
private fun InitialConditionsSection(
    initialConditions: InstructionItem,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text("Initial conditions")
        Card {
            Text(text = "Grid size: ${initialConditions.topRightCorner.x} x ${initialConditions.topRightCorner.y}")
            Text(text = "Rover position: ${initialConditions.roverPosition.x} x ${initialConditions.roverPosition.y}")
            Text(text = "Rover orientation: ${initialConditions.roverDirection}")
            Text(text = "Movements: ${initialConditions.encodedMovements}")
        }
    }
}

@Composable
private fun FinalResultSection(
    finalPosition: Coordinates,
    finalOrientation: RoverDirection,
    movementCode: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text("Final result")
        Card {
            Text(text = "Rover position: ${finalPosition.x} x ${finalPosition.y}")
            Text(text = "Rover orientation: $finalOrientation")
            Text(text = "Movement: $movementCode")
        }
    }
}

@ScreenPreview
@Composable
private fun InstructionsViewerScreenContentPreview() {
    MarsRoverTheme {
        InstructionsViewerScreenContent(
            report = PreviewMockData.instructionReport,
            onBack = {})
    }
}