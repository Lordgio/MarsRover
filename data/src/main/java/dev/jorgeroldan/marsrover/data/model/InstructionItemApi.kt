package dev.jorgeroldan.marsrover.data.model

import dev.jorgeroldan.marsrover.domain.mapper.toRoverDirection
import dev.jorgeroldan.marsrover.domain.mapper.toRoverMovement
import dev.jorgeroldan.marsrover.domain.model.Coordinates
import dev.jorgeroldan.marsrover.domain.model.InstructionItem
import kotlinx.serialization.Serializable

@Serializable
data class InstructionItemApi(
    val topRightCorner: CoordinatesApi,
    val roverPosition: CoordinatesApi,
    val roverDirection: String,
    val movements: String,
)

@Serializable
data class CoordinatesApi(
    val x: Int,
    val y: Int,
)

fun InstructionItemApi.toDomainModel(): InstructionItem {
    return InstructionItem(
        topRightCorner = Coordinates(topRightCorner.x, topRightCorner.y),
        roverPosition = Coordinates(roverPosition.x, roverPosition.y),
        roverDirection = roverDirection.toRoverDirection(),
        encodedMovements = movements,
        movements = movements.toRoverMovement(),
    )
}
