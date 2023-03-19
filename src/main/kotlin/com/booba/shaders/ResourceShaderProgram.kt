package com.booba.shaders

import org.lwjgl.opengl.GL20.*
import org.lwjgl.opengl.GL46
import withMemStack

class ResourceShaderProgram(
    override val uniforms: List<ShaderProgramSpec.UniformSpec>
):ShaderProgramSpec {




    override val vertexShaderString: String by lazy{loadShaderString(SHADER_TYPE.VERTEX).also(::println)}
    override val fragmentShaderString: String by lazy{loadShaderString(SHADER_TYPE.FRAGMENT).also(::println)}

    private var _vertexShaderId:Int?=null
    override val vertexShaderId: Int?
        get() = _vertexShaderId

    private var _fragmentShaderId:Int?=null
    override val fragmentShaderId: Int?
        get() =_fragmentShaderId

    private var _programId:Int?=null
    override val programId: Int?
        get() = _programId

    private var _isCompiled=false
    override val isCompiled: Boolean
        get() = _isCompiled

    /**Will compile an create program if not done until invokation of this function */
    override fun useProgram() {
        if(!_isCompiled)compile()
        glUseProgram(_programId!!)
    }

    override fun compile(): Boolean {
        _vertexShaderId= GL46.glCreateShader(GL46.GL_VERTEX_SHADER)
        GL46.glShaderSource(_vertexShaderId!!, vertexShaderString)
        GL46.glCompileShader(_vertexShaderId!!)
        withMemStack {
            val buf=mallocInt(1)
            GL46.glGetShaderiv(_vertexShaderId!!, GL46.GL_COMPILE_STATUS, buf)
            val res=buf.get(0)
            if(res==0) {
                val log= GL46.glGetShaderInfoLog(_vertexShaderId!!,)
                error("Fuck you vertexShader: $log")
            }

        }

        _fragmentShaderId= GL46.glCreateShader(GL46.GL_FRAGMENT_SHADER)
        GL46.glShaderSource(_fragmentShaderId!!, fragmentShaderString)
        GL46.glCompileShader(_fragmentShaderId!!)
        withMemStack {
            val buf=mallocInt(1)
            GL46.glGetShaderiv(_fragmentShaderId!!, GL46.GL_COMPILE_STATUS, buf)
            val res=buf.get(0)
            if(res==0) {
                val log= GL46.glGetShaderInfoLog(_fragmentShaderId!!,)
                error("Fuck you fragmentShader: $log")
            }
        }
        _programId=createProgram()
        _isCompiled=true
        return _programId!=null
    }

    private fun createProgram(): Int {
        _programId= GL46.glCreateProgram()
       glAttachShader(_programId!!, _vertexShaderId!!)
        glAttachShader(_programId!!, _fragmentShaderId!!)
        glLinkProgram(_programId!!)
        withMemStack{
            val buf=mallocInt(1)
            GL46.glGetProgramiv(_programId!!, GL46.GL_LINK_STATUS, buf)
            val res=buf.get(0)
            if(res==0) error("Fuck you programLinking")
        }
        return _programId!!
    }



        companion object{
            private fun loadShaderString(type:SHADER_TYPE):String{
                return ClassLoader
                    .getSystemResource(type.resPath)
                    .openStream()
                    .bufferedReader()
                    .readText()
            }

        }
    private enum class SHADER_TYPE(val resPath:String) {
        VERTEX("VERTEX_SHADER"),FRAGMENT("FRAGMENT_SHADER")
    }


}