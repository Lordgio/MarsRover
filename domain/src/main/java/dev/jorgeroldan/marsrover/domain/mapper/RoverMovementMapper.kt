package dev.jorgeroldan.marsrover.domain.mapper

import dev.jorgeroldan.marsrover.domain.model.RoverMovement

fun String.toRoverMovement(): List<RoverMovement> {
    return this.toCharArray()
        .asSequence()
        .map { char -> char.toRoverMovement() }
        .filterNotNull()
        .toList()
}

private fun Char.toRoverMovement(): RoverMovement? {
    return when (this.uppercaseChar()) {
        'L' -> RoverMovement.SPIN_LEFT
        'R' -> RoverMovement.SPIN_RIGHT
        'M' -> RoverMovement.MOVE
        else -> null
    }
}