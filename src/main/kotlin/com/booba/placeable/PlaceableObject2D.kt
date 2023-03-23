package com.booba.placeable

import com.booba.DimensionF
import com.booba.animators.Animator
import com.booba.animators.AnimatorHolder
import com.booba.animators.AnimatorTarget
import com.booba.canvas.Canvas
import com.booba.interactable.Interactable
import com.booba.objects.Object2D
import com.booba.shaders.ShaderProgramSpec
import getValue
import kotlinx.coroutines.flow.MutableStateFlow
import setValue


abstract class PlaceableObject2D (
    object2D: Object2D,
    initProgram:ShaderProgramSpec,
    initDimension:DimensionF,
    initCoordinates: DimensionF,
    initRotationDegree:Float
        ):Placeable,Interactable,AnimatorTarget {

    val shaderProgramState: MutableStateFlow<ShaderProgramSpec> = MutableStateFlow(initProgram)
    var shaderProgram: ShaderProgramSpec by shaderProgramState

    val object2DState: MutableStateFlow<Object2D> = MutableStateFlow(object2D)
    var object2D: Object2D by object2DState


    override val rotationDegreeState: MutableStateFlow<Float> = MutableStateFlow(initRotationDegree)
    override var rotationDegree:Float by rotationDegreeState
    override val coordinatesState: MutableStateFlow<DimensionF> = MutableStateFlow(initCoordinates)
    override var coordinates:DimensionF by coordinatesState
    override val dimensionsState: MutableStateFlow<DimensionF> = MutableStateFlow(initDimension)
    override var dimensions:DimensionF by dimensionsState

    abstract fun draw(currentTime:Long)

    override fun placeOn(canvas: Canvas) {
        canvas.addPlaceable(this)
    }

    override fun removeFrom(canvas:Canvas) {
        canvas.removePlaceable(this)
    }

    override fun addAnimator(animator: Animator<*>, holder: AnimatorHolder)=holder.addAnimator(this,animator)

    override fun stopAnimators(holder: AnimatorHolder) {
        holder.animationsRegistry[this.id]?.forEach { it.stop() }
    }

    override fun launchAnimators(holder: AnimatorHolder) {
        holder.animationsRegistry[this.id]?.forEach { it.launch() }
    }

    override fun removeAnimators(holder: AnimatorHolder) {
        holder.removeAnimatorsForTarget(this.id)
    }
}
