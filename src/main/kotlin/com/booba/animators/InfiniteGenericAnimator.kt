package com.booba.animators

import getValue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

abstract  class InfiniteGenericAnimator<T>:InfiniteAnimator<T> {
    private val _lastFrameTime= MutableStateFlow<Long?>(null)
    override val lastFrameTime: Long? by _lastFrameTime
    private val _isLaunched: MutableStateFlow<Boolean> = MutableStateFlow(false)
    override val isLaunched: Boolean by _isLaunched
    private val _startTime:MutableStateFlow<Long?> = MutableStateFlow(null)
    override val startTime: Long? by _startTime

    override fun commitTime(newTime: Long):T? {
        val frameVal=if(isLaunched){
            if(lastFrameTime==null){
                _startTime.update { newTime }
                _lastFrameTime.update { newTime }
                null
            }else {
                val deltaTime = newTime - lastFrameTime!!
                val valueDelta = deltaTimeMapper(deltaTime)
                val newValue = valueAdder(valueGetter(), valueDelta)
                valueSetter(newValue)
                _lastFrameTime.update { newTime }
                newValue
            }
        }else null
        return frameVal
    }

    override fun launch() =_isLaunched.update { true }

    override fun stop() {
        _isLaunched.update { false }
        _lastFrameTime.update { null }
    }
}