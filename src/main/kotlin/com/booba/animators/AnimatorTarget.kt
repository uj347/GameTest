package com.booba.animators

import com.booba.canvas.Canvas

interface AnimatorTarget {
    val id:Long
    fun addAnimator(animator:Animator<*>,holder: AnimatorHolder)
    fun stopAnimators(holder: AnimatorHolder)
    fun launchAnimators(holder: AnimatorHolder)
    fun removeAnimators(holder: AnimatorHolder)
}