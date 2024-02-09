package dev.jorgeroldan.marsrover.domain.mapper

import dev.jorgeroldan.marsrover.domain.model.Coordinates
import dev.jorgeroldan.marsrover.domain.model.InstructionItem
import dev.jorgeroldan.marsrover.domain.model.InstructionResolution
import dev.jorgeroldan.marsrover.domain.model.MovementStep
import dev.jorgeroldan.marsrover.domain.model.RoverDirection
import dev.jorgeroldan.marsrover.domain.model.RoverMovement

object InstructionsMapper {

    suspend fun processRoverPosition(instruction: InstructionItem, moveText: String, rotateText: String): InstructionResolution {
        val movements = generateMovements(
            initialPosition = instruction.roverPosition,
            initialOrientation = instruction.roverDirection,
            movements = instruction.movements,
            moveText = moveText,
            rotateText = rotateText)

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
        movements: List<RoverMovement>,
        moveText: String,
        rotateText: String
    ): List<MovementStep> {

        val initialMovement = MovementStep(
            initialCoordinates = Coordinates(0,0),
            initialOrientation = RoverDirection.NORTH,
            finalPosition = initialPosition,
            finalOrientation = initialOrientation,
            movement = RoverMovement.MOVE,
            movementDescription = "Rover on Start position: ${initialPosition.x} - ${initialPosition.y}"
        )
        return movements.runningFold(initialMovement) { accumulator, step ->
            val currentInitialPosition = accumulator.finalPosition
            val currentInitialOrientation = accumulator.finalOrientation
            val finalPosition = calculateNewPosition(accumulator.finalPosition, step, accumulator.finalOrientation)
            val finalOrientation = calculateNewOrientation(step, accumulator.finalOrientation)
            MovementStep(
                initialCoordinates = currentInitialPosition,
                initialOrientation = currentInitialOrientation,
                finalPosition = finalPosition,
                finalOrientation = finalOrientation,
                movement = step,
                movementDescription = if (step == RoverMovement.MOVE) {
                    formatMoveDescription(moveText, currentInitialPosition, finalPosition)
                } else {
                    createRotateDescription(rotateText, currentInitialOrientation, finalOrientation)
                }
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
        return when (movement) {
            RoverMovement.SPIN_LEFT -> {
                when (orientation) {
                    RoverDirection.NORTH -> RoverDirection.WEST
                    RoverDirection.SOUTH -> RoverDirection.EAST
                    RoverDirection.EAST -> RoverDirection.NORTH
                    RoverDirection.WEST -> RoverDirection.SOUTH
                }
            }
            RoverMovement.SPIN_RIGHT -> {
                when (orientation) {
                    RoverDirection.NORTH -> RoverDirection.EAST
                    RoverDirection.SOUTH -> RoverDirection.WEST
                    RoverDirection.EAST -> RoverDirection.SOUTH
                    RoverDirection.WEST -> RoverDirection.NORTH
                }
            }
            else -> {
                orientation
            }
        }
    }

    private fun encodePosition(coordinates: Coordinates, orientation: RoverDirection): String {
        return "${coordinates.x} ${coordinates.y} $orientation"
    }

    private fun formatMoveDescription(
        moveText: String,
        initialPosition: Coordinates,
        finalPosition: Coordinates
    ): String {
        return moveText.format(
            "${initialPosition.x} - ${initialPosition.y}",
            "${finalPosition.x} - ${finalPosition.y}"
        )
    }

    private fun createRotateDescription(
        rotateText: String,
        initialOrientation: RoverDirection,
        finalOrientation: RoverDirection
    ): String {
        return rotateText.format(
            initialOrientation,
            finalOrientation
        )
    }
}