package sco.carlukesoftware.composeextensions.composables

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.getSystemService

/**
 * Remembers and retrieves a required Android system service of type [T].
 *
 * Caches the lookup across recompositions via [remember] for optimal performance.
 *
 * Example usage:
 * ```kotlin
 * val windowManager = rememberRequiredService<WindowManager>()
 * ```
 *
 * @return The requested non-null system service of type [T].
 * @throws IllegalArgumentException if the service is unavailable on the device.
 */
@Composable
inline fun <reified T : Any> rememberRequiredService(): T {
    val context = LocalContext.current
    return remember(context) {
        requireNotNull(context.getSystemService<T>()) {
            "System service ${T::class.java.simpleName} is not available."
        }
    }
}

/**
 * Remembers and retrieves an optional Android system service of type [T], returning `null`
 * if the service is unavailable on the device.
 *
 * Example usage:
 * ```kotlin
 * val vibratorManager = rememberSystemServiceOrNull<VibratorManager>()
 * ```
 *
 * @return The requested system service of type [T], or `null` if unavailable.
 */
@Composable
inline fun <reified T : Any> rememberSystemServiceOrNull(): T? {
    val context = LocalContext.current
    return remember(context) {
        context.getSystemService<T>()
    }
}

/**
 * A Composable function that retrieves a system service of type [T].
 *
 *  @see rememberRequiredService
 *
 * This function uses [LocalContext] to get the current Android [Context] and then
 * calls [getSystemService] to retrieve the desired service.
 * It uses `requireNotNull` to ensure that the service is available, throwing an
 * [IllegalArgumentException] if the service is not found.
 *
 * This is useful for accessing Android system services like `WindowManager`,
 * `ConnectivityManager`, etc., directly within your Composable functions.
 *
 * The service type [T] is reified, so you can call it with a specific service class,
 * for example: `val windowManager = getRequiredService<WindowManager>()`.
 *
 * @return The requested system service of type [T].
 * @throws IllegalArgumentException if the requested service is not available.
 */

@Composable
inline fun <reified T : Any> getRequiredService(): T = rememberRequiredService<T>()
