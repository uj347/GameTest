package com.booba.canvasobjects

import com.booba.DimensionF
import com.booba.canvas.Canvas
import com.booba.shaders.ShaderProgramSpec
import com.booba.vertexdata.DebugVertexMeshData
import genId
import getValue
import kotlinx.coroutines.flow.MutableStateFlow
import org.lwjgl.opengl.GL46.*
import setValue

class DebugNdcVertexPlaceableObject(
    private val vertexMeshData: DebugVertexMeshData,
     initProgramSpec: ShaderProgramSpec
):Placeable,Drawable {
    override val id: Long = genId()
    override val tags: Collection<String> = setOf("DebugNdcVertexPlaceable")
    override val rotationDegreeState: MutableStateFlow<Float> = MutableStateFlow(0f)
    override var rotationDegree: Float by rotationDegreeState
    override val coordinatesState: MutableStateFlow<DimensionF> =MutableStateFlow(0f to 0f)
    override var coordinates: DimensionF by coordinatesState
    override val dimensionsState: MutableStateFlow<DimensionF> =MutableStateFlow(0f to 0f)
    override var dimensions: DimensionF by dimensionsState
    override val shaderProgramState: MutableStateFlow<ShaderProgramSpec> = MutableStateFlow(initProgramSpec)
    override var shaderProgram: ShaderProgramSpec by shaderProgramState

    override fun draw(currentTime: Long) {
        glUseProgram(shaderProgram.programId!!)
        glBindVertexArray(vertexMeshData.vao)
        glDrawArrays(GL_LINE_STRIP,0,vertexMeshData.vertices.size-1)
    }


    override fun placeOn(canvas: Canvas) {
       canvas.addPlaceable(this)
    }

    override fun removeFrom(canvas: Canvas) {
        canvas.removePlaceable(this)
    }

}