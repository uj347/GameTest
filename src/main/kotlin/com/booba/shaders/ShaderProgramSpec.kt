package com.booba.shaders


import org.lwjgl.opengl.GL46.glGetUniformLocation


interface ShaderProgramSpec {
    val vertexShaderString:String
    val fragmentShaderString:String
    val vertexShaderId:Int?
    val fragmentShaderId:Int?
    val programId:Int?
    val uniforms:List<UniformSpec>

    val isCompiled:Boolean
    fun compile():Boolean
    fun useProgram()
//    fun createProgram():Int
    fun setUniform(name: String,value:Any):Boolean{
        val uniSpec=uniforms.firstOrNull { it.name==name}
        if(uniSpec!=null){
            useProgram()
            uniSpec.setValue(programId!!,value)
        }
        return uniSpec!=null
    }



    class UniformSpec(val name:String,
                      private val setter:(newVal:Any,pId:Int,location:Int,name:String)->Unit){
        fun getLocation(pId:Int)= glGetUniformLocation(pId,name)

        fun setValue(pId:Int,newVal:Any)=setter(newVal,pId,getLocation(pId),name)

    }
}