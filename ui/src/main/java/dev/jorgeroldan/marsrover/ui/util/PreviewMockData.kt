package dev.jorgeroldan.marsrover.ui.util

import dev.jorgeroldan.marsrover.domain.model.Coordinates
import dev.jorgeroldan.marsrover.domain.model.Instruction
import dev.jorgeroldan.marsrover.domain.model.InstructionItem
import dev.jorgeroldan.marsrover.domain.model.InstructionResolution
import dev.jorgeroldan.marsrover.domain.model.MovementStep
import dev.jorgeroldan.marsrover.domain.model.RoverDirection
import dev.jorgeroldan.marsrover.domain.model.RoverMovement

object PreviewMockData {

    val instructionList = listOf(
        Instruction("Name 1", "https://111.com"),
        Instruction("Name 2", "https://222.com"),
        Instruction("Name 3", "https://333.com"),
        Instruction("Name 4", "https://444.com"),
        Instruction("Name 5", "https://555.com"),
    )

    val instructionReport = InstructionResolution(
        initial = InstructionItem(
            topRightCorner = Coordinates(5, 5),
            roverPosition = Coordinates(1, 1),
            roverDirection = RoverDirection.NORTH,
            encodedMovements = "MML",
            movements = listOf(
                RoverMovement.MOVE,
                RoverMovement.MOVE,
                RoverMovement.SPIN_LEFT
            )
        ),
        finalPosition = Coordinates(1, 3),
        finalOrientation = RoverDirection.WEST,
        finalResolution = "1 3 W",
        steps = listOf(
            MovementStep(
                initialCoordinates = Coordinates(0, 0),
                initialOrientation = RoverDirection.NORTH,
                finalPosition = Coordinates(1, 1),
                finalOrientation = RoverDirection.NORTH,
                movement = RoverMovement.MOVE,
                movementDescription = "Start"
            ),
            MovementStep(
                initialCoordinates = Coordinates(1, 1),
                initialOrientation = RoverDirection.NORTH,
                finalPosition = Coordinates(1, 2),
                finalOrientation = RoverDirection.NORTH,
                movement = RoverMovement.MOVE,
                movementDescription = "Move 1"
            ),
            MovementStep(
                initialCoordinates = Coordinates(1, 2),
                initialOrientation = RoverDirection.NORTH,
                finalPosition = Coordinates(1, 3),
                finalOrientation = RoverDirection.NORTH,
                movement = RoverMovement.MOVE,
                movementDescription = "Move 2"
            ),
            MovementStep(
                initialCoordinates = Coordinates(1, 3),
                initialOrientation = RoverDirection.NORTH,
                finalPosition = Coordinates(1, 3),
                finalOrientation = RoverDirection.WEST,
                movement = RoverMovement.SPIN_LEFT,
                movementDescription = "Rotate"
            )
        )
    )
}