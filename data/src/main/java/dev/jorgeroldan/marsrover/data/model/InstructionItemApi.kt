package dev.jorgeroldan.marsrover.data.model

import kotlinx.serialization.Serializable

@Serializable
data class InstructionItemApi(
    val topRightCorner: Coordinates,
    val roverPosition: Coordinates,
    val roverDirection: String,
    val movements: String,
)

@Serializable
data class Coordinates(
    val x: Int,
    val y: Int,
)
