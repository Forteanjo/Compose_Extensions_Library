package sco.carlukesoftware.composeextensions

import androidx.compose.runtime.Immutable

/**
 * A generic sealed interface representing the different states of an asynchronous operation or data loading process.
 * Annotated with [@Immutable][Immutable] for Jetpack Compose stability and optimal recomposition behavior.
 *
 * @param T The type of data held by the [Success] state.
 */
@Immutable
sealed interface UiState<out T> {

    /** Indicates the initial state before any operation has started. */
    data object Idle : UiState<Nothing>

    /** Indicates that an operation is currently in progress. */
    data object Loading : UiState<Nothing>

    /**
     * Indicates successful completion holding the resulting [data].
     *
     * @param data The payload produced by the successful operation.
     */
    data class Success<out T>(val data: T) : UiState<T>

    /**
     * Indicates an operation failure holding the thrown [throwable].
     *
     * @param throwable The exception or error that occurred.
     */
    data class Failure(val throwable: Throwable) : UiState<Nothing>

    // Convenience properties
    val isIdle: Boolean get() = this is Idle
    val isLoading: Boolean get() = this is Loading
    val isSuccess: Boolean get() = this is Success
    val isFailure: Boolean get() = this is Failure
}

/**
 * Returns the encapsulated data if this state is [UiState.Success], or `null` otherwise.
 */
fun <T> UiState<T>.getOrNull(): T? = (this as? UiState.Success)?.data

/**
 * Returns the encapsulated throwable if this state is [UiState.Failure], or `null` otherwise.
 */
fun <T> UiState<T>.exceptionOrNull(): Throwable? = (this as? UiState.Failure)?.throwable

/**
 * Returns the encapsulated data if this state is [UiState.Success], or [default] otherwise.
 */
fun <T> UiState<T>.getOrDefault(default: T): T = (this as? UiState.Success)?.data ?: default

/**
 * Returns the encapsulated data if this state is [UiState.Success], or the result of [onFailure] function
 * for non-success states.
 */
inline fun <T> UiState<T>.getOrElse(onFailure: (throwable: Throwable?) -> T): T = when (this) {
    is UiState.Success -> data
    is UiState.Failure -> onFailure(throwable)
    else -> onFailure(null)
}

/**
 * Returns the encapsulated data if this state is [UiState.Success], or throws the encapsulated [Throwable]
 * if this state is [UiState.Failure], or an [IllegalStateException] if [UiState.Idle] or [UiState.Loading].
 */
fun <T> UiState<T>.getOrThrow(): T = when (this) {
    is UiState.Success -> data
    is UiState.Failure -> throw throwable
    is UiState.Idle -> error("State is Idle: no data available")
    is UiState.Loading -> error("State is Loading: data is not yet available")
}

/**
 * Transforms the [UiState.Success] data using the given [transform] function.
 * Non-success states ([UiState.Idle], [UiState.Loading], [UiState.Failure]) are passed through unchanged.
 */
inline fun <T, R> UiState<T>.map(transform: (T) -> R): UiState<R> = when (this) {
    is UiState.Success -> UiState.Success(transform(data))
    is UiState.Failure -> this
    is UiState.Loading -> this
    is UiState.Idle -> this
}

/**
 * Transforms the [UiState.Success] data using the given [transform] function that returns a new [UiState].
 */
inline fun <T, R> UiState<T>.flatMap(transform: (T) -> UiState<R>): UiState<R> = when (this) {
    is UiState.Success -> transform(data)
    is UiState.Failure -> this
    is UiState.Loading -> this
    is UiState.Idle -> this
}

/**
 * Applies [onSuccess] if state is [UiState.Success], [onFailure] if [UiState.Failure],
 * [onLoading] if [UiState.Loading], or [onIdle] if [UiState.Idle].
 */
inline fun <T, R> UiState<T>.fold(
    onSuccess: (data: T) -> R,
    onFailure: (throwable: Throwable) -> R,
    onLoading: () -> R,
    onIdle: () -> R,
): R = when (this) {
    is UiState.Idle -> onIdle()
    is UiState.Loading -> onLoading()
    is UiState.Failure -> onFailure(throwable)
    is UiState.Success -> onSuccess(data)
}
