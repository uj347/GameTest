package com.booba.objects

import com.booba.DimensionF
import com.booba.objects.Object2D.Companion.createVao
import java.awt.Color

class ColoredObject2D(
   private val vertexMapping:List<Pair<DimensionF,Color>>
):Object2D() {
    override val vertices: List<VertexDescription2D> = vertexMapping.map{
        VertexDescription2D(it.first,SimpleColor2D(it.second))
    }


    override val textureType2D: TextureType2d = TextureType2d.SIMPLE_COLOR

    override val resourcePreparation: () -> Boolean={true}
    override val resourceRelease: () -> Boolean={true}
}