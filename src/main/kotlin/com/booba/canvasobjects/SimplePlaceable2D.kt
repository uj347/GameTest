package com.booba.canvasobjects

import com.booba.DimensionF
import com.booba.hitboxes.Hitbox
import com.booba.hitboxes.NeverHitbox
import com.booba.interactable.Interactable
import com.booba.interactable.Interaction
import com.booba.vertexdata.Object2DVertexData
import com.booba.shaders.*
import com.booba.textures.TextureDescription
import dp
import genId
import getValue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import org.joml.Matrix3f
import org.joml.Matrix4f
import org.joml.Vector4f
import org.lwjgl.opengl.GL30.*
import setValue
import transformPointWith


class SimplePlaceable2D(

    override val tags: Collection<String> = emptyList(),
    object2D: Object2DVertexData,
    textureDataLazy:Lazy<List<TextureDescription>>,
    initProgram:ShaderProgramSpec,
    initDimension:DimensionF,
    initCoordinates: DimensionF,
    initRotationDegree:Float,
    initHitboxes: List<Hitbox> = listOf(NeverHitbox)
):PlaceableObject2D (
    object2D, textureDataLazy,initProgram, initDimension,
    initCoordinates,initRotationDegree,initHitboxes
        ){

    override val id: Long=genId()
    private val rotationRad:Float
        get() {
            return Math.toRadians(rotationDegree.toDouble()).toFloat()
        }

    private val worldTransformMatrix:Matrix4f
        get()=Matrix4f()
            .translate(coordinates.first*dp,coordinates.second*dp,0f)
            .rotate(rotationRad,0f,0f,1f)
            .scale(dimensions.first*dp,dimensions.second*dp,0f)
    override fun draw(currentTime:Long) {
       try {
//           val worldTransform=Matrix4f()
//               .translate(coordinates.first*dp,coordinates.second*dp,0f)
//               .scale(dimensions.first*dp,dimensions.second*dp,0f)
//               .rotate(rotationRad,0f,0f,1f)
           val hitTrans=


           shaderProgram.setUniform(WORLD_TRANSFORM_LITERAL,worldTransformMatrix)
            val textData=textureData.value
           textData.sortedBy { if(it.type==TextureDescription.TextureType.ALPHA_MASK)1 else -1 }
           textData[0].setToUnit(GL_TEXTURE0)

           //TODO
//           textData[1].setToUnit(GL_TEXTURE1)

           shaderProgram.setUniform(TEXTURE_2D_TEXTURE,0)
           //TODO
//           shaderProgram.setUniform(ALPHA_MASK_TEXTURE,1)
           if(object2D.resourcePreparation()){
               glBindVertexArray(object2D.vao)
               glDrawElements(GL_TRIANGLES,object2D.vertexCount, GL_UNSIGNED_INT,0)
//              glDrawArrays(GL_TRIANGLES, 0, object2D.vertexCount)
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

    override fun addHitbox(hitbox: Hitbox) =
       hitboxState.update { it+hitbox }
    private val _globalToNdcTransformMatrix:Matrix4f
    get()=  Matrix4f()

        .rotate(-rotationRad,0f,0f,1f)
        .scale(2/dimensions.first,2/dimensions.second,0f)
        .translate(-(coordinates.first),-(coordinates.second),0f)

    override val globalToNdcTransformMatrix: Matrix4f
        get() = _globalToNdcTransformMatrix


    override fun clearHitboxes() =
      hitboxState.update { emptyList() }


    override fun checkHitPoint(coord: DimensionF): Boolean {
        val mappedCoord=coord.transformPointWith(_globalToNdcTransformMatrix)
        return hitboxes.any { it.checkHitPoint(mappedCoord) }
    }

    override fun checkHitPoly(anotherPoly: Array<DimensionF>): Boolean {
        val anotherMapped=anotherPoly.map{it.transformPointWith(_globalToNdcTransformMatrix)}.toTypedArray()
        return hitboxes.any { it.checkHitPoly(anotherMapped) }
    }

}