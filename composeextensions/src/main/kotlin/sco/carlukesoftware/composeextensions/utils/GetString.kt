package sco.carlukesoftware.composeextensions.utils

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

/**
 * A sealed interface for representing a string that can be retrieved in different ways.
 * This is useful for decoupling UI components from how strings are sourced, whether they
 * come from a remote API (`ApiString`) or from local Android string resources (`StringResource`).
 *
 * It provides methods to resolve the string value both within a Composable context and
 * outside of it (using an Android `Context`).
 *
 * Example Usage:
 * ```
 * // In a ViewModel
 * val title: GetString = GetString.StringResource(R.string.my_title)
 * val subtitle: GetString = GetString.ApiString("Live Data from API")
 *
 * // In a Composable
 * @Composable
 * fun MyScreen(title: GetString, subtitle: GetString) {
 *     Column {
 *         Text(text = title.asString())       // Uses stringResource()
 *         Text(text = subtitle.asString())    // Uses the raw string
 *     }
 * }
 *
 * // Outside of Compose (e.g., in a Service or BroadcastReceiver)
 * fun showNotification(context: Context, title: GetString) {
 *     val notificationText = title.asString(context) // Uses context.getString()
 *     // ... build and show notification
 * }
 * ```
 */
sealed interface GetString {

    /**
     * Represents a string that is obtained from an external source, like a network API.
     * This class wraps a raw [String] value, distinguishing it from a local Android string resource.
     *
     * @property value The literal string content.
     */
    data class ApiString(val value: String): GetString

    /**
     * Represents a string that can be retrieved from Android string resources.
     * This is useful for providing localized strings within the application.
     *
     * @param resourceId The ID of the string resource (e.g., `R.string.my_string`).
     * @param args The optional format arguments to be used for substitution in the string resource.
     */
    class StringResource(@StringRes val resourceId: Int, vararg val args: Any): GetString

    @Composable
    fun asString(): String {
        return when(this) {
            is ApiString -> {
                value
            }

            is StringResource -> {
                stringResource(id = resourceId, *args)
            }
        }

    }

    fun asString(context: Context): String {
        return when(this) {
            is ApiString -> { value }

            is StringResource -> {
                context.getString(resourceId, *args)
            }
        }
    }
}
