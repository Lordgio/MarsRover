package dev.jorgeroldan.marsrover.domain.mapper

import dev.jorgeroldan.marsrover.domain.model.RoverDirection
import org.junit.Assert
import org.junit.Test

class RoverDirectionMapperTest {

    @Test
    fun `toRoverDirection with N returns NORTH`() {
        Assert.assertEquals(RoverDirection.NORTH, "N".toRoverDirection())
    }

    @Test
    fun `toRoverDirection with n returns NORTH`() {
        Assert.assertEquals(RoverDirection.NORTH, "n".toRoverDirection())
    }

    @Test
    fun `toRoverDirection with S returns SOUTH`() {
        Assert.assertEquals(RoverDirection.SOUTH, "S".toRoverDirection())
    }

    @Test
    fun `toRoverDirection with s returns SOUTH`() {
        Assert.assertEquals(RoverDirection.SOUTH, "s".toRoverDirection())
    }

    @Test
    fun `toRoverDirection with W returns WEST`() {
        Assert.assertEquals(RoverDirection.WEST, "W".toRoverDirection())
    }

    @Test
    fun `toRoverDirection with w returns WEST`() {
        Assert.assertEquals(RoverDirection.WEST, "w".toRoverDirection())
    }

    @Test
    fun `toRoverDirection with E returns EAST`() {
        Assert.assertEquals(RoverDirection.EAST, "E".toRoverDirection())
    }

    @Test
    fun `toRoverDirection with e returns EAST`() {
        Assert.assertEquals(RoverDirection.EAST, "e".toRoverDirection())
    }

    @Test
    fun `toRoverDirection with invalid value returns NORTH`() {
        Assert.assertEquals(RoverDirection.NORTH, "d".toRoverDirection())
    }
}