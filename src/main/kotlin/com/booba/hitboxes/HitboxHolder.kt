package com.booba.hitboxes

import com.booba.DimensionF
import org.joml.Matrix4f

interface HitboxHolder {
    val hitboxes:List<Hitbox>
    val globalToNdcTransformMatrix:Matrix4f

    fun addHitbox(hitbox: Hitbox)
    fun clearHitboxes()
    fun checkHitPoint(coord:DimensionF):Boolean
    fun checkHitPoly(anotherPoly:Array<DimensionF>):Boolean
}