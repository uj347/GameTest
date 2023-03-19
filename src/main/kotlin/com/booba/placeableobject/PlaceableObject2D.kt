package com.booba.placeableobject

import com.booba.DimensionF
import com.booba.objects.Object2D
import com.booba.shaders.ShaderProgramSpec
import getValue
import kotlinx.coroutines.flow.MutableStateFlow
import org.joml.Matrix4f
import setValue

abstract class PlaceableObject2D (
    object2D: Object2D,
    initProgram:ShaderProgramSpec,
    initDimension:DimensionF,
    initCoordinates: DimensionF,
    initRotationDegree:Float
        ){

    val shaderProgramState: MutableStateFlow<ShaderProgramSpec> = MutableStateFlow(initProgram)
    var shaderProgram:ShaderProgramSpec by shaderProgramState
    val object2DState: MutableStateFlow<Object2D> = MutableStateFlow(object2D)

    var object2D:Object2D by object2DState

    val rotationDegreeState: MutableStateFlow<Float> = MutableStateFlow(initRotationDegree)
    var rotationDegree:Float by rotationDegreeState
    val coordinatesState: MutableStateFlow<DimensionF> = MutableStateFlow(initCoordinates)
    var coordinates:DimensionF by coordinatesState
    val dimensionsState: MutableStateFlow<DimensionF> = MutableStateFlow(initDimension)
    var dimensions:DimensionF by dimensionsState

    abstract fun draw()

}