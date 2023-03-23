package com.booba.placeable

import com.booba.DimensionF
import com.booba.animators.Animator
import com.booba.canvas.Canvas
import kotlinx.coroutines.flow.MutableStateFlow

interface Placeable {
    val id:Long
    val tags:Collection<String>



    val rotationDegreeState: MutableStateFlow<Float>
    var rotationDegree:Float


    val coordinatesState: MutableStateFlow<DimensionF>
    var coordinates:DimensionF


    val dimensionsState: MutableStateFlow<DimensionF>
    var dimensions:DimensionF

    fun placeOn(canvas: Canvas)
    fun removeFrom(canvas:Canvas)


}
