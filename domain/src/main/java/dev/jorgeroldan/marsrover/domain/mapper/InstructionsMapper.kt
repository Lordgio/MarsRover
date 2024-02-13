package dev.jorgeroldan.marsrover.domain.mapper

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import dev.jorgeroldan.marsrover.domain.model.Coordinates
import dev.jorgeroldan.marsrover.domain.model.FriendlyUserText
import dev.jorgeroldan.marsrover.domain.model.InstructionFailure
import dev.jorgeroldan.marsrover.domain.model.InstructionItem
import dev.jorgeroldan.marsrover.domain.model.InstructionResolution
import dev.jorgeroldan.marsrover.domain.model.MovementStep
import dev.jorgeroldan.marsrover.domain.model.RoverDirection
import dev.jorgeroldan.marsrover.domain.model.RoverMovement

object InstructionsMapper {

    fun processRoverPosition(
        instruction: InstructionItem,
        userTexts: FriendlyUserText,
    ): Either<InstructionFailure, InstructionResolution> {
        val initialMovement = generateFirstMovement(
            initialPosition = instruction.roverPosition,
            initialOrientation = instruction.roverDirection,
            userTexts = userTexts
        )
        val movements = generateMovements(
            initialMovement = initialMovement,
            movements = instruction.movements,
            userTexts = userTexts)
        val validMovements = getValidMovements(movements, instruction.topRightCorner)

        val lastMovement = validMovements.lastOrNull() ?: initialMovement
        val report = InstructionResolution(
            initial = instruction,
            finalPosition = lastMovement.finalPosition,
            finalOrientation = lastMovement.finalOrientation,
            finalResolution = encodePosition(
                coordinates = lastMovement.finalPosition,
                orientation = lastMovement.finalOrientation),
            steps = validMovements
        )

        return if(movements.size == validMovements.size) report.right() else InstructionFailure(report).left()
    }

    private fun generateFirstMovement(
        initialPosition: Coordinates,
        initialOrientation: RoverDirection,
        userTexts: FriendlyUserText,
    ): MovementStep {
        return MovementStep(
            initialCoordinates = Coordinates(0,0),
            initialOrientation = RoverDirection.NORTH,
            finalPosition = initialPosition,
            finalOrientation = initialOrientation,
            movement = RoverMovement.MOVE,
            movementDescription = userTexts.startText.format(
                initialPosition.x,
                initialPosition.y
            )
        )
    }

    private fun generateMovements(
        initialMovement: MovementStep,
        movements: List<RoverMovement>,
        userTexts: FriendlyUserText,
    ): List<MovementStep> {
        
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
                    formatMoveDescription(userTexts.moveText, currentInitialPosition, finalPosition)
                } else {
                    createRotateDescription(userTexts.rotateText, currentInitialOrientation, finalOrientation)
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

    private fun getValidMovements(movements: List<MovementStep>, fieldCoordinates: Coordinates): List<MovementStep> {
        val xFieldSize = 0 .. fieldCoordinates.x
        val yFieldSize = 0 .. fieldCoordinates.y

        val index = movements.indexOfFirst { step ->
            !(xFieldSize.contains(step.finalPosition.x)) || !(yFieldSize.contains(step.finalPosition.y)) }
        val listIndex = if (index == 0) index else index - 1
        return if (index == -1) movements else movements.subList(0, listIndex)
    }
}