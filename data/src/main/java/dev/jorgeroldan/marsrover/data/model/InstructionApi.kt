package dev.jorgeroldan.marsrover.data.model

import kotlinx.serialization.Serializable

@Serializable
data class InstructionApi(
    val name: String,
    val urlPath: String,
)
