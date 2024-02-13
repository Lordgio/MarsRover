package dev.jorgeroldan.marsrover.ui.features.instructions

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import dev.jorgeroldan.marsrover.ui.theme.MarsRoverTheme
import io.mockk.every
import io.mockk.mockk
import kotlinx.collections.immutable.persistentListOf
import org.junit.Rule
import org.junit.Test

class SelectInstructionsScreenTest {

    private val viewModel: SelectInstructionsViewModel = mockk(relaxed = true)
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun settingLoadingValueSetsProperScreenContent() {
        every { viewModel.state.value } returns SelectInstructionsViewModel.SelectInstructionsState.Loading
        composeTestRule.setContent {
            MarsRoverTheme {
                SelectInstructionsScreen(
                    viewModel = viewModel,
                    onInstructionSelected = {},
                    onCreateInstructionsSelected = {},
                    onBack = {})
            }
        }

        composeTestRule
            .onNodeWithTag("LoadingScreen")
            .assertIsDisplayed()
    }

    @Test
    fun settingErrorValueSetsProperScreenContent() {
        every { viewModel.state.value } returns SelectInstructionsViewModel.SelectInstructionsState.Error
        composeTestRule.setContent {
            MarsRoverTheme {
                SelectInstructionsScreen(
                    viewModel = viewModel,
                    onInstructionSelected = {},
                    onCreateInstructionsSelected = {},
                    onBack = {})
            }
        }

        composeTestRule
            .onNodeWithTag("ErrorScreen")
            .assertIsDisplayed()
    }

    @Test
    fun settingDataValueSetsProperScreenContent() {
        every { viewModel.state.value } returns SelectInstructionsViewModel.SelectInstructionsState.Data(persistentListOf())

        composeTestRule.setContent {
            MarsRoverTheme {
                SelectInstructionsScreen(
                    viewModel = viewModel,
                    onInstructionSelected = {},
                    onCreateInstructionsSelected = {},
                    onBack = {})
            }
        }

        composeTestRule
            .onNodeWithTag("ContentScreen")
            .assertIsDisplayed()
    }
}
