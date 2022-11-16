package com.vshkl.either

import arrow.core.Either
import com.vshkl.either.Models.Dto
import retrofit2.http.GET

interface Api {

    @GET("success")
    suspend fun getSuccess(): Either<Dto.ErrorResponse, Dto.SuccessResponse>

    @GET("error")
    suspend fun getError(): Either<Dto.ErrorResponse, Dto.SuccessResponse>
}
