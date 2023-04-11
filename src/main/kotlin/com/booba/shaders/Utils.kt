package com.booba.shaders


import getFloatValuesFloatBuf
import org.joml.Matrix4f
import org.joml.Vector3f
import org.lwjgl.opengl.GL30

val matrix44TransposedValueSetter:(newVal:Any,pId:Int,location:Int,name:String)->Unit = {newVal, pId,location, name ->
    val cast=newVal as Matrix4f
    println("Setting new value :${newVal} for $name")
    GL30.glUniformMatrix4fv(location, true, cast.getFloatValuesFloatBuf())
}

val matrix44ValueSetter:(newVal:Any,pId:Int,location:Int,name:String)->Unit = {newVal, pId,location, name ->
    val cast=newVal as Matrix4f
//    println("Setting new value :${newVal} for $name")
    GL30.glUniformMatrix4fv(location, false, cast.getFloatValuesFloatBuf())
}


val vector3fValueSetter:(newVal:Any,pId:Int,location:Int,name:String)->Unit = {newVal, pId,location, name ->
    val cast=newVal as Vector3f
    GL30.glUniform3fv(location,  cast.getFloatValuesFloatBuf())
}

val intSetter:(newVal:Any,pId:Int,location:Int,name:String)->Unit = {newVal, pId,location, name ->
    val cast=newVal as Int
    GL30.glUniform1ui(location,  cast)
}


const val TRANSFORM_LITERAL="transform"
val transformMatrixSpec=ShaderProgramSpec.UniformSpec(name = TRANSFORM_LITERAL, setter = matrix44ValueSetter)

const val WORLD_TRANSFORM_LITERAL="worldTransform"
val worldTransformMatrixSpec=ShaderProgramSpec.UniformSpec(name = WORLD_TRANSFORM_LITERAL, setter = matrix44ValueSetter)

const val CAMERA_TRANSFORM_LITERAL="cameraTransform"
val cameraTransformMatrixSpec=ShaderProgramSpec.UniformSpec(name = CAMERA_TRANSFORM_LITERAL, setter = matrix44ValueSetter)

const val PROJECTION_TRANSFORM_LITERAL="projectionTransform"
val projectionTransformMatrixSpec=ShaderProgramSpec.UniformSpec(name = PROJECTION_TRANSFORM_LITERAL, setter = matrix44ValueSetter)

const val ALPHA_MASK_TEXTURE="alphaMask"
val alphaMaskMatrixSpec=ShaderProgramSpec.UniformSpec(name = ALPHA_MASK_TEXTURE, setter = intSetter)

const val TEXTURE_2D_TEXTURE="texture2D"
val texture2dTextureSpec=ShaderProgramSpec.UniformSpec(name = TEXTURE_2D_TEXTURE, setter = intSetter)


