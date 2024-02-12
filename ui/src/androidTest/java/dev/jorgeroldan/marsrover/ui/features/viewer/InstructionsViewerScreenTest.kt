package dev.jorgeroldan.marsrover.ui.features.viewer

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import dev.jorgeroldan.marsrover.domain.model.Coordinates
import dev.jorgeroldan.marsrover.domain.model.InstructionItem
import dev.jorgeroldan.marsrover.domain.model.InstructionResolution
import dev.jorgeroldan.marsrover.domain.model.MovementStep
import dev.jorgeroldan.marsrover.domain.model.RoverDirection
import dev.jorgeroldan.marsrover.domain.model.RoverMovement
import dev.jorgeroldan.marsrover.ui.theme.MarsRoverTheme
import io.mockk.every
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test

class InstructionsViewerScreenTest {

    private val viewModel: InstructionsViewerViewModel = mockk(relaxed = true)
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun settingLoadingValueSetsProperScreenContent() {
        every { viewModel.state.value } returns InstructionsViewerViewModel.InstructionsViewerState.Loading
        composeTestRule.setContent {
            MarsRoverTheme {
                InstructionsViewerScreen(
                    viewModel = viewModel,
                    instructionPath = "test",
                    onBack = {}
                )
            }
        }

        composeTestRule
            .onNodeWithTag("LoadingScreen")
            .assertIsDisplayed()
    }

    @Test
    fun settingErrorValueSetsProperScreenContent() {
        every { viewModel.state.value } returns InstructionsViewerViewModel.InstructionsViewerState.Error
        composeTestRule.setContent {
            MarsRoverTheme {
                InstructionsViewerScreen(
                    viewModel = viewModel,
                    instructionPath = "test",
                    onBack = {}
                )
            }
        }

        composeTestRule
            .onNodeWithTag("ErrorScreen")
            .assertIsDisplayed()
    }

    @Test
    fun settingDataValueSetsProperScreenContent() {
        val response = InstructionItem(Coordinates(5, 5), Coordinates(0,0), RoverDirection.NORTH, "", listOf())
        val firstStep = MovementStep(Coordinates(0,0), RoverDirection.NORTH, Coordinates(0,0), RoverDirection.NORTH, RoverMovement.MOVE, "")
        val report = InstructionResolution(response, Coordinates(0,0), RoverDirection.NORTH, "0 0 N", listOf(firstStep))
        every { viewModel.state.value } returns InstructionsViewerViewModel.InstructionsViewerState.Data(report)

        composeTestRule.setContent {
            MarsRoverTheme {
                InstructionsViewerScreen(
                    viewModel = viewModel,
                    instructionPath = "test",
                    onBack = {}
                )
            }
        }

        composeTestRule
            .onNodeWithTag("ContentScreen")
            .assertIsDisplayed()
    }
}