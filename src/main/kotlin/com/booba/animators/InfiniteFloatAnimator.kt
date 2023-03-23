package com.booba.animators

import genId


class InfiniteFloatAnimator(
    override val valueGetter: () -> Float,
    override val valueSetter: (Float) -> Unit,
    override val deltaTimeMapper: (Long) -> Float,
    override val valueAdder: (old: Float, deltaT: Float) -> Float = simpleFloatSummator

):InfiniteGenericAnimator<Float> (){
    override val id: Long=genId()
}