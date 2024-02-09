package dev.jorgeroldan.marsrover.ui.di

import dev.jorgeroldan.marsrover.ui.features.instructions.SelectInstructionsViewModel
import dev.jorgeroldan.marsrover.ui.features.viewer.InstructionsViewerViewModel
import dev.jorgeroldan.marsrover.ui.util.ResourcesProvider
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object UiModule {
    val module = module {
        single<ResourcesProvider> { ResourcesProvider(get()) }
        viewModel<SelectInstructionsViewModel> { SelectInstructionsViewModel(get(), get()) }
        viewModel<InstructionsViewerViewModel> { InstructionsViewerViewModel(get(), get(), get(), get()) }
    }
}