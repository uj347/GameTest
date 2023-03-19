package com.booba.animators

import getValue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

abstract  class InfiniteGenericAnimator<T>:InfiniteAnimator<T> {

//    override val valueAdder: (old: T, deltaT: T) -> T


    private val _lastFrameTime= MutableStateFlow<Long?>(null)
    override val lastFrameTime: Long? by _lastFrameTime
    private val _isLaunched: MutableStateFlow<Boolean> = MutableStateFlow(false)
    override val isLaunched: Boolean by _isLaunched

    override fun commitTime(newTime: Long) {
        if(isLaunched){
            if(lastFrameTime==null){
                _lastFrameTime.update { newTime }
            }else {
                val deltaTime = newTime - lastFrameTime!!
                val valueDelta = deltaTimeMapper(deltaTime)
                val newValue = valueAdder(valueGetter(), valueDelta)
                valueSetter(newValue)
                _lastFrameTime.update { newTime }
            }
        }
    }

    override fun launch() =_isLaunched.update { true }

    override fun stop() {
        _isLaunched.update { false }
        _lastFrameTime.update { null }
    }
}