package dev.jorgeroldan.marsrover.ui.features.builder

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.jorgeroldan.marsrover.domain.model.InstructionItem
import dev.jorgeroldan.marsrover.domain.model.RoverDirection
import dev.jorgeroldan.marsrover.domain.model.RoverMovement
import dev.jorgeroldan.marsrover.ui.R
import dev.jorgeroldan.marsrover.ui.theme.MarsRoverTheme
import dev.jorgeroldan.marsrover.ui.util.ComponentPreview
import dev.jorgeroldan.marsrover.ui.util.PreviewMockData
import dev.jorgeroldan.marsrover.ui.util.ScreenPreview
import org.koin.androidx.compose.koinViewModel

@Composable
fun InstructionsBuilderScreen(
    modifier: Modifier = Modifier,
    viewModel: InstructionsBuilderViewModel = koinViewModel(),
    onNavigate: (InstructionItem) -> Unit = {},
    onBack: () -> Unit,
) {
    val state = viewModel.state.collectAsStateWithLifecycle()

    when (val value = state.value) {
        is InstructionsBuilderViewModel.InstructionsBuilderState.Data -> {
            InstructionsBuilderScreenContent(
                item = value.item,
                modifier = modifier,
                onViewEvent = { viewModel.onViewEvent(it) },
                onNavigate = {
                    val item = viewModel.getCurrent()
                    onNavigate.invoke(item)
                },
                onBack = onBack
            )
        }
        InstructionsBuilderViewModel.InstructionsBuilderState.Idle -> { /* no-op */ }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun InstructionsBuilderScreenContent(
    item: InstructionItem,
    modifier: Modifier = Modifier,
    onViewEvent: (InstructionsBuilderViewModel.InstructionsBuilderEvents) -> Unit = {},
    onNavigate: () -> Unit = {},
    onBack: () -> Unit,
) {
    Column(
        modifier = modifier
            .systemBarsPadding()
            .padding(16.dp)
            .testTag("ContentScreen"),
    ) {
        CenterAlignedTopAppBar(
            title = { Text(text = stringResource(id = R.string.instructions_builder_toolbar_title)) },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "")
                }
            }
        )
        CoordinatesCard(
            title = stringResource(id = R.string.instructions_builder_field_title),
            xValue = item.topRightCorner.x,
            yValue = item.topRightCorner.y,
            onIncrementX = { onViewEvent(InstructionsBuilderViewModel.InstructionsBuilderEvents.OnIncrementXFieldSize) },
            onDecrementX = { onViewEvent(InstructionsBuilderViewModel.InstructionsBuilderEvents.OnDecrementXFieldSize) },
            onIncrementY = { onViewEvent(InstructionsBuilderViewModel.InstructionsBuilderEvents.OnIncrementYFieldSize) },
            onDecrementY = { onViewEvent(InstructionsBuilderViewModel.InstructionsBuilderEvents.OnDecrementYFieldSize) }
        )
        CoordinatesCard(
            title = stringResource(id = R.string.instructions_builder_position_title),
            xValue = item.roverPosition.x,
            yValue = item.roverPosition.y,
            onIncrementX = { onViewEvent(InstructionsBuilderViewModel.InstructionsBuilderEvents.OnIncrementXRoverPosition) },
            onDecrementX = { onViewEvent(InstructionsBuilderViewModel.InstructionsBuilderEvents.OnDecrementXRoverPosition) },
            onIncrementY = { onViewEvent(InstructionsBuilderViewModel.InstructionsBuilderEvents.OnIncrementYRoverPosition) },
            onDecrementY = { onViewEvent(InstructionsBuilderViewModel.InstructionsBuilderEvents.OnDecrementYRoverPosition) }
        )

        CardinalPointSelector(
            currentValue = item.roverDirection,
            onValueSelected = { onViewEvent(InstructionsBuilderViewModel.InstructionsBuilderEvents.OnOrientationChanged(it)) }
        )
        MovementSelector(
            currentValue = item.encodedMovements,
            onMovementAdded = { onViewEvent(InstructionsBuilderViewModel.InstructionsBuilderEvents.OnMovementAdded(it))},
        )

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = onNavigate
        ) {
            Text(text = stringResource(id = R.string.instructions_builder_button_text))
        }
    }
}

