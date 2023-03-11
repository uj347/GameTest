package com.booba.objects

import com.booba.DimensionF
import java.awt.Color

sealed interface VertexTextureData2D
data class Texture2D(
    val textureCoordinates:DimensionF
):VertexTextureData2D
data class SimpleColor2D(
    val color:Color
):VertexTextureData2D

