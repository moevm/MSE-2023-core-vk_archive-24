package model

data class Message(
        var authorId:String = "",
        var authorName:String = "",
        var messageTime:String = "",
        var editTime:String? = null,
        var message:String? = null,
        var attachments:List<Attachment> = arrayListOf()
)
