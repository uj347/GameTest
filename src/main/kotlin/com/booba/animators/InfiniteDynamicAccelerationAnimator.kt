package com.booba.animators

import com.booba.DimensionF
import genId
import getValue
import kotlinx.coroutines.flow.MutableStateFlow
import setValue

class InfiniteDynamicAccelerationAnimator(
    override val valueGetter: () -> DimensionF,
override val valueSetter: (DimensionF) -> Unit,
private val initAccelerationUnitVector:DimensionF=(0f to 0f)
):InfiniteGenericAnimator<DimensionF>() {

    var acceleration:DimensionF=initAccelerationUnitVector

    var lastFrameSpeed:DimensionF=(0f to 0f)



    override val id: Long = genId()

    override val deltaTimeMapper: (Long) -> DimensionF={time->
        val deltaSecs=time*0.001f
        val newSpeed=acceleration*deltaSecs +lastFrameSpeed
        lastFrameSpeed=newSpeed
        newSpeed*deltaSecs
    }
    override val valueAdder: (old: DimensionF, deltaT: DimensionF) -> DimensionF= {old,new-> old + new }

    companion object{
        private operator fun DimensionF.plus(another:DimensionF)=this.first+another.first to this.second+another.second
        private operator fun DimensionF.minus(another:DimensionF)=this.first-another.first to this.second-another.second
        private operator fun DimensionF.times(another:DimensionF)=this.first*another.first to this.second*another.second
        private operator fun DimensionF.div(another:DimensionF)=this.first/another.first to this.second/another.second
        private operator fun DimensionF.times(scale:Float)=this.first*scale to this.second*scale
        private operator fun DimensionF.div(scale:Float)=this.first/scale to this.second/scale

    }
}