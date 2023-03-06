package com.booba
/* test */
import com.booba.shaders.TestShader


fun main(){

    helloWorld()
}

fun helloWorld(){
    val shaderSpec=TestShader()
    HelloWorldWindow(shaderSpec =shaderSpec ).run()
}
