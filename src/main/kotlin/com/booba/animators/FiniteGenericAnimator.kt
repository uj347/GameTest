package com.booba.animators

import getValue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

interface FiniteGenericAnimator <T>:FiniteAnimator<T> {

    override val lastFrameTime: Long?
        get() = underlyingAnimator.lastFrameTime

    override val isLaunched: Boolean
        get() = underlyingAnimator.isLaunched

    override val startTime: Long?
        get() = underlyingAnimator.startTime

    override fun commitTime(newTime: Long):T? {
        val frameResult=underlyingAnimator.commitTime(newTime)
        if (frameResult!=null&&stopPredicate(frameResult,newTime))underlyingAnimator.stop()
        return frameResult
    }

    override fun launch() =underlyingAnimator.launch()

    override fun stop() =underlyingAnimator.stop()
}