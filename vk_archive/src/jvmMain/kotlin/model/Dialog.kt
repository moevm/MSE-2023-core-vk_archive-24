package model

import kotlinx.serialization.Serializable

@Serializable
data class Dialog(
    var id:String = "",
    var name:String = "",
    var messages: MutableList<Message> = mutableListOf()
)