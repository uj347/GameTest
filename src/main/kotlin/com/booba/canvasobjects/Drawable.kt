package com.booba.canvasobjects

import com.booba.shaders.ShaderProgramSpec
import kotlinx.coroutines.flow.MutableStateFlow

 interface Drawable {
    fun draw(currentTimeMillis:Long)
    val shaderProgramState: MutableStateFlow<ShaderProgramSpec>
    var shaderProgram: ShaderProgramSpec
}