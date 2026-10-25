package sco.carlukesoftware.composeextensions.utils

import android.content.Context
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource

/**
 * A sealed interface representing UI text that can be resolved dynamically in Compose
 * or using an Android [Context].
 */
@Immutable
sealed interface UiText {

    /** Represents an empty string. */
    @Immutable
    data object Empty : UiText

    /** Represents a raw or dynamic string (e.g., from an API, database, or user input). */
    @Immutable
    data class Dynamic(val value: String) : UiText

    /** Represents an Android string resource ID with optional format arguments. */
    @Immutable
    class Resource(
        @StringRes val resourceId: Int,
        vararg val args: Any
    ) : UiText {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Resource) return false
            if (resourceId != other.resourceId) return false
            return args.contentEquals(other.args)
        }

        override fun hashCode(): Int {
            var result = resourceId
            result = 31 * result + args.contentHashCode()
            return result
        }
    }

    /** Represents an Android plural string resource ID with quantity and format arguments. */
    @Immutable
    class PluralResource(
        @PluralsRes val resourceId: Int,
        val count: Int,
        vararg val args: Any
    ) : UiText {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is PluralResource) return false
            if (resourceId != other.resourceId) return false
            if (count != other.count) return false
            return args.contentEquals(other.args)
        }

        override fun hashCode(): Int {
            var result = resourceId
            result = 31 * result + count
            result = 31 * result + args.contentHashCode()
            return result
        }
    }

    /** Resolves the string value within a Composable context. */
    @Composable
    fun asString(): String = when (this) {
        is Empty -> ""
        is Dynamic -> value
        is Resource -> stringResource(id = resourceId, *args)
        is PluralResource -> pluralStringResource(id = resourceId, count = count, *args)
    }

    /** Resolves the string value using an Android [Context]. */
    fun asString(context: Context): String = when (this) {
        is Empty -> ""
        is Dynamic -> value
        is Resource -> context.getString(resourceId, *args)
        is PluralResource -> context.resources.getQuantityString(resourceId, count, *args)
    }
}
