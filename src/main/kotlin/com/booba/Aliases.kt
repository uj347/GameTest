package com.booba

import org.lwjgl.system.MemoryStack

typealias ActionMap=Map<Int,(windowId:Long,keyCode:Int)->Unit>
typealias Dimension=Pair<Int,Int>
typealias DimensionF=Pair<Float,Float>
typealias MemStack =MemoryStack