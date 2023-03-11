package com.booba
/* test */
import com.booba.objects.ColoredObject2D
import com.booba.objects.Object2D
import com.booba.shaders.ResourceShader
import com.booba.shaders.TestShader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL30.glBindVertexArray
import org.lwjgl.opengl.GL40
import org.lwjgl.opengl.GL46
import java.awt.Color


fun main(){
//    tryLoadRes()
    helloWorld()
}


fun tryLoadRes(){
    val res=ClassLoader.getSystemResource("VERTEX_SHADER").openStream()
    println(res.bufferedReader().readLine())
}

fun helloWorld(){
    val shaderSpec=TestShader()
    val triangle=ColoredObject2D(
        listOf(
            (-0.9f to 0f) to Color.YELLOW,
            (0f to 0.9f) to Color.YELLOW,
            (0.9f to 0f) to Color.YELLOW
        )
    )
    HelloWorldWindow(shaderSpec =shaderSpec ).apply {
        runBlocking {
            launch(Dispatchers.IO) {
               delay(5000)
                renderState.value={
                    val vao=Object2D.createVao(triangle)
//            gl
                    glBindVertexArray(vao)
                    glDrawArrays(GL_LINES,0,3)
                    println("Render state invoked!!!")
//            GL46.glBindVertexArray(0)
                }
            }
            run()

        }

    }


}
