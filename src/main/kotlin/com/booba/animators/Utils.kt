package com.booba.animators

import com.sun.org.apache.xpath.internal.operations.Bool

fun produceLinearDeltaTimeFloatMapper(unitPerSec:Float)={ deltaTime:Long ->
        (deltaTime * 0.001f) * unitPerSec
    }

val simpleFloatSummator:(old: Float, deltaT: Float) -> Float= { old, deltaT -> old+deltaT }

fun produceTimerPredicate(stopTimerMillis:Long):(Any?,Long)->Boolean {
    val finish=System.currentTimeMillis()+stopTimerMillis
    return{ _: Any?, time: Long ->
        time >= finish
    }
}