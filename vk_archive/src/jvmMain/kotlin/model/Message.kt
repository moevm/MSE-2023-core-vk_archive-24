package model

import java.util.Date

data class Message(
        val authorId:Int,
        val authorName:String,
        val messageTime:Date,
        val editTime:Date? = null,
        val message:String? = null,
        val attachments:MutableList<Attachment> = mutableListOf()
)
