package com.booba.canvasobjects

import com.booba.DimensionF
import com.booba.animators.Animator
import com.booba.animators.AnimatorHolder
import com.booba.animators.AnimatorTarget
import com.booba.canvas.Canvas
import com.booba.hitboxes.Hitbox
import com.booba.hitboxes.HitboxHolder
import com.booba.interactable.Interactable
import com.booba.vertexdata.Object2DVertexData
import com.booba.shaders.ShaderProgramSpec
import com.booba.textures.TextureDescription
import getValue
import kotlinx.coroutines.flow.MutableStateFlow
import setValue


abstract class PlaceableObject2D (
    initObject2D: Object2DVertexData,
    initTextureData:Lazy<List<TextureDescription>>,
    initProgram:ShaderProgramSpec,
    initDimension:DimensionF,
    initCoordinates: DimensionF,
    initRotationDegree:Float,
    initHitboxes: List<Hitbox>
        ):Placeable,Interactable,Drawable,AnimatorTarget,HitboxHolder {

    val textureDataState: MutableStateFlow<Lazy<List<TextureDescription>>> = MutableStateFlow(initTextureData)
    var textureData: Lazy<List<TextureDescription>> by textureDataState



    val object2DState: MutableStateFlow<Object2DVertexData> = MutableStateFlow(initObject2D)
    var object2D: Object2DVertexData by object2DState

    val hitboxState: MutableStateFlow<List<Hitbox>> = MutableStateFlow(initHitboxes)
    override val hitboxes: List<Hitbox> by hitboxState

    override val shaderProgramState: MutableStateFlow<ShaderProgramSpec> = MutableStateFlow(initProgram)
    override var shaderProgram: ShaderProgramSpec by shaderProgramState
    override val rotationDegreeState: MutableStateFlow<Float> = MutableStateFlow(initRotationDegree)
    override var rotationDegree:Float by rotationDegreeState
    override val coordinatesState: MutableStateFlow<DimensionF> = MutableStateFlow(initCoordinates)
    override var coordinates:DimensionF by coordinatesState
    override val dimensionsState: MutableStateFlow<DimensionF> = MutableStateFlow(initDimension)
    override var dimensions:DimensionF by dimensionsState



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
