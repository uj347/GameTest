package com.booba.textures

import org.lwjgl.opengl.GL46.*


data class TextureDescription(
    val src:String,
    val glId:Int,
    val type:TextureType
){

    fun setToUnit(unit:Int){
        glActiveTexture(unit)
        glBindTexture(GL_TEXTURE_2D,glId)
    }
    enum class TextureType{
        TEXTURE,ALPHA_MASK,NORMAL_MAP,DIFFUSE_MAP,SPECULAR_MAP,NOT_DEFINED
    }
}
