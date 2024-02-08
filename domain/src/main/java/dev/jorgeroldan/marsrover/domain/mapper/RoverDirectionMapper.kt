package dev.jorgeroldan.marsrover.domain.mapper

import dev.jorgeroldan.marsrover.domain.model.RoverDirection

fun String.toRoverDirection(): RoverDirection {
    return when (this) {
        "N", "n" -> RoverDirection.NORTH
        "S", "s" -> RoverDirection.SOUTH
        "W", "w" -> RoverDirection.WEST
        "E", "e" -> RoverDirection.EAST
        else -> RoverDirection.NORTH
    }
}