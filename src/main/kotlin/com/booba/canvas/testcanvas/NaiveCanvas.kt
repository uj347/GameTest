package com.booba.canvas.testcanvas

import com.booba.animators.AnimatorHolder
import com.booba.animators.AnimatorTarget
import com.booba.canvas.Canvas
import com.booba.interactable.Interactable
import com.booba.interactable.Interaction
import com.booba.placeable.Placeable
import com.booba.placeable.PlaceableObject2D
import getValue
import kotlinx.coroutines.flow.MutableStateFlow
import setValue


class NaiveCanvas: Canvas,AnimatorHolder by AnimatorRegistry() {

    override fun drawFrame(currentTime: Long) {
        commitTime(currentTime)
        interact(
            _placeables.filterIsInstance<Interactable>()
        )
        _placeables
            .filterIsInstance<PlaceableObject2D>()
            .forEach {it.draw(currentTime) }
    }

    override fun addPlaceable(placeable: Placeable) {
        _placeables.add(placeable)
    }

    override fun removePlaceable(id: Long) {
       _placeables.filter { it.id==id }
            .filterIsInstance<AnimatorTarget>()
            .map{it.id}
            .forEach {
                animationsRegistry[it]?.forEach {anim->
                    anim.stop()
                }
                removeAnimatorsForTarget(id)
            }

        _placeables.removeIf { it.id==id }
    }



    private val _placeables= mutableListOf<Placeable>()

    override val placeables: Collection<Placeable>
        get() = _placeables

    private val _interactibleState= MutableStateFlow(true)
    override var isInteractable: Boolean by _interactibleState

    private val _interactionRegistry= mutableSetOf<Interaction>()
    override val interactionRegistry: Set<Interaction>
        get()=_interactionRegistry

    override fun addInteraction(interaction: Interaction) {
        _interactionRegistry.add(interaction)
    }

    override fun removeInteraction(name: String) {
        _interactionRegistry.removeIf {it.name==name  }
    }

    override fun interact(anothers: Collection<Interactable>) {
        if(isInteractable){
            _interactionRegistry
                .filter { it.launchPredicate(anothers,this) }
                .forEach { it.block(anothers,this) }
        }
    }
}