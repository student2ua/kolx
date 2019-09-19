package com.tor

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.math.BigInteger
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter


fun main(args: Array<String>) {
    val links = LinkedHashSet<ListItem>()

//    println("------------------- detailsLink:")
//    getRezult.select(".detailsLink").map{it.attr("href")}.forEach { println(it) }
    links.addAll(
        parseolx("https://www.olx.ua/nedvizhimost/kvartiry-komnaty/prodazha-kvartir-komnat/kvartira/kha/?search%5Bfilter_float_number_of_rooms%3Afrom%5D=1&currency=USD")
    )
    for (i in 2..36) {
        links.addAll(
            parseolx("https://www.olx.ua/nedvizhimost/kvartiry-komnaty/prodazha-kvartir-komnat/kvartira/kha/?search%5Bfilter_float_number_of_rooms%3Afrom%5D=1&page=$i&currency=USD")
        )
    }

    /* println("------------------- data-id:")
     getRezult.select("table[data-id]").forEach {
         links.add(ListItem(
             it.attr("data-id").toBigInteger(),
             it.select("""p[class^="price"]""").text().trimEnd('$').replace(" ","").toInt(),
             it.select("tbody>tr>td.bottom-cell>div.space.rel>p.lheight16>small.breadcrumb.x-normal>span")[0].text(),
             it.select("tbody>tr>td.title-cell>div.space.rel>h3.lheight22.margintop5").text(),
             it.select("tbody>tr>td.bottom-cell>div.space.rel>p.lheight16>small.breadcrumb.x-normal>span")[1].text()
             ))
 //        println("cssSelector=" +                it.cssSelector())

     }*/
    /* getRezult.getElementsByTag("a").forEach{
         println(it.text()+"/ "+it.html())
        }*/
    /* println("------------------- href:")
     getRezult.select("""a[href^="https://www.olx.ua/obyavlenie/"]""").forEach { link ->
         println(" * a: ${link.attr("abs:href")}  (${link.text()})");
         links.add(Pair(link.attr("abs:href"), link.text()))
     }*/
    /*  getRezult.select("""p[class^="price"]""").forEach { link ->
          println(" * price: ${link.text()}");
          links.add(Pair(link.attr("abs:href"), link.text()))
      }*/
    /*   val getRezult = Jsoup.parse(File("C:\\Users\\Admin\\IdeaProjects\\kolx\\src\\resource\\in.html"), "UTF-8");
       *//*  Jsoup.connect("https://www.olx.ua/nedvizhimost/kvartiry-komnaty/prodazha-kvartir-komnat/kvartira/kha/?search%5Bfilter_float_number_of_rooms%3Afrom%5D=1")
          .userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.21 (KHTML, like Gecko) Chrome/19.0.1042.0 Safari/535.21")
          .timeout(10_000)
          .get();*//*
    println("title: ${getRezult.title()}");
    println("searchCriteria: ${getRezult.select("div#searchCriteria").text()}");
    println("searchCriteriaUid: ${getRezult.select("div#searchCriteriaUid").text()}");
    getRezult.select("a.lheight24").forEach {
        println(it.attr("abs:href"))
    }*/
    links.sortedBy { it.price }.forEach { println(it.toTSV()) }
    println(links.size)
}

private fun parseolx(url: String): Set<ListItem> {
    val links = LinkedHashSet<ListItem>()
    val getRezult = Jsoup.connect(url)
        .userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.21 (KHTML, like Gecko) Chrome/19.0.1042.0 Safari/535.21")
        .timeout(10_000)
//        .referrer("http://www.google.com")
        .get();
    getRezult.select("table[data-id]").forEach {
        val datetimeStr =
            it.select("tbody>tr>td.bottom-cell>div.space.rel>p.lheight16>small.breadcrumb.x-normal>span")[1].text()

        when {
            datetimeStr.contains("Сегодня") || datetimeStr.contains("Вчера") -> links.add(
                ListItem(
                    it.attr("data-id").toBigInteger(),
                    it.select("""p[class^="price"]""").text().trimEnd('$').replace(" ", "").toIntOrNull(),
                    it.select("tbody>tr>td.bottom-cell>div.space.rel>p.lheight16>small.breadcrumb.x-normal>span")[0].text(),
                    it.select("tbody>tr>td.title-cell>div.space.rel>h3.lheight22.margintop5").text(),
                    datetimeStr.getLocalDateTime()
                )
            )
        }
    }
    println(url + "," + links.size)
    return links
}

fun String.getLocalDateTime(): LocalDateTime {
    val from = this
    var string = "Сегодня"
    val str1 = "Сегодня"
    val str2 = "Вчера"
    var now = LocalDateTime.now()
    if (from.contains(string)) {
        string = str1
    } else {
        string = str2
        now = now.minusDays(1)
    }
    val timeStr = from.substring(from.lastIndexOf(string) + string.length, from.length)
    val timeL  :LocalTime = try {
        LocalTime.parse(timeStr.trim(), DateTimeFormatter.ISO_TIME)
    } catch (e: Exception) {
       System.err.println(from + e)
        LocalTime.now()
    }
    return now.withHour(timeL.hour).withMinute(timeL.minute)
}

fun getPhoneToken(doc: Document): CharSequence {
    val phoneToken = doc.select("section#body-container>script")
    val html = phoneToken.html()
    return html.subSequence(html.indexOf("'") + 1, html.lastIndexOf("'"))
}

fun getPhoneId(doc: Document): CharSequence {
    val str = doc.select("li.link-phone").first().classNames().filter { it.contains("id") }.first()
    return str.subSequence(str.indexOf("'id':'") + 6, str.lastIndexOf("'"))
}

private fun parseUserIDinURL(url: String): String = """https?://.*ID(.*?)\.html.*""".toRegex().findAll(url).joinToString()

private fun parsePhones(responseBody: String): String =
    """([\d\+\(][\d\s\-\(\)]+\d)""".toRegex().findAll(responseBody).joinToString()

data class ListItem(
    val id: BigInteger,
    val price: Int?,
    val district: String,
    val header: String,
    val time: LocalDateTime
) {
    fun toTSV(): String {
        return id.toString() + "\t" + district + "\t" + header + "\t" + price + "\t" + time
    }
}