package sitemap

import org.apache.commons.io.FileUtils
import java.io.File
import java.net.URL
import javax.xml.parsers.SAXParserFactory

/**
 * Class that reads all the sitemaps from e-hentai and gets the galleries urls.
 *
 * @author Ignacio Slater Muñoz
 */
class SitemapParser {
    var sitemapUrls: List<String>
        private set

    init {
        val factory = SAXParserFactory.newInstance()
        val saxParser = factory.newSAXParser()
        val sitemapIndexUrl = URL("https://xml.e-hentai.org/sitemap_index.xml")
        val sitemapIndex = File("SitemapIndex.xml")
        val handler = SitemapHandler()
        FileUtils.copyURLToFile(sitemapIndexUrl, sitemapIndex)

        saxParser.parse(sitemapIndex, handler)
        sitemapUrls = handler.urlList
        sitemapIndex.delete()
    }
}