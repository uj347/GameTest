package com.booba.animators

interface AnimatorHolder {

    fun commitTime(timeMillis:Long)
    val animationsRegistry:Map<Long,Collection<Animator<*>>>
    fun addAnimator(animatorTarget: AnimatorTarget,animator:Animator<*>)
    fun removeAnimator(animatorId:Long)
    fun removeAnimatorsForTarget(targetId:Long)

    fun stopAnimator(animatorId:Long)
    fun launchAnimators(animatorId:Long)

    fun stopAllAnimators()
    fun launchAllAnimators()
    fun removeAllAnimators()
}