package dev.jorgeroldan.marsrover.ui.features.viewer

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.jorgeroldan.marsrover.domain.model.InstructionResolution
import dev.jorgeroldan.marsrover.domain.model.MovementStep
import dev.jorgeroldan.marsrover.domain.model.RoverDirection
import dev.jorgeroldan.marsrover.ui.R
import dev.jorgeroldan.marsrover.ui.components.FullScreenLoader
import dev.jorgeroldan.marsrover.ui.components.GenericErrorScreen
import dev.jorgeroldan.marsrover.ui.components.GridCanvas
import dev.jorgeroldan.marsrover.ui.theme.MarsRoverTheme
import dev.jorgeroldan.marsrover.ui.util.PreviewMockData
import dev.jorgeroldan.marsrover.ui.util.ScreenPreview
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun InstructionsViewerScreen(
    instructionPath: String,
    modifier: Modifier = Modifier,
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
            .systemBarsPadding()
            .padding(16.dp)
            .verticalScroll(
                state = rememberScrollState(),
                flingBehavior = ScrollableDefaults.flingBehavior()
            ),
    ) {
        IconButton(
            onClick = onBack
        ) {
            Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "")
        }
        GenericInfoCard(
            sectionTitle = stringResource(id = R.string.instructions_viewer_preconditions_title),
            items = persistentListOf(
                "Grid size:" to "${report.initial.topRightCorner.x} x ${report.initial.topRightCorner.y}",
                "Rover position:" to "${report.initial.roverPosition.x} x ${report.initial.roverPosition.y}",
                "Rover orientation:" to "${report.initial.roverDirection}",
                "Movements:" to report.initial.encodedMovements,
            )
        )
        GenericInfoCard(
            sectionTitle = stringResource(id = R.string.instructions_viewer_report_title),
            items = persistentListOf(
                "Rover position:" to "${report.finalPosition.x} x ${report.finalPosition.y}",
                "Rover orientation:" to "${report.finalOrientation}",
                "Final position:" to report.finalResolution
            )
        )
        Spacer(modifier = Modifier.height(20.dp))
        if (report.steps.isNotEmpty()) {
            ReplayMovements(
                columns = report.initial.topRightCorner.y,
                rows = report.initial.topRightCorner.x,
                movements = report.steps.toImmutableList()
            )
        }
    }
}

@Composable
private fun GenericInfoCard(
    sectionTitle: String,
    items: ImmutableList<Pair<String, String>>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = sectionTitle,
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center
        )
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                items.forEach { (title, value) ->
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = value,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}

@Composable
private fun ReplayMovements(
    columns: Int,
    rows: Int,
    movements: ImmutableList<MovementStep>,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val animatedElement by infiniteTransition.animateValue(
        initialValue = 0,
        targetValue = movements.size,
        typeConverter = Int.VectorConverter,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000 * movements.size, delayMillis = 2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "anim"
    )
    val currentMovement by rememberUpdatedState(newValue = movements[animatedElement])

    val vectorNorthPainter = rememberVectorPainter(image = Icons.Rounded.KeyboardArrowUp)
    val vectorSouthPainter = rememberVectorPainter(image = Icons.Rounded.KeyboardArrowDown)
    val vectorEastPainter = rememberVectorPainter(image = Icons.Rounded.KeyboardArrowRight)
    val vectorWestPainter = rememberVectorPainter(image = Icons.Rounded.KeyboardArrowLeft)

    Column(
        modifier = modifier
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.instructions_viewer_events_title),
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center
        )
        Text(
            modifier = Modifier.padding(vertical = 8.dp),
            text = currentMovement.movementDescription,
            style = MaterialTheme.typography.bodyLarge
        )
        GridCanvas(
            rows = rows,
            columns = columns,
        ) { row, column, size, color ->
            if (currentMovement.finalPosition.x == column && currentMovement.finalPosition.y == row) {
                val currentPainter = when (currentMovement.finalOrientation) {
                    RoverDirection.NORTH -> vectorNorthPainter
                    RoverDirection.SOUTH -> vectorSouthPainter
                    RoverDirection.EAST -> vectorEastPainter
                    RoverDirection.WEST -> vectorWestPainter
                }

                translate(
                    left = (size / 2) - (currentPainter.intrinsicSize.width / 2),
                    top = (size / 2) - (currentPainter.intrinsicSize.height / 2),
                ) {
                    with(currentPainter) {
                        draw(
                            size = currentPainter.intrinsicSize,
                            colorFilter = ColorFilter.tint(color)
                        )
                    }
                }
            }
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