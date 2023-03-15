package com.booba.objects

import com.booba.MemStack
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL15

import org.lwjgl.opengl.GL40.*
import org.lwjgl.system.MemoryUtil
import put
import withMemStack

 sealed class Object2D {
    abstract val vertices:List<VertexDescription2D>
    val vao:Int by lazy {
        createVao()
    }
     val vertexCount
        get() = vertices.size
    abstract val textureType2D:TextureType2d
    abstract fun releaseResources()

    companion object{
        fun createVao(obj2d:Object2D):Int=obj2d.createVao()
        internal fun Object2D.createVao():Int{

//            val vaoId=glGenVertexArrays()
//            glBindVertexArray(vaoId)
            val res= withMemStack {
                val fSize=Float.SIZE_BYTES
               val floatCount=3+4+2
               val bufSize=vertices.size*(floatCount)
               val buf=mallocFloat(bufSize)

                when(textureType2D){
                   TextureType2d.SIMPLE_COLOR->{
                       vertices.forEach { vDescr->
                           val color=(vDescr.vertexTextureData as SimpleColor2D).color

                           val (r,g,b,a)=arrayOf(color.red/255f,color.green/255f,color.blue/255f,color.alpha/255f)
//                               arrayOf(1f,1f,1f,0f)

                           buf.put(vDescr.x,vDescr.y,1f)
                           buf.put(r,g,b,a)
                           buf.put(0f,0f)
//

                       }
                   }
                   TextureType2d.TEXTURED-> {
                       vertices.forEach { vDescr->

                           buf.put(vDescr.x,vDescr.y,1f)
                           buf.put(0f,0f,0f,0f)
                           with((vDescr.vertexTextureData as Texture2D).textureCoordinates){
                               buf.put(first,second)
                           }

                       }

                   }
               }
               //TODO


                buf.flip()
               val vao= glGenVertexArrays()
               glBindVertexArray(vao)
               val vbo=glGenBuffers()
               glBindBuffer(GL_ARRAY_BUFFER,vbo)
               glBufferData(GL_ARRAY_BUFFER,buf, GL_DYNAMIC_DRAW)

              val int1=(0*fSize).toLong()
               glVertexAttribPointer(0,3, GL_FLOAT,false,floatCount*fSize,int1)
               glEnableVertexAttribArray(0)

              val int2=(3*fSize).toLong()
               glVertexAttribPointer(1,4, GL_FLOAT,false,floatCount*fSize,int2)
               glEnableVertexAttribArray(1)

              val int3=((3+4)*fSize).toLong()
               glVertexAttribPointer(2,2, GL_FLOAT,false,floatCount*fSize,int3)
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