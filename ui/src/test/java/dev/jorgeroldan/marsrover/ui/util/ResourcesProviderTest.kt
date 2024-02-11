package dev.jorgeroldan.marsrover.ui.util

import android.content.Context
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class ResourcesProviderTest {

    @Test
    fun `getString recovers it from context`() {
        val context: Context = mockk(relaxed = true)
        val provider = ResourcesProviderImpl(context)
        provider.getString(1234)

        verify { context.getString(1234) }
    }
}