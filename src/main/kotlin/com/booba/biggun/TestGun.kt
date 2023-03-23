package com.booba.biggun

import com.booba.DimensionF
import com.booba.animators.*
import com.booba.placeable.PlaceableObject2D
import degreeToRads
import org.joml.Matrix4f
import org.joml.Vector3f
import org.joml.Vector4f
import toDimensionF
import toVec4f

class TestGun(
val gunObject:PlaceableObject2D,
val projectileProducer:(coord:DimensionF,direction:Float)->PlaceableObject2D,
val projectileSpeed:Float
) {
    val animators= mutableListOf<Animator<Vector4f>>()
    val projectiles= mutableListOf<PlaceableObject2D>()
    fun drawComplex(time:Long){
//        gunObject.placeOn()
        for(animator in animators){
            animator.commitTime(time)
        }
        for (projectile in projectiles) {
//            projectile.placeOn()
        }

    }

    fun fire(){
        val startPosition=gunObject.coordinates
        val direction=gunObject.rotationDegree
        val projectile=projectileProducer(startPosition,direction)
        projectiles.add(projectile)
//        val projAnimator=object:InfiniteGenericAnimator<Vector4f>(){
//            override val deltaTimeMapper: (Long) -> Vector4f={delta->
//                val units=delta*0.001f*projectileSpeed
//                val vec= Vector4f(0f,units,0f,1f)
//                val matrix=Matrix4f()
//                    .rotate(direction.degreeToRads(),Vector3f(0f,0f,1f))
//
//                val resVec= matrix.transformAffine(vec)
//                resVec
//
//            }
//            override val valueAdder: (old: Vector4f, deltaT: Vector4f) -> Vector4f= { old, deltaT ->
//            old.add(deltaT)
//            }
//            override val valueGetter: () -> Vector4f={
//                projectile.coordinates.toVec4f()
//            }
//            override val valueSetter: (Vector4f) -> Unit= {
//                projectile.coordinates=it.toDimensionF()
//            }
//        }

//        projAnimator.launch()
//        animators.add(projAnimator)
//        projAnimator.launch()

    }
}