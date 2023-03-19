package com.booba.animators

interface InfiniteAnimator<T> {
    val deltaTimeMapper:(Long)->T
    val valueAdder:(old:T,deltaT:T)->T
    val valueGetter:()->T
    val valueSetter:(T)->Unit
    val isLaunched:Boolean
    val lastFrameTime:Long?
    fun commitTime(newTime:Long)
    fun launch()
    fun stop()
}