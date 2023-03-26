package model

data class Dialog(
    var id:String = "",
    var name:String = "",
    var messages: List<Message> = arrayListOf()
)