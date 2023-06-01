package processing

import model.AttachmentType
import model.Dialog
import utils.AttachmentSaver
import utils.UIProcessUpdater
import utils.downloadAttachment

object DialogsProcessing {
    /**
     * пример использования: downloadAttachments(dialog, listOf(AttachmentType.PHOTO, AttachmentType.VIDEO))
     */
    fun downloadAttachments(
        dialogs: List<Dialog>,
        fileTypesToDownload: List<AttachmentType>,
        attachmentSaver: AttachmentSaver,
        amountMessages: Int? = null,
        uiUpdater: UIProcessUpdater? = null,
        isActive: () -> Boolean
    ) {
        uiUpdater?.initProcess()
        uiUpdater?.updateProcessStatus("0/${dialogs.size}")
        for((index, dialog) in dialogs.withIndex()) {
            if (isActive()) {
                val messagesToProcess = dialog.messages.take(amountMessages ?: dialog.messages.size)
                for (message in messagesToProcess) {
                    if (isActive()) {
                        for (attachment in message.attachments) {
                            if (isActive()) {
                                if (attachment.url == null) continue
                                when (attachment.attachmentType) {
                                    in AttachmentType.PHOTO.translates.values -> {
                                        if (AttachmentType.PHOTO !in fileTypesToDownload) continue

                                        val regexImage = Regex("""/([\w-]+\.(?:jpg|png|jpeg|gif))""")
                                        val os = regexImage.find(attachment.url)?.value?.let { photoName ->
                                            attachmentSaver.createOutputStream(dialog.id, photoName)
                                        } ?: continue

                                        val photoIS = downloadAttachment(attachment.url)
                                        photoIS?.copyTo(os)
                                        photoIS?.close()
                                        os.close()
                                    }

                                    in AttachmentType.VIDEO.translates.values,
                                    in AttachmentType.GIFT.translates.values,
                                    in AttachmentType.FILE.translates.values,
                                    in AttachmentType.STICKER.translates.values,
                                    in AttachmentType.URL.translates.values,
                                    in AttachmentType.AUDIO.translates.values,
                                    in AttachmentType.CALL.translates.values,
                                    in AttachmentType.COMMENT.translates.values,
                                    in AttachmentType.POST.translates.values -> { continue }
                                    else -> { continue }
                                }
                            } else break
                        }
                    } else break
                }
            }
            uiUpdater?.updateProcessStatus("${index + 1}/${dialogs.size}")
        }
        uiUpdater?.finishProcess()
    }
}