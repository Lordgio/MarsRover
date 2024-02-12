package dev.jorgeroldan.marsrover.domain.model

import java.io.Serializable

data class InstructionItem(
    val topRightCorner: Coordinates,
    val roverPosition: Coordinates,
    val roverDirection: RoverDirection,
    val encodedMovements: String,
    val movements: List<RoverMovement>,
) : Serializable

data class Coordinates(
    val x: Int,
    val y: Int,
) : Serializable

enum class RoverDirection {
    NORTH, SOUTH, EAST, WEST;

    override fun toString(): String {
        return when (this) {
            NORTH -> "N"
            SOUTH -> "S"
            EAST -> "E"
            WEST -> "W"
        }
    }
}

enum class RoverMovement {
    SPIN_LEFT, SPIN_RIGHT, MOVE;

    override fun toString(): String {
        return when (this) {
            SPIN_LEFT -> "L"
            SPIN_RIGHT -> "R"
            MOVE -> "M"
        }
    }
}
