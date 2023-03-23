package com.booba.animators

interface FiniteAnimator <T>:Animator<T> {
    val stopPredicate:(value:T,currentTime:Long)->Boolean
    val underlyingAnimator:Animator<T>

}