package com.ikhoka.core

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * A convenience wrapper around [repeatOnLifecycle]
 *
 * ```
 * uiStateFlow.collectIn(owner) { state -> }
 * ```
 */
fun <T> Flow<T>.collectIn(
    owner: LifecycleOwner,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    action: suspend (value: T) -> Unit
) = owner.lifecycleScope.launch {
    owner.repeatOnLifecycle(minActiveState) {
        collect { action(it) }
    }
}

/**
 * Shorthand for map(...).distinctUntilChanged()
 */
fun <T, V> Flow<T>.mapDistinct(mapper: suspend (T) -> V): Flow<V> =
    map(mapper).distinctUntilChanged()

/**
 * Use in [ViewModel] to avoid passing 5000
 */
fun <T> Flow<T>.stateIn(
    scope: CoroutineScope,
    initialValue: T
): StateFlow<T> = stateIn(
    scope = scope,
    started = SharingStarted.WhileSubscribed(5000),
    initialValue = initialValue
)

inline fun <reified T> instantCombine(vararg flows: Flow<T>) = channelFlow {
    val array = Array(flows.size) {
        false to (null as T?) // first element stands for "present"
    }

    flows.forEachIndexed { index, flow ->
        launch {
            flow.collect { emittedElement ->
                array[index] = true to emittedElement
//                send(array.filter { it.first }.map { it.second })
                send(array.map { it.second })
            }
        }
    }
}
