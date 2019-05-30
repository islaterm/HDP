package sitemap

import org.xml.sax.Attributes
import org.xml.sax.helpers.DefaultHandler

/**
 * This class handles the general structure of a sitemap.
 *
 * @author [Ignacio Slater Muñoz](islaterm@gmail.com)
 */
class SitemapHandler : DefaultHandler() {
    private lateinit var data: StringBuilder    // text contained inside a loc tag
    private var inLoc = false   // flag that indicates if the handler is currently inside a loc tag
    private val urls = mutableListOf<String>()  // Internal representation of the url list

    /** List of the urls parsed by the handler  */
    val urlList get() = urls.toList()


    override fun startElement(uri: String?, localName: String?, qName: String?, attributes: Attributes?) {
        if (qName.equals("loc")) inLoc = true
        data = StringBuilder()
    }

    override fun endElement(uri: String?, localName: String?, qName: String?) {
        if (qName.equals("loc")) {
            urls.add(data.toString())
            inLoc = false
        }
    }

    override fun characters(ch: CharArray?, start: Int, length: Int) {
        if (inLoc)
            data.append(ch?.let { String(it, start, length) })
    }

    /** Resets the handler  */
    fun reset() {
        urls.clear()
    }
}