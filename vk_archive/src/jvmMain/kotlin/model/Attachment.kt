package model

import kotlinx.serialization.Serializable

@Serializable
data class Attachment(
        val attachmentType:String,
        val url:String? = null
)