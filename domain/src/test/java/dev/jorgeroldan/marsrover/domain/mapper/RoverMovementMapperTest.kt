package dev.jorgeroldan.marsrover.domain.mapper

import dev.jorgeroldan.marsrover.domain.model.RoverMovement
import org.junit.Assert
import org.junit.Test

class RoverMovementMapperTest {

    @Test
    fun `toRoverMovement with empty string returns empty movements`() {
        Assert.assertEquals(0, "".toRoverMovement().size)
    }

    @Test
    fun `toRoverMovement with not valid movements returns empty movements`() {
        Assert.assertEquals(0, "ABCDE".toRoverMovement().size)
    }

    @Test
    fun `toRoverMovement with valid and invalid movements returns only valid movements`() {
        Assert.assertEquals(3, "ABCLDREM".toRoverMovement().size)
    }

    @Test
    fun `toRoverMovement with valid and invalid movements returns valid movements in proper order`() {
        val result = "ABCLDREM".toRoverMovement()
        Assert.assertEquals(RoverMovement.SPIN_LEFT, result[0])
        Assert.assertEquals(RoverMovement.SPIN_RIGHT, result[1])
        Assert.assertEquals(RoverMovement.MOVE, result[2])
    }
}