@Composable
private fun NumberSelector(
    currentValue: Int,
    modifier: Modifier = Modifier,
    onNumberIncremented: () -> Unit = {},
    onNumberDecremented: () -> Unit = {},
) {

    Row(
        modifier = modifier
            .border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(6.dp)),
        verticalAlignment = Alignment.CenterVertically
    ) {

        IconButton(onClick = onNumberDecremented) {
            Icon(
                painter = painterResource(id = R.drawable.remove),
                contentDescription = "Decrease",
                tint = MaterialTheme.colorScheme.onSurface)
        }
        Text(
            text = currentValue.toString(),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface)
        IconButton(onClick = onNumberIncremented) {
            Icon(
                imageVector = Icons.Rounded.Add,
                contentDescription = "Increase",
                tint = MaterialTheme.colorScheme.onSurface)
        }
    }
}

@Composable
private fun CoordinatesCard(
    title: String,
    xValue: Int,
    yValue: Int,
    onIncrementX: () -> Unit,
    onDecrementX: () -> Unit,
    onIncrementY: () -> Unit,
    onDecrementY: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = title,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                NumberSelector(
                    currentValue = xValue,
                    onNumberIncremented = onIncrementX,
                    onNumberDecremented = onDecrementX,
                )
                Text(
                    text = "-",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                NumberSelector(
                    currentValue = yValue,
                    onNumberIncremented = onIncrementY,
                    onNumberDecremented = onDecrementY
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CardinalPointSelector(
    currentValue: RoverDirection,
    modifier: Modifier = Modifier,
    onValueSelected: (RoverDirection) -> Unit = {},
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = R.string.instructions_builder_orientation_title),
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(12.dp))
            val items = listOf(
                stringResource(id = R.string.instructions_builder_orientation_north) to RoverDirection.NORTH,
                stringResource(id = R.string.instructions_builder_orientation_south) to RoverDirection.SOUTH,
                stringResource(id = R.string.instructions_builder_orientation_west) to RoverDirection.WEST,
                stringResource(id = R.string.instructions_builder_orientation_east) to RoverDirection.EAST,
            )
            SingleChoiceSegmentedButtonRow {
                items.forEachIndexed { index, (name, value) ->
                    SegmentedButton(
                        selected = currentValue == value,
                        onClick = { onValueSelected(value) },
                        shape = SegmentedButtonDefaults.itemShape(index = index, count = items.size),
                        label = { Text(text = name) },
                        icon = { Icon(getIconForDirection(direction = value), contentDescription = "")}
                    )
                }
            }
        }
    }
}

@Composable
private fun getIconForDirection(direction: RoverDirection): Painter {
    return when (direction) {
        RoverDirection.NORTH -> painterResource(id = R.drawable.arrow_circle_up)
        RoverDirection.SOUTH -> painterResource(id = R.drawable.arrow_circle_down)
        RoverDirection.EAST -> painterResource(id = R.drawable.arrow_circle_right)
        RoverDirection.WEST -> painterResource(id = R.drawable.arrow_circle_left)
    }
}

@Composable
private fun MovementSelector(
    currentValue: String,
    modifier: Modifier = Modifier,
    onMovementAdded: (RoverMovement) -> Unit = {},
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = R.string.instructions_builder_movements_title),
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                text = currentValue,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            val possibleMovements = listOf(
                stringResource(id = R.string.instructions_builder_movement_left) to RoverMovement.SPIN_LEFT,
                stringResource(id = R.string.instructions_builder_movement_move) to RoverMovement.MOVE,
                stringResource(id = R.string.instructions_builder_movement_right) to RoverMovement.SPIN_RIGHT
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                possibleMovements.forEach { (title, value) ->
                    Button(
                        onClick = { onMovementAdded(value) },
                        content = { Text(text = title)}
                    )
                }
            }
        }
    }
}


@ScreenPreview
@Composable
private fun InstructionsBuilderScreenContentPreview() {
    MarsRoverTheme {
        InstructionsBuilderScreenContent(
            item = PreviewMockData.instructionReport.initial,
            onViewEvent = {},
            onNavigate = {},
            onBack = {}
        )
    }
}

@ComponentPreview
@Composable
private fun NumberSelectorPreview() {
    MarsRoverTheme {
        NumberSelector(currentValue = 3)
    }
}