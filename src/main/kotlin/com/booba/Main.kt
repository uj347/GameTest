package com.booba
/* test */
import com.booba.objects.ColoredObject2D
import com.booba.shaders.ResourceShader
import com.booba.shaders.TestShader
import org.lwjgl.opengl.GL11.GL_TRIANGLES
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
    val shaderSpec=ResourceShader()
    val triangle=ColoredObject2D(
        listOf(
            -0.9f to 0f to Color.YELLOW,
            0f to 0.9f to Color.YELLOW,
            0.9f to 0f to Color.YELLOW
        )
    )
    HelloWorldWindow(shaderSpec =shaderSpec ).apply {
        renderState.value={
            val vao=triangle.vao
            GL46.glBindVertexArray(vao)
            glVertexArra
            GL46.glDrawArrays(GL_TRIANGLES,0,3)
            GL46.glBindVertexArray(0)
        }
        run()
    }

}
