package sco.carlukesoftware.composeextensions.utils

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

sealed interface GetString {

    data class ApiString(val value: String): GetString

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

