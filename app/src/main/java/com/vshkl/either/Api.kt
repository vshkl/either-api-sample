package com.vshkl.either

import arrow.core.Either
import retrofit2.http.GET

interface Api {

    @GET("success")
    suspend fun getSuccess(): Either<ErrorResponseDto, SuccessResponseDto>

    @GET("error")
    suspend fun getError(): Either<ErrorResponseDto, SuccessResponseDto>
}

data class SuccessResponseDto(
    val data: SuccessDto,
) {

    data class SuccessDto(
        val id: Int,
        val username: String,
        val display: String,
    )
}

data class ErrorResponseDto(
    val data: ErrorDto,
) {

    data class ErrorDto(
        val message: String,
        val reasons: List<String>,
    )
}
