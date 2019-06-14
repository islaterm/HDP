import khttp.post
import sitemap.EHSitemapParser

fun main() {
    val parser = EHSitemapParser()
    parser.parseSitemaps()

    val randomUrl = parser.galleriesURL.random()

    val (galleryId, galleryToken) = randomUrl.replace("https://e-hentai.org/g/", "").split("/")
    val data = mapOf(
        "method" to "gdata",
        "gidlist" to listOf(listOf(galleryId, galleryToken)),
        "namespace" to 1
    )
    val response = post("https://api.e-hentai.org/api.php", json = data)
    println(response.jsonObject.toString(2))
}