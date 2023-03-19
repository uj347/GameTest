package com.booba.placeableobject

import com.booba.DimensionF
import com.booba.objects.Object2D
import com.booba.shaders.ShaderProgramSpec
import com.booba.shaders.WORLD_TRANSFORM_LITERAL
import dp
import kotlinx.coroutines.flow.MutableStateFlow
import org.joml.Matrix4f
import org.lwjgl.opengl.GL30.*


class SimplePlaceable2D(
    object2D: Object2D,
    initProgram:ShaderProgramSpec,
    initDimension:DimensionF,
    initCoordinates: DimensionF,
    initRotationDegree:Float
):PlaceableObject2D (
    object2D, initProgram, initDimension, initCoordinates,initRotationDegree
        ){
    private val rotationRad get() = Math.toRadians(rotationDegree.toDouble()).toFloat()
    override fun draw() {
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
}