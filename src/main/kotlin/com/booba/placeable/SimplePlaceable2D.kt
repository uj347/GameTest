package com.booba.placeable

import com.booba.DimensionF
import com.booba.animators.Animator
import com.booba.canvas.Canvas
import com.booba.interactable.Interactable
import com.booba.interactable.Interaction
import com.booba.objects.Object2D
import com.booba.shaders.ShaderProgramSpec
import com.booba.shaders.WORLD_TRANSFORM_LITERAL
import dp
import genId
import getValue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.update
import org.joml.Matrix4f
import org.lwjgl.opengl.GL30.*
import setValue


class SimplePlaceable2D(

    override val tags: Collection<String> = emptyList(),
    object2D: Object2D,
    initProgram:ShaderProgramSpec,
    initDimension:DimensionF,
    initCoordinates: DimensionF,
    initRotationDegree:Float
):PlaceableObject2D (
    object2D, initProgram, initDimension, initCoordinates,initRotationDegree
        ){


    override val id: Long=genId()
    private val rotationRad:Float
        get() {
            return Math.toRadians(rotationDegree.toDouble()).toFloat()
        }

    override fun draw(currentTime:Long) {
       try {
           val worldTransform=Matrix4f()
               .translate(coordinates.first*dp,coordinates.second*dp,0f)
               .scale(dimensions.first*dp,dimensions.second*dp,0f)
               .rotate(rotationRad,0f,0f,1f)

           shaderProgram.setUniform(WORLD_TRANSFORM_LITERAL,worldTransform)
           if(object2D.resourcePreparation()){
               glBindVertexArray(object2D.vao)
              glDrawArrays(GL_TRIANGLES, 0, object2D.vertexCount)
           }else{
               println("Resources not prepared!!!!!")
           }
       }finally {
           object2D.resourceRelease()
       }
    }

    private val _interactableState= MutableStateFlow(false)
    override var isInteractable: Boolean by _interactableState


    private val _interactionRegistry=MutableStateFlow<Set<Interaction>>(emptySet())
    override val interactionRegistry: Set<Interaction> by _interactionRegistry

    override fun addInteraction(interaction: Interaction) {
        _interactionRegistry.update {
            it+interaction
        }
    }

    override fun removeInteraction(name: String) {
        _interactionRegistry.update {interactionSet->
            interactionSet.firstOrNull { it.name==name }?.let{inter->
                interactionSet-inter
            }?:interactionSet
        }
    }

    override fun interact(anothers: Collection<Interactable>) {
       if(isInteractable){
           _interactionRegistry.value
               .filter { it.launchPredicate(anothers,this) }
               .forEach { it.block(anothers,this) }
       }
    }
}