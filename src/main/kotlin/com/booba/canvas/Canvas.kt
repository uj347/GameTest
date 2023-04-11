package com.booba.canvas

import com.booba.animators.AnimatorHolder
import com.booba.interactable.Interactable
import com.booba.canvasobjects.Placeable

interface Canvas:Interactable,AnimatorHolder {

    fun drawFrame(currentTime:Long)
    fun addPlaceable(placeable: Placeable)
    fun removePlaceable(id:Long)
    fun removePlaceable(placeable:Placeable)=removePlaceable(placeable.id)
    val placeables:Collection<Placeable>
}