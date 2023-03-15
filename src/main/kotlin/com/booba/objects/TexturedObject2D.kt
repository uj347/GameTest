package com.booba.objects

import com.booba.DimensionF
import java.awt.Color

class TexturedObject2D(
    private val vertexMapping:List<Pair<DimensionF, DimensionF>>):Object2D() {
    override val vertices: List<VertexDescription2D> = vertexMapping.map{
        VertexDescription2D(it.first,Texture2D(it.second))
    }

    override val textureType2D: TextureType2d=TextureType2d.TEXTURED

    override fun releaseResources() {
        TODO("Not yet implemented")
    }
}