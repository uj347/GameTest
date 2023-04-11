package com.booba.hitboxes

import com.booba.Dimension
import com.booba.DimensionF
import org.joml.Matrix4f
import org.joml.PolygonsIntersection

interface Hitbox {
    fun checkHitPoint(hit:DimensionF):Boolean
     fun checkHitPoly(anotherPoly: Array<DimensionF>):Boolean

}
object NeverHitbox:Hitbox{
    override fun checkHitPoint(hit: DimensionF): Boolean =false
    override fun checkHitPoly(anotherPoly:  Array<DimensionF>): Boolean=false

}

fun wholeRectHitbox(topLeft:DimensionF,size:DimensionF):Hitbox{
    return object:Hitbox{


        override fun checkHitPoint(hit: DimensionF): Boolean {
        return hit.first in -1f..1f && hit.second in -1f..1f
    }

    override fun checkHitPoly(anotherPoly: Array<DimensionF>): Boolean {
        val ndcVerts= floatArrayOf(-1f,1f,1f,1f,1f,-1f,-1f,-1f)
        val intersec=PolygonsIntersection(ndcVerts, intArrayOf(0),3)
        return anotherPoly.any { point->intersec.testPoint(point.first,point.second) }
    }}
}

