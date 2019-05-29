package sitemap

import java.net.URL
import javax.xml.parsers.SAXParserFactory

/**
 * Class that reads all the sitemaps from e-hentai and gets the galleries urls.
 *
 * @author Ignacio Slater Muñoz
 */
class EHSitemapParser {
    var sitemapUrls: List<String>
        private set

    init {
        val factory = SAXParserFactory.newInstance()
        val saxParser = factory.newSAXParser()
        val sitemapIndex = URL("https://xml.e-hentai.org/sitemap_index.xml").openStream()
        val handler = SitemapHandler()

        saxParser.parse(sitemapIndex, handler)
        sitemapUrls = handler.urlList
    }
}