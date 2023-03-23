package com.booba.canvas.testcanvas

import com.booba.animators.Animator
import com.booba.animators.AnimatorTarget

internal class AnimatorRegistry:com.booba.animators.AnimatorHolder {
    val _animationRegistry= mutableMapOf<Long, Collection<Animator<*>>>()


    override fun commitTime(timeMillis: Long) {
        _animationRegistry.flatMap { it.value }.forEach { it.commitTime(timeMillis) }
    }

    override val animationsRegistry: Map<Long, Collection<Animator<*>>>
        get() = _animationRegistry

    override fun addAnimator(animatorTarget: AnimatorTarget, animator: Animator<*>) {
        val old=_animationRegistry[animatorTarget.id]?: emptySet()
        _animationRegistry[animatorTarget.id]=old+animator
    }

    override fun removeAnimator(animatorId: Long) {
        val targetEntry=_animationRegistry.entries.firstOrNull{it.value.any { it.id==animatorId }}
        targetEntry?.let{
           val res= it.value-it.value.filter { it.id==animatorId }
            _animationRegistry[targetEntry.key]=res
        }
    }

    override fun removeAnimatorsForTarget(targetId: Long) {
        _animationRegistry.remove(targetId)
    }

    override fun stopAnimator(animatorId: Long) {
        _animationRegistry.flatMap { it.value }.firstOrNull { it.id==animatorId }?.stop()
    }

    override fun launchAnimators(animatorId: Long) {
        _animationRegistry.flatMap { it.value }.firstOrNull { it.id==animatorId }?.launch()
    }

    override fun stopAllAnimators() {
        _animationRegistry.flatMap { it.value }.forEach { it.stop() }
    }

    override fun launchAllAnimators() {
        _animationRegistry.flatMap { it.value }.forEach { it.launch() }
    }

    override fun removeAllAnimators() {
        _animationRegistry.clear()
    }
}