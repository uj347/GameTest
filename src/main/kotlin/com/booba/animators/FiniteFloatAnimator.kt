package com.booba.animators

import genId

class FiniteFloatAnimator(
    override val underlyingAnimator: Animator<Float>,
    override val stopPredicate: (value: Float, currentTime: Long) -> Boolean,

):FiniteGenericAnimator<Float> {
    override val deltaTimeMapper: (Long) -> Float
        get() = underlyingAnimator.deltaTimeMapper
    override val valueGetter: () -> Float
        get() = underlyingAnimator.valueGetter
    override val valueSetter: (Float) -> Unit
        get() = underlyingAnimator.valueSetter
    override val valueAdder: (old: Float, deltaT: Float) -> Float
        get() = underlyingAnimator.valueAdder
    override val id: Long=genId()
}