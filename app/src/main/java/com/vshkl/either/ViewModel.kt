package com.vshkl.either

import com.vshkl.either.Models.Domain
import com.vshkl.either.Models.Dto
import com.vshkl.either.Models.Dto.toDomain
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
            _state.value = api
                .run {
                    if (shouldFail)
                        getError() else
                        getSuccess()
                }
                .fold({ errorResponse: Dto.ErrorResponse ->
                    State.Failure(errorResponse.data.toDomain())
                }, { successResponse: Dto.SuccessResponse ->
                    State.Success(successResponse.data.toDomain())
                })
        }
    }
}

/**
 * State of the async operation.
 */
sealed class State {
    object Idle : State()
    object Loading : State()
    data class Success(val profile: Domain.Profile) : State()
    data class Failure(val error: Domain.Error) : State()
}
