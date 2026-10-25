package sco.carlukesoftware.composeextensions.lifecycle

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner

/**
 * An interface for observing lifecycle events of a [LifecycleOwner].
 *
 * All methods provide default empty implementations so implementers can selectively
 * override only the callbacks they need.
 */
interface ComposableLifecycleObserver {

    /**
     * Called whenever any lifecycle event occurs.
     *
     * @param lifecycleOwner The [LifecycleOwner] whose state has changed.
     * @param event The specific [Lifecycle.Event] that occurred.
     */
    fun onAny(lifecycleOwner: LifecycleOwner, event: Lifecycle.Event) {}

    /**
     * Called after the [LifecycleOwner]'s `onCreate` event completes.
     *
     * @param lifecycleOwner The [LifecycleOwner] whose state has changed.
     */
    fun onCreate(lifecycleOwner: LifecycleOwner) {}

    /**
     * Called when the [LifecycleOwner] transitions to the STARTED state.
     *
     * @param lifecycleOwner The [LifecycleOwner] whose state has changed.
     */
    fun onStart(lifecycleOwner: LifecycleOwner) {}

    /**
     * Called when the [LifecycleOwner] becomes visible and interactive (`ON_RESUME`).
     *
     * @param lifecycleOwner The [LifecycleOwner] whose state has changed.
     */
    fun onResume(lifecycleOwner: LifecycleOwner) {}

    /**
     * Called when the [LifecycleOwner] is paused (`ON_PAUSE`).
     *
     * @param lifecycleOwner The [LifecycleOwner] whose state has changed.
     */
    fun onPause(lifecycleOwner: LifecycleOwner) {}

    /**
     * Called when the [LifecycleOwner] is no longer visible (`ON_STOP`).
     *
     * @param lifecycleOwner The [LifecycleOwner] whose state has changed.
     */
    fun onStop(lifecycleOwner: LifecycleOwner) {}

    /**
     * Called when the [LifecycleOwner] is being destroyed (`ON_DESTROY`).
     *
     * @param lifecycleOwner The [LifecycleOwner] whose state has changed.
     */
    fun onDestroy(lifecycleOwner: LifecycleOwner) {}
}
