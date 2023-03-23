package com.booba.animators

import java.util.concurrent.atomic.AtomicLong
import java.util.concurrent.atomic.AtomicLongArray

interface Animator<T> {
    val id:Long
    val deltaTimeMapper:(Long)->T
    val valueAdder:(old:T,deltaT:T)->T
    val valueGetter:()->T
    val valueSetter:(T)->Unit
    val isLaunched:Boolean
    val startTime:Long?
    val lastFrameTime:Long?
    fun commitTime(newTime:Long):T?
    fun launch()
    fun stop()

}