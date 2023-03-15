package com.booba.shaders



import  org.lwjgl.opengl.GL46.*
import withMemStack


class TestShader:ShaderProgramSpec {
    override val uniforms: List<ShaderProgramSpec.UniformSpec> = listOf()


        override fun useProgram() {
            if(!_isCompiled)compile()
            if(_programId==null)createProgram()
            glUseProgram(_programId!!)
        }


    override val vertexShaderString: String ="#version 330 core\n"+
    "layout (location = 0) in vec3 aPos;\n"+
    "void main()\n"+
    "{\n"+
    " gl_Position = vec4(aPos.x, aPos.y, aPos.z, 1.0);\n"+
    "}"

    override val fragmentShaderString: String ="" +
            "#version 330 core\n" +
            "out vec4 FragColor;\n" +
            "void main()\n" +
            "{\n" +
            "FragColor = vec4(1.0f, 0.5f, 0.2f, 1.0f);\n" +
            "}"

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

    override fun compile(): Boolean {
        _vertexShaderId=glCreateShader(GL_VERTEX_SHADER)
        glShaderSource(_vertexShaderId!!,vertexShaderString)
        glCompileShader(_vertexShaderId!!)
        withMemStack {
            val buf=mallocInt(1)
            glGetShaderiv(_vertexShaderId!!, GL_COMPILE_STATUS,buf)
            val res=buf.get(0)
            if(res==0) {
                val log= glGetShaderInfoLog(_vertexShaderId!!,)
                error("Fuck you vertexShader: $log")
            }
        }

        _fragmentShaderId=glCreateShader(GL_FRAGMENT_SHADER)
        glShaderSource(_fragmentShaderId!!,fragmentShaderString)
        glCompileShader(_fragmentShaderId!!)
        withMemStack {
            val buf=mallocInt(1)
            glGetShaderiv(_fragmentShaderId!!, GL_COMPILE_STATUS,buf)
            val res=buf.get(0)
            if(res==0) {
                val log= glGetShaderInfoLog(_fragmentShaderId!!,)
                error("Fuck you fragmentShader: $log")
            }
        }

        createProgram()
        _isCompiled=true
        return _programId!=null
    }

    private fun createProgram(): Int {
        _programId= glCreateProgram()
        glAttachShader(_programId!!,_vertexShaderId!!)
        glAttachShader(_programId!!,_fragmentShaderId!!)
        glLinkProgram(_programId!!)
        withMemStack{
            val buf=mallocInt(1)
            glGetProgramiv(_programId!!, GL_LINK_STATUS,buf)
            val res=buf.get(0)
            if(res==0) error("Fuck you programLinking")
        }
        return _programId!!
    }
}
