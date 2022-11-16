package com.vshkl.either

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ViewModel(
    private val coroutineScope: CoroutineScope,
    private val coroutineDispatcherIO: CoroutineDispatcher,
    private val api: Api,
) {

    private val _state: MutableStateFlow<State> = MutableStateFlow(State.Idle)
    val state: MutableStateFlow<State> = _state

    fun fetchSuccess() {
        fetch(shouldFail = false)
    }

    fun fetchFailure() {
        fetch(shouldFail = true)
    }

    private fun fetch(shouldFail: Boolean) {
        _state.value = State.Loading

        coroutineScope.launch(coroutineDispatcherIO) {
            api
                .run {
                    if (shouldFail)
                        getError() else
                        getSuccess()
                }
                .bimap({ errorResponse: ErrorResponseDto ->
                    _state.value = State.Failure(errorResponse.data.toDomain())
                }, { successResponse: SuccessResponseDto ->
                    _state.value = State.Success(successResponse.data.toDomain())
                })
        }
    }
}

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

sealed class State {
    object Idle : State()
    object Loading : State()
    data class Success(val profile: Profile) : State()
    data class Failure(val error: Error) : State()
}
