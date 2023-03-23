package com.booba.interactable


data class Interaction (
    val name:String,
    val launchPredicate:(anothers:Collection<Interactable>, self: Interactable)->Boolean,
    val block:(anothers:Collection<Interactable>, self: Interactable)->Unit,

    ){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Interaction

        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }
}