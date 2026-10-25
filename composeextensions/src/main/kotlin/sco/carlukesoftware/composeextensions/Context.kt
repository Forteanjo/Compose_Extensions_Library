package sco.carlukesoftware.composeextensions

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.widget.Toast
import androidx.annotation.StringRes

/**
 * Traverses the [ContextWrapper] chain to find the underlying [Activity].
 *
 * @return The [Activity] this context belongs to, or `null` if no activity is found in the chain
 * (e.g., when called from ApplicationContext, Service, or Compose Previews).
 */
fun Context.findActivity(): Activity? = generateSequence(this) {
    (it as? ContextWrapper)?.baseContext
}.filterIsInstance<Activity>().firstOrNull()

/**
 * Traverses the [ContextWrapper] chain to find a specific subtype of [Activity] [T].
 *
 * Example usage: `context.findActivity<ComponentActivity>()`
 *
 * @return The [Activity] of type [T], or `null` if not found.
 */
inline fun <reified T : Activity> Context.findActivityOf(): T? = generateSequence(this) {
    (it as? ContextWrapper)?.baseContext
}.filterIsInstance<T>().firstOrNull()

/**
 * Traverses the [ContextWrapper] chain to find the underlying [Activity].
 *
 * @return The [Activity] this context belongs to.
 * @throws IllegalStateException if this context does not belong to an [Activity].
 */
fun Context.requireActivity(): Activity =
    findActivity() ?: throw IllegalStateException("Context $this does not contain an Activity.")

/**
 * Displays a toast message.
 *
 * @param msg The text message to show.
 * @param duration Duration of the toast: [Toast.LENGTH_SHORT] or [Toast.LENGTH_LONG].
 */
fun Context.showToast(msg: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, msg, duration).show()
}

/**
 * Displays a toast message using a string resource ID.
 *
 * @param msgRes String resource ID to show.
 * @param duration Duration of the toast: [Toast.LENGTH_SHORT] or [Toast.LENGTH_LONG].
 */
fun Context.showToast(@StringRes msgRes: Int, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, msgRes, duration).show()
}
