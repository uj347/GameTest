package com.booba.vertexdata

import com.booba.DimensionF

class DebugVertexMeshData(
    val vertices:Array<DimensionF>,

) {
    val vao:Int by lazy {
        createDebugVertexVao()
    }

}