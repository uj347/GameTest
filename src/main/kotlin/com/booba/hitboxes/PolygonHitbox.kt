package com.booba.hitboxes

import com.booba.DimensionF
import com.booba.shaders.worldTransformMatrixSpec
import org.joml.Intersectionf
import org.joml.Matrix4f
import org.joml.PolygonsIntersection
import org.joml.Vector2f
import org.joml.Vector4f
import toVec2f

class PolygonHitbox(private val vertices:Array<DimensionF>):Hitbox {
//    private var transformMatrix4f:Matrix4f=Matrix4f()

//    private val v2TransformedVertices:Array<Vector2f>
//        get() = vertices.map {(x,y)->
////            println("Input to transform : $x $y")
//            transformMatrix4f.transformAffine(Vector4f(x,y,0f,0f)).let{
//                val res= Vector2f(it.x,it.y)
////                println("Output from transform : $res.x $res.y")
//                res
//            }
//        }
////            .onEach { println("Hitbox vertex array: $it") }
//            .toTypedArray()

    private val polygonsIntersection=PolygonsIntersection(
        vertices.flatMap { listOf(it.first,it.second)}.toFloatArray(),
        intArrayOf(0),vertices.size-1)

    override fun checkHitPoint(hit: DimensionF): Boolean =polygonsIntersection.testPoint(hit.first,hit.second)




    override fun checkHitPoly(anotherPoly: Array<DimensionF>): Boolean = Intersectionf.testPolygonPolygon(
           vertices.map { it.toVec2f() }.toTypedArray(),
            anotherPoly.map { it.toVec2f() }.toTypedArray()
        )

}