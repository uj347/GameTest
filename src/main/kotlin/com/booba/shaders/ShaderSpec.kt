package com.booba.shaders



interface ShaderSpec {
    val vertexShaderString:String
    val fragmentShaderString:String
    val vertexShaderId:Int?
    val fragmentShaderId:Int?
    val programId:Int?


    val isCompiled:Boolean
    fun compile():Boolean
    fun createProgram():Int
}