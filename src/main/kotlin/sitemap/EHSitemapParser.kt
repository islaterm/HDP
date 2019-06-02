package sitemap

import java.io.InputStream
import java.net.URL
import java.util.zip.GZIPInputStream
import javax.xml.parsers.SAXParser
import javax.xml.parsers.SAXParserFactory

/**
 * Class that reads all the sitemaps from e-hentai and gets the galleries urls.
 *
 * @author Ignacio Slater Muñoz
 */
class EHSitemapParser {
    /** List of links to the galleries */
    var galleriesURL = listOf<String>()
        private set
    /** List with the link to the sitemaps  */
    var sitemapUrls: List<String>
        private set
    private val saxParser: SAXParser
    private val handler: SitemapHandler

    init {
        System.setProperty("http.agent", "")
        val factory = SAXParserFactory.newInstance()
        saxParser = factory.newSAXParser()
        val sitemapIndex = URL("https://xml.e-hentai.org/sitemap_index.xml").openStream()
        handler = SitemapHandler()

        saxParser.parse(sitemapIndex, handler)
        sitemapUrls = handler.urlList
        handler.reset()
    }

    /**
     * Retrieves all the gallery links from the sitemaps
     */
    fun parseSitemaps() {
        for (url in sitemapUrls) {
            val sitemapURL = URL(url)
            val urlConnection = sitemapURL.openConnection() // Opens a connection to the url
            urlConnection.setRequestProperty(   // Sets a User-Agent (to avoid error 403)
                "User-Agent",
                "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:47.0) Gecko/20100101 Firefox/47.0"
            )
            val gzipSitemap = GZIPInputStream(urlConnection.getInputStream()) as InputStream
            saxParser.parse(gzipSitemap, handler)
            // Get the parsed urls and filter the ones that are not galleries
            galleriesURL = handler.urlList.filter { it.startsWith("https://e-hentai.org/g/") }
        }
    }
}