package com.tor

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import java.io.File
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class MainKtTest {

    private var getRezult: Document = Jsoup.parse(File("src/test/resource/listOlx.html"), "UTF-8")
    @BeforeTest
    fun init() {
//    getRezult=Jsoup.parse(File("C:\\Users\\Admin\\IdeaProjects\\kolx\\src\\resource\\in.html"), "UTF-8")
        if (getRezult == null) getRezult =
            Jsoup.connect("https://www.olx.ua/nedvizhimost/kvartiry-komnaty/prodazha-kvartir-komnat/kvartira/kha/?search%5Bfilter_float_number_of_rooms%3Afrom%5D=1&currency=USD")
                .userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.21 (KHTML, like Gecko) Chrome/19.0.1042.0 Safari/535.21")
                .timeout(10_000)
                .get();
    }

    @Test
    fun `все detailsLink`() {
        val map = getRezult.select(".detailsLink").map { it.attr("href") }
        assertNotNull(map)
        assertTrue(map.isNotEmpty())
        map.forEach { println(it) }
    }

    @Test
    fun `все obyavlenie`() {
        var links: MutableList<Element> = ArrayList<Element>();
        getRezult.select("""a[href^="https://www.olx.ua/obyavlenie/"]""").forEach { link ->
            //        println(" * a: ${link.attr("abs:href")}  (${link.text()})");
            links.add(link)
        }
        assertNotNull(links)
        assertTrue(links.isNotEmpty())
        links.forEach { println(it) }
    }

    @Test
    fun `все price`() {
        var links: MutableList<Element> = ArrayList<Element>();
        getRezult.select("""p[class^="price"]""").forEach { link ->
            println(" * a: ${link.attr("abs:href")}  (${link.text()})");
            links.add(link)
        }
        assertNotNull(links)
        assertTrue(links.isNotEmpty())
        links.forEach { println(it) }
    }

    @Test
    fun `все src`() {
        var links: MutableList<Element> = ArrayList<Element>();
        getRezult.select("[src]").forEach { src ->
            if (src.tagName().equals("img"))
                println(
                    " * ${src.tagName()}: ${src.attr("abs:src")} ${src.attr("width")}x${src.attr("height")} (${src.attr(
                        "alt"
                    )})"
                );
            else
                println(" * ${src.tagName()}: ${src.attr("abs:src")}");
        }
        /*assertNotNull(links)
        assertTrue(links.isNotEmpty())
        links.forEach { println(it) }*/
    }

    @Test
    fun `все link`() {
        getRezult.select("link[href]").forEach { link ->
            println(" * ${link.tagName()} ${link.attr("abs:href")} (${link.attr("rel")})")
        }
    }

    @Test
    fun `Добавлено|Опубликовано с мобильного`() {
        println(getRezult.select("span:matches((Добавлено|Опубликовано с мобильного))"))
    }

    @Test
    fun `превращение в дату`() {
        val from = "Сегодня 15:29"
//        val current = LocalDateTime.now()
        val current = Date();
        val string = "Сегодня"
        assertTrue(from.contains(string, true))
        val timeStr = from.substring(from.lastIndexOf(string) + string.length, from.length)
        println(timeStr)
        val timeL = LocalTime.parse(timeStr.trim(), DateTimeFormatter.ISO_TIME)
//        val time = Time.valueOf(timeStr)
        println(timeL)
        var localDate = LocalDateTime.now().withHour(timeL.hour).withMinute(timeL.minute)
//        current.time = time.toNanoOfDay()
        println(localDate)

    }
}