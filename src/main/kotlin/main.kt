import sitemap.SitemapParser

fun main() {
    val parser = SitemapParser()
    for (url in parser.sitemapUrls) {
        println(url)
    }
    /*val randomUrl = handler.urlList.random()

    val (galleryId, galleryToken) = randomUrl.replace("https://e-hentai.org/g/", "").split("/")
    val data = mapOf(
        "method" to "gdata",
        "gidlist" to listOf(listOf(galleryId, galleryToken)),
        "namespace" to 1
    )
    val response = post("https://api.e-hentai.org/api.php", json = data)
    val result = ObjectMapper().readValue(response.text, HashMap::class.java)
    val mdataList = result["gmetadata"] as List<*>
    println(randomUrl)
    for (mdata in mdataList) {
        val galleryData = mdata as Map<*, *>
        val tags = galleryData["tags"] as List<*>
        for (tag in tags) {
            println(tag)
        }
    }*/
    //if (result != null)
    //for (elem in result.metaData)
    //println(elem)
}