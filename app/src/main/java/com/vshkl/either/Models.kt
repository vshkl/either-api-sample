package com.vshkl.either

import com.google.gson.annotations.SerializedName

object Models {

    object Dto {

        data class SuccessResponse(
            val data: Success,
        ) {

            data class Success(
                val id: Int,
                val username: String,
                @SerializedName("display_name") val displayName: String,
            )
        }

        fun SuccessResponse.Success.toDomain(): Domain.Profile {
            return Domain.Profile(
                id = id,
                username = username,
                displayName = displayName,
            )
        }

        data class ErrorResponse(
            val data: Error,
        ) {

            data class Error(
                val message: String,
                val reasons: List<String>,
            )
        }

        fun ErrorResponse.Error.toDomain(): Domain.Error {
            return Domain.Error(
                message = message,
                reasons = reasons,
            )
        }
    }

    object Domain {

        data class Profile(
            val id: Int,
            val username: String,
            val displayName: String,
        ) {

            fun pretty(): String {
                return "Profile(" +
                        "\n    id = $id" +
                        "\n    username = $username " +
                        "\n    displayName = $displayName" +
                        "\n)"
            }
        }

        data class Error(
            val message: String,
            val reasons: List<String>,
        ) {

            fun pretty(): String {
                return "Error(" +
                        "\n    message = $message" +
                        "\n    reasons = $reasons" +
                        "\n)"
            }
        }
    }
}
