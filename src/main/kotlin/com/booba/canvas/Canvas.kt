package com.booba.canvas

import com.booba.animators.Animator
import com.booba.animators.AnimatorHolder
import com.booba.canvas.testcanvas.AnimatorRegistry
import com.booba.interactable.Interactable
import com.booba.interactable.Interaction
import com.booba.placeable.Placeable

interface Canvas:Interactable,AnimatorHolder {

    fun drawFrame(currentTime:Long)
    fun addPlaceable(placeable: Placeable)
    fun removePlaceable(id:Long)
    fun removePlaceable(placeable:Placeable)=removePlaceable(placeable.id)
    val placeables:Collection<Placeable>
}