package reactivecircus.blueprint.demo.util

import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * Lazily retrieve a viewModel instance associated with the current [ComponentActivity].
 */
inline fun <reified T : ViewModel> ComponentActivity.viewModel(crossinline provider: () -> T) =
    viewModels<T> {
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>) = provider() as T
        }
    }
