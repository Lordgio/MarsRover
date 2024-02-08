package dev.jorgeroldan.marsrover.domain.model

data class InstructionResolution(
    val initial: InstructionItem,
    val finalPosition: Coordinates,
    val finalOrientation: RoverDirection,
    val finalResolution: String,
    val steps: List<MovementStep>
)

data class MovementStep(
    val initialCoordinates: Coordinates,
    val initialOrientation: RoverDirection,
    val finalPosition: Coordinates,
    val finalOrientation: RoverDirection,
    val movement: RoverMovement
)
