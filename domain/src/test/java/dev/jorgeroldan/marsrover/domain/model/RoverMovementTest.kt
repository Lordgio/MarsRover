package dev.jorgeroldan.marsrover.domain.model

import org.junit.Assert
import org.junit.Test

class RoverMovementTest {

    @Test
    fun `toString returns proper value for SPIN_LEFT`() {
        Assert.assertEquals("L", RoverMovement.SPIN_LEFT.toString())
    }

    @Test
    fun `toString returns proper value for SPIN_RIGHT`() {
        Assert.assertEquals("R", RoverMovement.SPIN_RIGHT.toString())
    }

    @Test
    fun `toString returns proper value for MOVE`() {
        Assert.assertEquals("M", RoverMovement.MOVE.toString())
    }
}