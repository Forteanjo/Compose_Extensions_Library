package sco.carlukesoftware.composeextensions.composables

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.getSystemService

/**
 * A Composable function that retrieves a system service of type [T].
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
inline fun <reified T : Any> getRequiredService(): T {
    val context = LocalContext.current
    return requireNotNull(context.getSystemService()) as T
}
