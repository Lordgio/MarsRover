package dev.jorgeroldan.marsrover.ui.features.viewer

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.jorgeroldan.marsrover.domain.model.InstructionItem
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

@Composable
fun InstructionsViewerScreen(
    instructionPath: String,
    modifier: Modifier = Modifier,
    viewModel: InstructionsViewerViewModel = koinViewModel(),
    onBack: () -> Unit,
) {
    val state = viewModel.state.collectAsStateWithLifecycle()

    when (val value = state.value) {
        is InstructionsViewerViewModel.InstructionsViewerState.Data -> {
            InstructionsViewerScreenContent(
                report = value.result,
                modifier = modifier,
                onBack = onBack,
            )
        }
        InstructionsViewerViewModel.InstructionsViewerState.Error -> GenericErrorScreen(onBackClick = onBack)
        InstructionsViewerViewModel.InstructionsViewerState.Idle -> { /* no-op */ }
        InstructionsViewerViewModel.InstructionsViewerState.Loading -> FullScreenLoader()
    }

    LaunchedEffect(key1 = null) {
        viewModel.initViewModel(instructionUrlPath = instructionPath)
    }
}

@Composable
fun InstructionsViewerScreen(
    instruction: InstructionItem,
    modifier: Modifier = Modifier,
    viewModel: InstructionsViewerViewModel = koinViewModel(),
    onBack: () -> Unit,
) {
    val state = viewModel.state.collectAsStateWithLifecycle()

    when (val value = state.value) {
        is InstructionsViewerViewModel.InstructionsViewerState.Data -> {
            InstructionsViewerScreenContent(
                report = value.result,
                modifier = modifier,
                onBack = onBack,
            )
        }
        InstructionsViewerViewModel.InstructionsViewerState.Error -> GenericErrorScreen(onBackClick = onBack)
        InstructionsViewerViewModel.InstructionsViewerState.Idle -> { /* no-op */ }
        InstructionsViewerViewModel.InstructionsViewerState.Loading -> FullScreenLoader()
    }

    LaunchedEffect(key1 = null) {
        viewModel.initViewModel(instructionModel = instruction)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
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
            .testTag("ContentScreen")
            .verticalScroll(
                state = rememberScrollState(),
                flingBehavior = ScrollableDefaults.flingBehavior()
            ),
    ) {
        CenterAlignedTopAppBar(
            title = { Text(text = stringResource(id = R.string.instructions_viewer_toolbar_title)) },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "")
                }
            }
        )

        GenericInfoCard(
            sectionTitle = stringResource(id = R.string.instructions_viewer_preconditions_title),
            items = persistentListOf(
                stringResource(id = R.string.instructions_viewer_info_grid) to "${report.initial.topRightCorner.x} x ${report.initial.topRightCorner.y}",
                stringResource(id = R.string.instructions_viewer_info_position) to "${report.initial.roverPosition.x} x ${report.initial.roverPosition.y}",
                stringResource(id = R.string.instructions_viewer_info_orientation) to "${report.initial.roverDirection}",
                stringResource(id = R.string.instructions_viewer_info_movements) to report.initial.encodedMovements,
            )
        )
        GenericInfoCard(
            sectionTitle = stringResource(id = R.string.instructions_viewer_report_title),
            items = persistentListOf(
                stringResource(id = R.string.instructions_viewer_info_position) to "${report.finalPosition.x} x ${report.finalPosition.y}",
                stringResource(id = R.string.instructions_viewer_info_orientation) to "${report.finalOrientation}",
                stringResource(id = R.string.instructions_viewer_info_final) to report.finalResolution
            )
        )
        if (report.steps.isNotEmpty()) {
            ReplayMovements(
                columns = report.initial.topRightCorner.x,
                rows = report.initial.topRightCorner.y,
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
    var activeAnimation by remember { mutableStateOf(false) }
    val currentMovement by animateListState(items = movements, activeAnimation)
    val vectorNorthPainter = painterResource(id = R.drawable.arrow_circle_up)
    val vectorSouthPainter = painterResource(id = R.drawable.arrow_circle_down)
    val vectorEastPainter = painterResource(id = R.drawable.arrow_circle_right)
    val vectorWestPainter = painterResource(id = R.drawable.arrow_circle_left)

    if (currentMovement == movements.last()) {
        activeAnimation = false
    }

    Column(
        modifier = modifier
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.instructions_viewer_events_title),
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center
        )
        FilledTonalButton(
            enabled = !activeAnimation,
            onClick = { activeAnimation = true }
        ) {
            Icon(painter = painterResource(id = R.drawable.play_circle), contentDescription = "")
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Play animation")
        }
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            text = currentMovement.movementDescription,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
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

@Composable
fun animateListState(
    items: ImmutableList<MovementStep>,
    isActive: Boolean,
) : State<MovementStep> {
    val state = remember { mutableStateOf(items.first()) }

    if (isActive) {
        val (_, setValue) = state
        LaunchedEffect(key1 = items) {
            setValue(items.first())
            animate(
                typeConverter = Int.VectorConverter,
                initialValue = 0,
                targetValue = items.size - 1,
                animationSpec = tween(
                    durationMillis = 2000 * items.size,
                    easing = LinearEasing
                ),
                block = { value, _ -> setValue(items[value]) }
            )
        }
    }

    return state
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