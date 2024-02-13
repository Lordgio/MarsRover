package dev.jorgeroldan.marsrover.data.model

import dev.jorgeroldan.marsrover.domain.model.Instruction
import kotlinx.serialization.Serializable

@Serializable
data class InstructionApi(
    val name: String,
    val urlPath: String,
)

fun InstructionApi.toDomainModel(): Instruction {
    return Instruction(
        name = this.name,
        urlPath = this.urlPath)
}
