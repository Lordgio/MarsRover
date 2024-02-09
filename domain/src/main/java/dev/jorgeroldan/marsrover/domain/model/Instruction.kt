package dev.jorgeroldan.marsrover.domain.model

import java.io.Serializable

data class Instruction(
    val name: String,
    val urlPath: String,
) : Serializable
