package dev.jorgeroldan.marsrover.ui.util

import org.junit.Assert
import org.junit.Test

class PreviewMockDataTest {

    @Test
    fun `instructionList returns list of 5 instructions`() {
        val result = PreviewMockData.instructionList
        Assert.assertEquals(5, result.size)
    }

    @Test
    fun `instructionReport returns valid report`() {
        val result = PreviewMockData.instructionReport
        Assert.assertEquals("1 3 W", result.finalResolution)
    }
}