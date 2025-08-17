package sco.carlukesoftware.composeextensions

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.widget.Toast
import kotlin.jvm.Throws

/**
 * [Context] extension function
 *
 * Steps back up a context chain till it reaches the last activity
 *
 * @return [Activity] this context belongs to
 *
 * @throws IllegalStateException if this context does not belong to an activity
 *
 */
@Throws(IllegalStateException::class)
fun Context.findActivity(): Activity {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }

    throw IllegalStateException("no activity")
}

/**
 * [Context] extension function
 *
 * Shows a toast message for a short period
 *
 * @param msg message to show
 */
fun Context.showToast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

