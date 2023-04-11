package com.booba.vertexdata

class Object2DVertexData(
      val vertices:List<VertexDescription2D>,
      val vertexEboOrder:List<Int> ,
      val resourcePreparation:()->Boolean,
      val resourceRelease:()->Boolean
 ) {

    val vao:Int by lazy {
        defaultCreateTexturedVao()
    }
     val vertexCount
        get() = vertexEboOrder.size



}
