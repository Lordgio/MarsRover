package dev.jorgeroldan.marsrover.ui.di

import dev.jorgeroldan.marsrover.ui.features.builder.InstructionsBuilderViewModel
import dev.jorgeroldan.marsrover.ui.features.instructions.SelectInstructionsViewModel
import dev.jorgeroldan.marsrover.ui.features.viewer.InstructionsViewerViewModel
import dev.jorgeroldan.marsrover.ui.util.ResourcesProvider
import dev.jorgeroldan.marsrover.ui.util.ResourcesProviderImpl
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object UiModule {
    val module = module {
        single<ResourcesProvider> { ResourcesProviderImpl(get()) }
        single<CoroutineDispatcher> { Dispatchers.IO }
        viewModel<SelectInstructionsViewModel> { SelectInstructionsViewModel(get(), get(), get()) }
        viewModel<InstructionsViewerViewModel> { InstructionsViewerViewModel(get(), get(), get(), get()) }
        viewModel<InstructionsBuilderViewModel> { InstructionsBuilderViewModel(get()) }
    }
}