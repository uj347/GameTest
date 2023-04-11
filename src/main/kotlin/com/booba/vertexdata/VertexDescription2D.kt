package com.booba.vertexdata

import com.booba.DimensionF

data class VertexDescription2D (
    val x:Float,val y:Float,
    val textureCoordinates: DimensionF
        ){
    constructor(dimen:DimensionF, textureCoordinates: DimensionF):this(dimen.first,dimen.second,textureCoordinates)
}