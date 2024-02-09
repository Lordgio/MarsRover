package dev.jorgeroldan.marsrover.ui.features.instructions

import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.jorgeroldan.marsrover.domain.model.Instruction
import dev.jorgeroldan.marsrover.domain.usecase.GetInstructionsListUseCase
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

class SelectInstructionsViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val useCase: GetInstructionsListUseCase
) : ViewModel() {

    val state = savedStateHandle.getStateFlow<SelectInstructionsState>(SELECT_INSTRUCTIONS_STATE, SelectInstructionsState.Idle)

    init {
        initViewmodel()
    }

    private fun initViewmodel() = viewModelScope.launch(Dispatchers.IO) {
        savedStateHandle[SELECT_INSTRUCTIONS_STATE] = SelectInstructionsState.Loading

        useCase.invoke().fold(
            ifLeft = { savedStateHandle[SELECT_INSTRUCTIONS_STATE] = SelectInstructionsState.Error },
            ifRight = {
                items -> savedStateHandle[SELECT_INSTRUCTIONS_STATE] = SelectInstructionsState.Data(items.toImmutableList())
            }
        )
    }

    sealed interface SelectInstructionsState {
        @Parcelize
        data object Idle : SelectInstructionsState, Parcelable
        @Parcelize
        data object Loading : SelectInstructionsState, Parcelable
        @Parcelize
        data class Data(val items: @RawValue ImmutableList<Instruction>): SelectInstructionsState, Parcelable
        @Parcelize
        data object Error : SelectInstructionsState, Parcelable
    }

    companion object {
        private const val SELECT_INSTRUCTIONS_STATE = "select_instructions_state"
    }
}