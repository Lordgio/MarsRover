package dev.jorgeroldan.marsrover.domain.mapper

import dev.jorgeroldan.marsrover.domain.model.Coordinates
import dev.jorgeroldan.marsrover.domain.model.InstructionItem
import dev.jorgeroldan.marsrover.domain.model.InstructionResolution
import dev.jorgeroldan.marsrover.domain.model.MovementStep
import dev.jorgeroldan.marsrover.domain.model.RoverDirection
import dev.jorgeroldan.marsrover.domain.model.RoverMovement

object InstructionsMapper {

    fun processRoverPosition(instruction: InstructionItem): InstructionResolution {
        val movements = generateMovements(
            initialPosition = instruction.roverPosition,
            initialOrientation = instruction.roverDirection,
            movements = instruction.movements)

        return InstructionResolution(
            initial = instruction,
            finalPosition = movements.last().finalPosition,
            finalOrientation = movements.last().finalOrientation,
            finalResolution = encodePosition(movements.last().finalPosition, movements.last().finalOrientation),
            steps = movements
        )
    }

    private fun generateMovements(
        initialPosition: Coordinates,
        initialOrientation: RoverDirection,
        movements: List<RoverMovement>
    ): List<MovementStep> {

        val initialMovement = MovementStep(
            initialCoordinates = Coordinates(0,0),
            initialOrientation = RoverDirection.NORTH,
            finalPosition = initialPosition,
            finalOrientation = initialOrientation,
            movement = RoverMovement.MOVE
        )
        return movements.runningFold(initialMovement) { accumulator, step ->
            MovementStep(
                initialCoordinates = accumulator.finalPosition,
                initialOrientation = accumulator.finalOrientation,
                finalPosition = calculateNewPosition(accumulator.finalPosition, step, accumulator.finalOrientation),
                finalOrientation = calculateNewOrientation(step, accumulator.finalOrientation),
                movement = step
            )
        }
    }

    private fun calculateNewPosition(
        position: Coordinates,
        movement: RoverMovement,
        orientation: RoverDirection
    ): Coordinates {
        return if (movement == RoverMovement.MOVE) {
            when (orientation) {
                RoverDirection.NORTH -> Coordinates(position.x, position.y + 1)
                RoverDirection.SOUTH -> Coordinates(position.x, position.y - 1)
                RoverDirection.EAST -> Coordinates(position.x + 1, position.y)
                RoverDirection.WEST -> Coordinates(position.x - 1, position.y)
            }
        } else {
            position
        }
    }

    private fun calculateNewOrientation(
        movement: RoverMovement,
        orientation: RoverDirection
    ): RoverDirection {
        return if (movement == RoverMovement.SPIN_LEFT) {
            when (orientation) {
                RoverDirection.NORTH -> RoverDirection.WEST
                RoverDirection.SOUTH -> RoverDirection.EAST
                RoverDirection.EAST -> RoverDirection.NORTH
                RoverDirection.WEST -> RoverDirection.SOUTH
            }
        } else if (movement == RoverMovement.SPIN_RIGHT) {
            when (orientation) {
                RoverDirection.NORTH -> RoverDirection.EAST
                RoverDirection.SOUTH -> RoverDirection.WEST
                RoverDirection.EAST -> RoverDirection.SOUTH
                RoverDirection.WEST -> RoverDirection.NORTH
            }
        } else {
            orientation
        }
    }

    private fun encodePosition(coordinates: Coordinates, orientation: RoverDirection): String {
        return "${coordinates.x} ${coordinates.y} $orientation"
    }
}