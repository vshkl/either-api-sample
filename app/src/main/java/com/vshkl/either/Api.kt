package com.vshkl.either

import arrow.core.Either
import com.google.gson.annotations.SerializedName
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
        @SerializedName("display_name") val displayName: String,
    )
}

fun SuccessResponseDto.SuccessDto.toDomain(): Profile {
    return Profile(
        id = id,
        username = username,
        displayName = displayName,
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

fun ErrorResponseDto.ErrorDto.toDomain(): Error {
    return Error(
        message = message,
        reasons = reasons,
    )
}
