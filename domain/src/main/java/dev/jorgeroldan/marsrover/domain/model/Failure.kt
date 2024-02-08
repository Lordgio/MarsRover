package dev.jorgeroldan.marsrover.domain.model


sealed class Failure(val message: String) {

    data class HttpFailure(val errorCode: String, val errorMessage: String) : Failure("$errorCode: $errorMessage")
    data class IOFailure(val cause: String) : Failure(cause)
    data class UnexpectedFailure(val cause: String): Failure(cause)
}