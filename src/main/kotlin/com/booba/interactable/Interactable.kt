package com.booba.interactable

interface Interactable {
    var isInteractable:Boolean
    val interactionRegistry:Set<Interaction>
    fun addInteraction(interaction: Interaction)
    fun removeInteraction(name:String)
    fun interact(anothers: Collection<Interactable>)
}