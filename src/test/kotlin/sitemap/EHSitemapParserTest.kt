package sitemap

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class EHSitemapParserTest {
    private lateinit var parser: EHSitemapParser

    @BeforeAll
    fun initialSetUp() {
        parser = EHSitemapParser()
    }

    @Test
    fun getSitemapUrls() {
        for (url in parser.sitemapUrls) {
            assertTrue(Regex("https://e-hentai.org/sitemap*.\\.xml.gz").matches(url))
        }
    }

    @Test
    fun parseSitemaps() {
        assertTrue(parser.galleriesURL.isEmpty())
        parser.parseSitemaps()
        assertTrue(parser.galleriesURL.isNotEmpty())
        for (url in parser.galleriesURL) {
            assertTrue(Regex("https://e-hentai.org/g/.*").matches(url), "Wrong url: $url")
        }
    }
}