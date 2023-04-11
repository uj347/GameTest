package com.booba.vertexdata

import org.lwjgl.opengl.GL46.*
import put
import withMemStack



fun DebugVertexMeshData.createDebugVertexVao():Int{
    val res= withMemStack {
        val fSize=Float.SIZE_BYTES
        val floatCount=2
        val bufSize=vertices.size*(floatCount)
        val buf=mallocFloat(bufSize)
        vertices.forEach { coords->

            buf.put(coords.first,coords.second)

        }

        buf.flip()
        val vao= glGenVertexArrays()
        glBindVertexArray(vao)


        val vbo= glGenBuffers()
        glBindBuffer(GL_ARRAY_BUFFER, vbo)
        glBufferData(GL_ARRAY_BUFFER, buf, GL_STATIC_DRAW)


        glEnableVertexAttribArray(0)
        glVertexAttribPointer(0, 2, GL_FLOAT, false, floatCount * fSize, 0)

        glBindBuffer(GL_ARRAY_BUFFER, 0)
        glBindVertexArray(0)
        vao
    }
    return res
}

 fun Object2DVertexData.defaultCreateTexturedVao():Int{

    val res= withMemStack {
        val fSize=Float.SIZE_BYTES
        val floatCount=3+//PositionData
                        2 //TextureCoords
        val bufSize=vertices.size*(floatCount)
        val buf=mallocFloat(bufSize)
        this@defaultCreateTexturedVao.vertices.forEach { vDescr->

            buf.put(vDescr.x,vDescr.y,1f)

            with(vDescr.textureCoordinates){
                buf.put(first,second)
            }

        }

        buf.flip()
        val vao= glGenVertexArrays()
        glBindVertexArray(vao)

        val ebo= glGenBuffers()
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo)
        val eboData=mallocInt(vertexEboOrder.size)
        vertexEboOrder.forEach { eboData.put(it) }
        eboData.flip()
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, eboData, GL_STATIC_DRAW)

        val vbo= glGenBuffers()
        glBindBuffer(GL_ARRAY_BUFFER, vbo)
        glBufferData(GL_ARRAY_BUFFER, buf, GL_STATIC_DRAW)

        val int1=(0*fSize).toLong()
        glEnableVertexAttribArray(0)
        glVertexAttribPointer(0, 3, GL_FLOAT, false, floatCount * fSize, int1)

        val int2=(3*fSize).toLong()
        glEnableVertexAttribArray(1)

        glVertexAttribPointer(1, 2, GL_FLOAT, false, floatCount * fSize, int2)

        glBindBuffer(GL_ARRAY_BUFFER, 0)
        glBindVertexArray(0)
        vao
    }
    return res

}