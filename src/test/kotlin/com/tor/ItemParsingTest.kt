package com.tor

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.File
import kotlin.test.*

class ItemParsingTest {
    private var getRezult: Document = Jsoup.parse(File("src/test/resource/item.html"), "UTF-8")
    @Test
    fun `чтение Title`() {
        assertNotNull(getRezult.title())
        assertTrue(getRezult.title().isNotEmpty())
        println("title: ${getRezult.title()}");
    }

    @Test
    fun `все a`() {
        getRezult.select("a[href]").forEach { link ->
            println(" * ${link.tagName()} ${link.attr("abs:href")} (${link.attr("rel")})")
        }
    }

    @Test
    fun `все link`() {
        getRezult.select("link[href]").forEach { link ->
            println(" * ${link.tagName()} ${link.attr("abs:href")} (${link.attr("rel")})")
        }
    }

    @Test
    fun `все link-phone`() {
        getRezult.select("li.link-phone").forEach { link ->
            println(" * ${link.className()}")
        }
    }

    @Test
    fun `получение id телефона`() {
        val str = getRezult.select("li.link-phone").first().classNames().filter { it.contains("id") }.first()
        val sequence = str.subSequence(str.indexOf("'id':'")+6, str.lastIndexOf("'"))
        assertNotNull(sequence)
        assertTrue(sequence.isNotEmpty())
        println(sequence)
    }

    @Test
    fun `get phoneToken`() {
        val phoneToken = getRezult.select("section#body-container>script")
        val html = phoneToken.html()
        assertFalse(html.isEmpty())
        println(html)
        val subSequence = html.subSequence(html.indexOf("'") + 1, html.lastIndexOf("'"))
        println(subSequence)
        assertEquals(getPhoneToken(getRezult), subSequence)
//        getRezult.getElementsByAttributeValueMatching("|phoneToken = '(.*)';|")
    }

}
