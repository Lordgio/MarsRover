package dev.jorgeroldan.marsrover.ui.util

import dev.jorgeroldan.marsrover.domain.model.Coordinates
import dev.jorgeroldan.marsrover.domain.model.Instruction
import dev.jorgeroldan.marsrover.domain.model.InstructionItem
import dev.jorgeroldan.marsrover.domain.model.InstructionResolution
import dev.jorgeroldan.marsrover.domain.model.RoverDirection

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
            encodedMovements = "",
            movements = listOf()
        ),
        finalPosition = Coordinates(1, 1),
        finalOrientation = RoverDirection.NORTH,
        finalResolution = "1 1 N",
        steps = listOf()
    )
}