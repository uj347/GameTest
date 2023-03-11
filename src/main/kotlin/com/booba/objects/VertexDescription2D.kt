package com.booba.objects

import com.booba.DimensionF

data class VertexDescription2D (
    val x:Float,val y:Float,
    val vertexTextureData: VertexTextureData2D
        ){
    constructor(dimen:DimensionF, vertexTextureData: VertexTextureData2D):this(dimen.first,dimen.second,vertexTextureData)
}