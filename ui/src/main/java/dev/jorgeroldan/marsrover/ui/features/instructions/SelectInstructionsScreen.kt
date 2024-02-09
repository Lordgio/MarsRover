package dev.jorgeroldan.marsrover.ui.features.instructions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.jorgeroldan.marsrover.domain.model.Instruction
import dev.jorgeroldan.marsrover.ui.R
import dev.jorgeroldan.marsrover.ui.theme.MarsRoverTheme
import dev.jorgeroldan.marsrover.ui.util.ScreenPreview
import dev.jorgeroldan.marsrover.ui.components.FullScreenLoader
import dev.jorgeroldan.marsrover.ui.components.GenericErrorScreen
import dev.jorgeroldan.marsrover.ui.util.ComponentPreview
import dev.jorgeroldan.marsrover.ui.util.PreviewMockData
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import org.koin.androidx.compose.koinViewModel

@Composable
fun SelectInstructionsScreen(
    onInstructionSelected: (Instruction) -> Unit,
    modifier: Modifier = Modifier,
    onCreateInstructionsSelected: () -> Unit,
) {

    val viewModel: SelectInstructionsViewModel = koinViewModel()

    val state = viewModel.state.collectAsStateWithLifecycle()

    when (val value = state.value) {
        is SelectInstructionsViewModel.SelectInstructionsState.Data -> {
            SelectInstructionsScreenContent(
                items = value.items,
                modifier = modifier,
                onInstructionSelected = onInstructionSelected,
                onCreateInstructionsSelected = onCreateInstructionsSelected,
            )
        }
        SelectInstructionsViewModel.SelectInstructionsState.Error -> GenericErrorScreen()
        SelectInstructionsViewModel.SelectInstructionsState.Idle -> { /* no-op */ }
        SelectInstructionsViewModel.SelectInstructionsState.Loading -> FullScreenLoader()
    }
}

@Composable
private fun SelectInstructionsScreenContent(
    items: ImmutableList<Instruction>,
    onInstructionSelected: (Instruction) -> Unit,
    modifier: Modifier = Modifier,
    onCreateInstructionsSelected: () -> Unit,
) {

    Column(
        modifier = modifier
            .systemBarsPadding()
            .padding(16.dp),
    ) {
        Text(
            text = stringResource(id = R.string.select_instructions_list_title),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleMedium
        )
        LazyColumn(
            contentPadding = PaddingValues(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items) {
                InstructionCard(
                    instruction = it,
                    onInstructionSelected = onInstructionSelected
                )
            }
        }
        Text(
            text = stringResource(id = R.string.select_instructions_custom_title),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleMedium)
        FilledTonalButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = onCreateInstructionsSelected
        ) {
            Text(stringResource(id = R.string.select_instructions_custom_button))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun InstructionCard(
    instruction: Instruction,
    modifier: Modifier = Modifier,
    onInstructionSelected: (Instruction) -> Unit,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        onClick = { onInstructionSelected.invoke(instruction) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = instruction.name)
            Icon(Icons.AutoMirrored.Rounded.ArrowForward, contentDescription = "")
        }
    }
}

@ScreenPreview
@Composable
private fun SelectInstructionsScreenContentPreview() {

    MarsRoverTheme {
        SelectInstructionsScreenContent(
            items = PreviewMockData.instructionList.toImmutableList(),
            onInstructionSelected = {},
            onCreateInstructionsSelected = {}
        )
    }
}

@ComponentPreview
@Composable
private fun InstructionCardPreview() {
    MarsRoverTheme {
        InstructionCard(
            instruction = PreviewMockData.instructionList.first(),
            onInstructionSelected = {})
    }
}