package sco.carlukesoftware.composeextensions

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.MutableCreationExtras

/**
 * Provides a [CreationExtras] instance with the [Application] instance.
 *
 * This is useful for creating ViewModels that require an [Application] instance
 * and can be used with [androidx.lifecycle.ViewModelProvider.Factory.create].
 *
 * Example:
 * ```
 * class MyViewModel(application: Application) : ViewModel() {
 *    // ...
 * }
 *
 * class MyViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
 *    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
 *        return MyViewModel(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]!!) as T
 *    }
 * }
 *
 * // In your Composable
 * val viewModel: MyViewModel = viewModel(
 *     factory = MyViewModelFactory(LocalContext.current.applicationContext as Application)
 * )
 * ```
 * Or, more simply using this extension:
 * ```
 * // In your Composable
 * val viewModel: MyViewModel = viewModel(
 *     factory = MyViewModelFactory(LocalContext.current.applicationContext as Application),
 *     extras = (LocalContext.current.applicationContext as Application).creationExtras
 * )
 * ```
 */
val Application.creationExtras: CreationExtras
    get() {
        val extras = MutableCreationExtras()
        extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] = this
        return extras
    }

/**
 * Provides [CreationExtras] from a [Context].
 *
 * This is useful when you need to create a ViewModel with [CreationExtras] but only have a [Context] available.
 * It automatically adds the [Application] to the extras, which is required by [ViewModelProvider.AndroidViewModelFactory].
 */
val Context.creationExtras: CreationExtras
    get() {
        val extras = MutableCreationExtras()
        extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] = this.applicationContext as Application
        return extras
    }
