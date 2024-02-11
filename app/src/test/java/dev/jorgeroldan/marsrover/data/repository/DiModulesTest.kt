package dev.jorgeroldan.marsrover.data.repository

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import dev.jorgeroldan.marsrover.data.di.DataModule
import dev.jorgeroldan.marsrover.domain.di.DomainModule
import dev.jorgeroldan.marsrover.ui.di.UiModule
import io.mockk.mockk
import org.junit.Test
import org.koin.dsl.koinApplication
import org.koin.test.check.checkModules

class DiModulesTest {

    @Test
    fun `verify di modules`() {
        val contextMock: Context = mockk(relaxed = true)
        val stateHandleMock: SavedStateHandle = mockk(relaxed = true)
        koinApplication {
            modules(
                DataModule.module,
                DomainModule.module,
                UiModule.module
            )
            checkModules {
                withInstance(contextMock)
                withInstance(stateHandleMock)
            }
        }
    }
}