package utils

import model.Attachment
import model.Dialog
import model.Message
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import java.io.File
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

object HtmlParser {
    private lateinit var messages:ArrayList<Message>
    private lateinit var dialog: Dialog

    /**
     * file - директория с файлами сообщений
     */
    fun parseDialogFolder(file: File): Dialog{
        dialog = Dialog();
        messages = arrayListOf();
        val tmp = file.listFiles();
        tmp.sortWith { o1, o2 ->
            val value1 = o1!!.name.filter { it.isDigit() }.toInt();
            val value2 = o2!!.name.filter { it.isDigit() }.toInt();
            value1 - value2;
        }
        tmp.forEach {
            parseVkMessages(it);
        }
        dialog.messages = messages;
        return dialog;
    }

    private fun parseMonth(month:String):Int{
        if (month.startsWith("янва"))
            return 1;
        if (month.startsWith("февра"))
            return 2;
        if (month.startsWith("мар"))
            return 3;
        if (month.startsWith("апре"))
            return 4;
        if (month.startsWith("ма"))
            return 5;
        if (month.startsWith("июн"))
            return 6;
        if (month.startsWith("июл"))
            return 7;
        if (month.startsWith("авг"))
            return 8;
        if (month.startsWith("сен"))
            return 9;
        if (month.startsWith("окт"))
            return 10;
        if (month.startsWith("ноя"))
            return 11;
        if (month.startsWith("дека"))
            return 12;
        return 0;
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
        );
    }

    private fun parseVkMessages(file: File){
        if (!file.name.endsWith(".html")) throw RuntimeException("Not html file")
        val document: Document = try {
            Jsoup.parse(file, "Windows-1251")
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
        val divs: Elements = document.getElementsByClass("item")
        // div in divs
        for (i in 0 until divs.size) {
            val div = divs[i];
            val message = Message();
            val messageBlock: Element? = div.getElementsByClass("message").first()
            val attachmentsBlock: Elements = messageBlock!!.getElementsByClass("attachment")
            val headerBlock: Elements = div.getElementsByClass("message__header")

            val nameAndTime: List<String> = headerBlock.first()?.text()?.trim()?.split(", ")!!
            if (headerBlock.first()?.children()?.size!! >1){
                val editBlock: Element? = headerBlock.first()?.child(1)
                val editTime = editBlock?.attr("title");
                message.editTime = editTime;
            }
            val name = nameAndTime[0]
            val timeRaw = nameAndTime[1]
            val urlElem = (headerBlock.first())?.getElementsByTag("a");
            val url = urlElem?.first()?.attr("href");
            val id = url?.substring(15,url.length);
            if (id!=null && dialog.id.isBlank()){
                dialog.id = id;
                dialog.name = name;
            }
            message.authorId = id ?: "myId"
            message.authorName = name;
            message.messageTime = timeRaw
            //parseDate(timeRaw)

            var content = "";
            val contentNodes = messageBlock
                .getElementsByIndexEquals(1)
                .first()
                ?.childNodes();
            for (node in contentNodes!!){
                if (node.nodeName()=="div")
                    break;
                content+= if (node.nodeName()== "br") " /n " else node.toString().trim();
            }
            if (content.isNotEmpty()) message.message = content;

            val attachments = arrayListOf<Attachment>();
            for (element in attachmentsBlock){
                var url:String? = null;
                val attachmentLink: Elements = element.getElementsByClass("attachment__link");
                if (attachmentLink.size>0){
                    url = attachmentLink.first()?.text()?.trim()
                }
                val attachmentType = element.getElementsByClass("attachment__description").first()?.text()?.trim();
                val attachment = Attachment(attachmentType!!, url)
                attachments.add(attachment)
            }
            message.attachments = attachments;
            messages.add(message)
        }
    }
}