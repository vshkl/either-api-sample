package com.vshkl.either

import arrow.core.Either
import com.vshkl.either.Models.Dto
import retrofit2.http.GET

/**
 * API interface. Both methods are effectively the same thing, at least that's how it would've been
 * in the real world, but we've split them up here because that's the simplest way to enable the
 * mock server to return different responses: success and failure.
 */
interface Api {

    @GET("success")
    suspend fun getSuccess(): Either<Dto.ErrorResponse, Dto.SuccessResponse>

    @GET("error")
    suspend fun getError(): Either<Dto.ErrorResponse, Dto.SuccessResponse>
}
