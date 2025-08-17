package sco.carlukesoftware.composeextensions.lifecycle

import androidx.lifecycle.LifecycleOwner

/**
 * An interface for observing lifecycle events of a [LifecycleOwner].
 *
 * Implement this interface to receive notifications about specific lifecycle events.
 * Each method corresponds to a distinct lifecycle event.
 */
interface ILifecycleEventObserver {

    /**
     * Called when any lifecycle event occurs.
     *
     * @param lifecycleOwner The [LifecycleOwner] whose state has changed.
     */
    fun onAny(lifecycleOwner: LifecycleOwner)

    /**
     * Notifies that `ON_CREATE` event occurred.
     * <p>
     * This method will be called after the [LifecycleOwner]'s `onCreate`
     * method returns.
     *
     * @param lifecycleOwner the component, whose state was changed
     */
    fun onCreate(lifecycleOwner: LifecycleOwner)

    /**
     * Called when the LifecycleOwner is being destroyed.
     * This method is invoked once for a given LifecycleOwner.
     * After this method is called, the LifecycleOwner is considered destroyed
     * and will not receive any more lifecycle events.
     *
     * @param lifecycleOwner The LifecycleOwner whose lifecycle is being observed.
     */
    fun onDestroy(lifecycleOwner: LifecycleOwner)

    /**
     * Notifies that the ON_PAUSE event occurred.
     * <p>
     * This method will be called before the [LifecycleOwner]'s [LifecycleOwner.onPause] method
     * is called.
     *
     * @param lifecycleOwner the component, whose state was changed
     */
    fun onPause(lifecycleOwner: LifecycleOwner)

    /**
     * Called when the lifecycle event `ON_RESUME` is received.
     * This typically happens when the associated UI becomes visible and interactive again
     * after being paused.
     *
     * @param lifecycleOwner The [LifecycleOwner] whose lifecycle event is being observed.
     */
    fun onResume(lifecycleOwner: LifecycleOwner)

    /**
     * Called when the [LifecycleOwner] transitions to the STARTED state.
     *
     * This method is invoked when the component associated with the lifecycle becomes visible
     * to the user.
     *
     * @param lifecycleOwner The [LifecycleOwner] whose state has changed.
     */
    fun onStart(lifecycleOwner: LifecycleOwner)

    /**
     * Called when the LifecycleOwner's state transitions to `ON_STOP`.
     * This typically happens when the associated UI component is no longer visible to the user.
     *
     * @param lifecycleOwner The LifecycleOwner whose state has changed.
     */
    fun onStop(lifecycleOwner: LifecycleOwner)

}
