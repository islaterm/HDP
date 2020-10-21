import gallery.EHGallery
import khttp.post
import sitemap.EHSitemapParser

fun main() {
    val parser = EHSitemapParser()
    parser.parseSitemaps()

    val randomUrl = parser.galleriesURL.random()

    val (galleryId, galleryToken) = randomUrl.replace("https://e-hentai.org/g/", "").split("/")
    val query = mapOf(
        "method" to "gdata",
        "gidlist" to listOf(listOf(galleryId, galleryToken)),
        "namespace" to 1
    )
    val response = post("https://api.e-hentai.org/api.php", json = query)
    val gMetadata = response.jsonObject.getJSONArray("gmetadata").toList()
    for (entryMetadata in gMetadata) {
        val gallery = EHGallery(entryMetadata as Map<*, *>)
        println(gallery.toTSV())
    }
}