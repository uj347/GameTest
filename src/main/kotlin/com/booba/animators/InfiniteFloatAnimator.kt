package com.booba.animators


class InfiniteFloatAnimator(
    private val speedUnitPerSec:Float,
    override val valueGetter: () -> Float,
    override val valueSetter: (Float) -> Unit
):InfiniteGenericAnimator<Float> (){
    override val deltaTimeMapper: (Long) -> Float={deltaTime->
        (deltaTime*0.001f)*speedUnitPerSec
    }
    override val valueAdder: (old: Float, deltaT: Float) -> Float=
        {old, deltaT -> old+deltaT }


}