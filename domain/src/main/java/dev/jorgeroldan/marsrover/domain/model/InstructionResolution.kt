package dev.jorgeroldan.marsrover.domain.model

import java.io.Serializable

data class InstructionResolution(
    val initial: InstructionItem,
    val finalPosition: Coordinates,
    val finalOrientation: RoverDirection,
    val finalResolution: String,
    val steps: List<MovementStep>
) : Serializable

data class MovementStep(
    val initialCoordinates: Coordinates,
    val initialOrientation: RoverDirection,
    val finalPosition: Coordinates,
    val finalOrientation: RoverDirection,
    val movement: RoverMovement,
    val movementDescription: String,
) : Serializable

data class FriendlyUserText(
    val startText: String,
    val moveText: String,
    val rotateText: String,
) : Serializable
