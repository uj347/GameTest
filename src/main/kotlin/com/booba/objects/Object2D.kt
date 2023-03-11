package com.booba.objects

import com.booba.MemStack
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL15

import org.lwjgl.opengl.GL40.*
import org.lwjgl.system.MemoryUtil
import put
import withMemStack

sealed interface Object2D {
    val vertices:List<VertexDescription2D>
    val vao:Int
    val textureType2D:TextureType2d
    fun releaseResources()
    companion object{
        internal fun Object2D.createVao():Int{

//            val vaoId=glGenVertexArrays()
//            glBindVertexArray(vaoId)
            val res= withMemStack {
               val floatCount=3+4+2
               val bufSize=vertices.size*(floatCount)
               val buf=mallocFloat(bufSize)
               when(textureType2D){
                   TextureType2d.SIMPLE_COLOR->{
                       vertices.forEach { vDescr->
                           val color=(vDescr.vertexTextureData as SimpleColor2D).color
                           val arr=FloatArray(4)
                           val (r,g,b,a)=color.getColorComponents(arr)
                           buf.put(vDescr.x,vDescr.y,0f)
                           buf.put(r,g,b,a)
                           buf.put(0f,0f)
                       }
                   }
                   TextureType2d.TEXTURED-> TODO()
               }
               //TODO
               val vao= glGenVertexArrays()
               glBindVertexArray(vao)
               val vbo=glGenBuffers()
               glBindBuffer(GL_ARRAY_BUFFER,vbo)
               glBufferData(GL_ARRAY_BUFFER,buf, GL_DYNAMIC_DRAW)

              val int1=mallocInt(1)
              int1.put(0)
               glVertexAttribPointer(0,3, GL_FLOAT,false,floatCount*Float.SIZE_BYTES,int1)
               glEnableVertexAttribArray(0)

              val int2=mallocInt(1)
               int2.put(3*Float.SIZE_BYTES)
               glVertexAttribPointer(1,4, GL_FLOAT,false,floatCount*Float.SIZE_BYTES,int2)
               glEnableVertexAttribArray(1)

              val int3=mallocInt(1)
               int3.put((3+4)*Float.SIZE_BYTES)
               glVertexAttribPointer(2,2, GL_FLOAT,false,floatCount*Float.SIZE_BYTES,int3)
               glEnableVertexAttribArray(2)

              glBindBuffer(GL_ARRAY_BUFFER,0)
               glBindVertexArray(0)
               vao
           }
            return res

        }
    }
}
enum class TextureType2d(val intCode:Int){
    SIMPLE_COLOR(1), TEXTURED(2)
}