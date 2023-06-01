package utils

import model.Attachment
import model.Dialog
import model.Message
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import java.io.IOException
import java.io.InputStream
import java.util.*

object VkDialogParser {

    /**
     * dialogId - id диалога
     * htmlString - список строчек. Текст строк представляет собой текст html-файла, содержащего сообщения из диалога
     */
    fun parseDialogFromArray(htmlStrings: List<String>,dialogId: String): Dialog {
        val dialog = Dialog()
        dialog.id = dialogId

        for (html in htmlStrings) {
            val tmpMessages = parseMessagesFromString(dialog,html)
            addMessagesToDialog(dialog,tmpMessages)
        }
        return dialog
    }

    private fun parseMessagesFromString(dialog:Dialog,htmlString: String):List<Message>{
        val document: Document = try {
            Jsoup.parse(htmlString)
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
        if (dialog.name == "")
            setName(dialog,document)
        return pushMessagesFromDOM(document)
    }

    /**
     * dialogId - id диалога
     * charsetName - кодировка
     * baseUri - URL, для разрешения относительных ссылок
     */
    fun parseVkMessagesFromStream(
        dialogId: String,
        inputStream: InputStream,
        charsetName: String = "UTF-8",
        baseUri: String="-"
    ): Dialog {
        val dialog = Dialog()
        dialog.id = dialogId

        val tmpMessages = parseMessagesFromStream(dialog,inputStream,charsetName,baseUri)
        addMessagesToDialog(dialog,tmpMessages)
        return dialog
    }

    private fun parseMessagesFromStream(dialog:Dialog, inputStream:InputStream,charsetName: String = "UTF-8", baseUri: String="-"):List<Message>{
        val document: Document = try {
            Jsoup.parse(inputStream, charsetName, baseUri)
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
        if (dialog.name == "")
            setName(dialog,document)
        return pushMessagesFromDOM(document)
    }

    private fun parseMonth(month:String):Int{
        when (month){
            "янв"-> return 1
            "фев"-> return 2
            "мар"-> return 3
            "апр"-> return 4
            "мая"-> return 5
            "июн"-> return 6
            "июл"-> return 7
            "авг"-> return 8
            "сен"-> return 9
            "окт"-> return 10
            "ноя"-> return 11
            "дек"-> return 12
        }
        return 0
    }

    private fun parseDate(dateStr: String): Date {
        val tmpArray = dateStr.split(" ")
        val timeArray = tmpArray[4].split(":")
        return Date(
            tmpArray[2].toInt() - 1900,
            parseMonth(tmpArray[1]) - 1,
            tmpArray[0].toInt(),
            timeArray[0].toInt(),
            timeArray[1].toInt(),
            timeArray[2].toInt()
        )
    }

    fun addMessagesToDialog(dialog:Dialog,messages: List<Message>){
        dialog.messages.addAll(messages)
    }

    fun setName(dialog: Dialog, document: Document){
        val dialogHeaderBlock = document.getElementsByClass("ui_crumb")
        val div = dialogHeaderBlock.eq(2)
        val name = div.first()!!.text()
        dialog.name = name
    }

    fun pushMessagesFromDOM(document: Document):List<Message>{
        val messages: MutableList<Message> = mutableListOf()
        val divs: Elements = document.getElementsByClass("item")
        // div in divs
        for (i in 0 until divs.size) {
            val div = divs[i]
            val message = Message()
            val messageBlock: Element? = div.getElementsByClass("message").first()
            val attachmentsBlock: Elements = messageBlock!!.getElementsByClass("attachment")
            val headerBlock: Elements = div.getElementsByClass("message__header")

            val nameAndTime: List<String> = headerBlock.first()?.text()?.trim()?.split(", ")!!
            if (headerBlock.first()?.children()?.size!! >1){
                val editBlock: Element? = headerBlock.first()?.child(1)
                val editTime = editBlock?.attr("title")
                message.editTime = editTime
            }
            val name = nameAndTime[0]
            val timeRaw = nameAndTime[1]
            val urlElem = (headerBlock.first())?.getElementsByTag("a")
            val url = urlElem?.first()?.attr("href")
            val id = url?.substring(15,url.length)

            message.authorId = id ?: "myId"
            message.authorName = name
            message.messageTime = timeRaw

            var content = ""
            val contentNodes = messageBlock
                .getElementsByIndexEquals(1)
                .first()
                ?.childNodes()
            for (node in contentNodes!!){
                if (node.nodeName()=="div")
                    break
                content+= if (node.nodeName()== "br") " /n " else node.toString().trim()
            }
            if (content.isNotEmpty()) message.message = content

            val attachments = arrayListOf<Attachment>()
            for (element in attachmentsBlock){
                var url:String? = null
                val attachmentLink: Elements = element.getElementsByClass("attachment__link")
                if (attachmentLink.size>0){
                    url = attachmentLink.first()?.text()?.trim()
                }
                val attachmentType = element.getElementsByClass("attachment__description").first()?.text()?.trim()
                val attachment = Attachment(attachmentType!!, url)
                attachments.add(attachment)
            }
            message.attachments = attachments
            messages.add(message)
        }
        return messages
    }
}