package dev.jorgeroldan.marsrover.domain.mapper

import arrow.core.Either
import dev.jorgeroldan.marsrover.domain.model.Coordinates
import dev.jorgeroldan.marsrover.domain.model.FriendlyUserText
import dev.jorgeroldan.marsrover.domain.model.InstructionItem
import dev.jorgeroldan.marsrover.domain.model.RoverDirection
import dev.jorgeroldan.marsrover.domain.model.RoverMovement
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class InstructionsMapperTest {

    private val defaultTexts = FriendlyUserText("", "", "")

    @Test
    fun `processRoverPosition with empty movements`() = runTest {
        val item = InstructionItem(Coordinates(5, 5), Coordinates(0, 0), RoverDirection.WEST, "", listOf())
        val result = InstructionsMapper.processRoverPosition(item, defaultTexts)

        Assert.assertTrue(result is Either.Right)
        Assert.assertEquals(1, result.getOrNull()!!.steps.size)
    }

    @Test
    fun `processRoverPosition with all movements`() = runTest {
        val movements = listOf(RoverMovement.SPIN_LEFT, RoverMovement.SPIN_RIGHT, RoverMovement.MOVE)
        val item = InstructionItem(Coordinates(5, 5), Coordinates(0, 0), RoverDirection.EAST, "LRM", movements)
        val result = InstructionsMapper.processRoverPosition(item, defaultTexts)

        Assert.assertTrue(result is Either.Right)
        Assert.assertEquals(4, result.getOrNull()!!.steps.size)
        Assert.assertEquals("1 0 E", result.getOrNull()!!.finalResolution)
    }

    @Test
    fun `processRoverPosition with all possible left movement directions`() = runTest {
        val movements = listOf(
            RoverMovement.MOVE,
            RoverMovement.SPIN_LEFT,
            RoverMovement.MOVE,
            RoverMovement.SPIN_LEFT,
            RoverMovement.MOVE,
            RoverMovement.SPIN_LEFT,
            RoverMovement.MOVE,
            RoverMovement.SPIN_LEFT,
            RoverMovement.SPIN_LEFT,
            RoverMovement.SPIN_LEFT,)
        val item = InstructionItem(Coordinates(5, 5), Coordinates(2, 2), RoverDirection.NORTH, "MLMLMLMLLL", movements)
        val result = InstructionsMapper.processRoverPosition(item, defaultTexts)

        Assert.assertTrue(result is Either.Right)
        Assert.assertEquals(11, result.getOrNull()!!.steps.size)
        Assert.assertEquals("2 2 S", result.getOrNull()!!.finalResolution)
    }

    @Test
    fun `processRoverPosition with all possible right movement directions`() = runTest {
        val movements = listOf(
            RoverMovement.MOVE,
            RoverMovement.SPIN_RIGHT,
            RoverMovement.MOVE,
            RoverMovement.SPIN_RIGHT,
            RoverMovement.MOVE,
            RoverMovement.SPIN_RIGHT,
            RoverMovement.MOVE,
            RoverMovement.SPIN_RIGHT,)
        val item = InstructionItem(Coordinates(5, 5), Coordinates(2, 2), RoverDirection.NORTH, "MRMRMRMR", movements)
        val result = InstructionsMapper.processRoverPosition(item, defaultTexts)

        Assert.assertTrue(result is Either.Right)
        Assert.assertEquals(9, result.getOrNull()!!.steps.size)
        Assert.assertEquals("2 2 N", result.getOrNull()!!.finalResolution)
    }

    @Test
    fun `processRoverPosition with movements failure`() = runTest {
        val movements = listOf(
            RoverMovement.SPIN_LEFT,
            RoverMovement.MOVE,
            RoverMovement.SPIN_LEFT,
            RoverMovement.MOVE,)
        val item = InstructionItem(Coordinates(2, 2), Coordinates(0, 0), RoverDirection.NORTH, "LMLM", movements)
        val result = InstructionsMapper.processRoverPosition(item, defaultTexts)

        Assert.assertTrue(result is Either.Left)
    }
}