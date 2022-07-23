package com.upf.memorytrace_android.common

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.*

class MutableStateStackFlow<T>(initialValue: T) : MutableStateFlow<T> {

    private val mutableStateFlow: MutableStateFlow<T> = MutableStateFlow(initialValue)
    private val stateStack: Stack<T> = Stack()

    @InternalCoroutinesApi
    override suspend fun collect(collector: FlowCollector<T>) {
        mutableStateFlow.collect(collector)
    }

    override val replayCache: List<T>
        get() = mutableStateFlow.replayCache

    override var value: T = initialValue
        get() = mutableStateFlow.value
        set(value) {
            stateStack.push(value)
            field = value
        }

    override fun compareAndSet(expect: T, update: T): Boolean {
        return mutableStateFlow.compareAndSet(expect, update)
    }

    override val subscriptionCount: StateFlow<Int>
        get() = mutableStateFlow.subscriptionCount

    override suspend fun emit(value: T) {
        stateStack.push(value)
        mutableStateFlow.emit(value)
    }

    @ExperimentalCoroutinesApi
    override fun resetReplayCache() {
        mutableStateFlow.resetReplayCache()
    }

    override fun tryEmit(value: T): Boolean {
        stateStack.push(value)
        return mutableStateFlow.tryEmit(value)
    }

    fun push(function: (T) -> T) {
        while (true) {
            val prevValue = value
            val nextValue = function(prevValue)
            if (compareAndSet(prevValue, nextValue)) {
                stateStack.push(nextValue)
                return
            }
        }
    }

    fun pop(): T {
        val prevValue = stateStack.pop()
        push { prevValue }
        return prevValue
    }
}