package dev.jorgeroldan.marsrover.ui.di

import dev.jorgeroldan.marsrover.ui.features.instructions.SelectInstructionsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object UiModule {
    val module = module {
        viewModel<SelectInstructionsViewModel> { SelectInstructionsViewModel(get()) }
    }
}