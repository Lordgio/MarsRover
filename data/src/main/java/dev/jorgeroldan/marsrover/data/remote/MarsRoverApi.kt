package dev.jorgeroldan.marsrover.data.remote

import arrow.core.Either
import arrow.retrofit.adapter.either.networkhandling.CallError
import dev.jorgeroldan.marsrover.data.model.Instruction
import dev.jorgeroldan.marsrover.data.model.InstructionItem
import retrofit2.http.GET
import retrofit2.http.Path

interface MarsRoverApi {

    @GET("instructionsList.json")
    suspend fun getInstructionsList() : Either<CallError, List<Instruction>>

    @GET("{instructionName}.json")
    suspend fun getInstruction(
        @Path("instructionName") instructionName: String,
    ) : Either<CallError, InstructionItem>
}