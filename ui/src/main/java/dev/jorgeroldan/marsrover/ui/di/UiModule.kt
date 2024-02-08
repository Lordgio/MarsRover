package dev.jorgeroldan.marsrover.ui.di

import dev.jorgeroldan.marsrover.ui.features.instructions.SelectInstructionsViewModel
import dev.jorgeroldan.marsrover.ui.features.viewer.InstructionsViewerViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object UiModule {
    val module = module {
        viewModel<SelectInstructionsViewModel> { SelectInstructionsViewModel(get(), get()) }
        viewModel<InstructionsViewerViewModel> { InstructionsViewerViewModel(get(), get(), get()) }
    }
}