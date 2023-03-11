package com.booba
/* test */
import com.booba.shaders.ResourceShader
import com.booba.shaders.TestShader


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
    HelloWorldWindow(shaderSpec =shaderSpec ).run()

}
