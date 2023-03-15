package com.booba
/* test */
import com.booba.objects.ColoredObject2D
import com.booba.objects.TexturedObject2D
import com.booba.shaders.ResourceShader
import com.booba.shaders.ShaderProgramSpec
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.lwjgl.glfw.GLFW
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL30.*
import withBuf
import java.awt.Color
import java.awt.image.DataBuffer
import javax.imageio.ImageIO
import kotlin.io.path.Path


fun main(){
//    tryLoadRes()
    helloWorld()
}


fun tryLoadRes(){
    val res=ClassLoader.getSystemResource("VERTEX_SHADER").openStream()
    println(res.bufferedReader().readLine())
}

fun helloWorld(){

    val shaderSpec=ResourceShader(
        listOf(ShaderProgramSpec.UniformSpec(name = "transform",
            setter = {newVal, pId,location, name ->
                glUniformMatrix4fv(location,false,newVal as FloatArray)
            })
        )

    )
    val triangleColor=ColoredObject2D(
        listOf(
            (-0.9f to 0f) to Color.YELLOW,
            (0f to 0.9f) to Color.YELLOW,
            (0.9f to 0f) to Color.YELLOW,

        )
    )
    val triangleTexture=TexturedObject2D(
        listOf<Pair<DimensionF,DimensionF>>(
//            (-1f to 0f) to (-0.5f to 0f),
//            (0f to 1f) to (0f to 1f),
//            (1f to 0f) to (1f to 0f),
            (-0.5f to 0.5f) to (0f to 1f),
            (0.5f to 0.5f) to (1f to 1f),
            (-0.5f to -0.5f) to (0f to 0f),
            (-0.5f to -0.5f) to (0f to 0f),
            (0.5f to 0.5f) to (1f to 1f),
            (0.5f to -0.5f) to (1f to 0f),
            )
    )


    val idMatrixState:MutableStateFlow<FloatArray> = MutableStateFlow(
        floatArrayOf(
            1f,0f,0f,0f,
            0f,1f,0f,0f,
            0f,0f,1f,0f,
            0f,0f,0f,1f
        )
    )

    val actionMap:ActionMap= mapOf(
        GLFW.GLFW_KEY_LEFT to {_,_,->
            idMatrixState.transpose(-1f,0f,0.05f)
        },
        GLFW.GLFW_KEY_RIGHT to {_,_,->
            idMatrixState.transpose(1f,0f,0.05f)
        },
        GLFW.GLFW_KEY_UP to {_,_,->
            idMatrixState.transpose(0f,1f,0.05f)
        },
        GLFW.GLFW_KEY_DOWN to {_,_,->
            idMatrixState.transpose(0f,-1f,0.05f)
        }

    )


    HelloWorldWindow(shaderSpec =shaderSpec
    ).apply {
        actionMapState.update {
            it+actionMap
        }
        runBlocking {

            launch(Dispatchers.IO) {
               delay(1000)
                renderState.value={programId->
                shaderSpec.setUniform("transform",idMatrixState.value)
                val texture=genTexture("C:\\Shared\\GOSPODA.jpg")
                    glBindVertexArray(triangleTexture.vao)
                    glDrawArrays(GL_TRIANGLES,0,6)
                    GL11.glDeleteTextures(texture)
                    println("Render state invoked!!!")
//            GL46.glBindVertexArray(0)
                }
            }
            run()
        }

    }


}

fun MutableStateFlow<FloatArray>.transpose(x:Float,y:Float,step:Float){
    val xInd=3
    val yInd=7
    update {
        val oldX=it[xInd]
        val oldY=it[yInd]
        it[xInd]=it[xInd]+step*x
        it[yInd]=it[yInd]+step*y
        it.clone()
    }
}


fun genTexture(path:String):Int{
    var res=-1
    val file=path.let(::Path).toFile()
    var size=0
    var width=0
    var height=0
    val img=ImageIO.read(file)

    width=img.width
    height=img.height
    size=width*height*3
    withBuf(size){bBuf->



        for(pixY in height-1 downTo  0){
            for (pixX in width-1 downTo 0){

                val (r,g,b)=Color(img.getRGB(pixX,pixY)).let{c->
                    arrayOf(
                        c.red.toByte(),
                        c.green.toByte(),
                        c.blue.toByte()
                    )
                }
                bBuf.put(r)
                bBuf.put(b)
                bBuf.put(g)
            }
        }
        bBuf.flip()

        img.graphics.dispose()
        res=glGenTextures()
        glBindTexture(GL_TEXTURE_2D,res)
        glTexImage2D(
            GL_TEXTURE_2D, 0, GL_RGB, width, height, 0, GL_RGB,
            GL_UNSIGNED_BYTE,bBuf
        )

        glGenerateMipmap(GL_TEXTURE_2D)
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP)

    }
    if (res==-1){error("No texture initalized")}

        return res
}