package sitemap

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class EHSitemapParserTest {
    private lateinit var parser: EHSitemapParser

    @BeforeEach
    fun setUp() {
        parser = EHSitemapParser()
    }

    @Test
    fun getSitemapUrls() {
        for (url in parser.sitemapUrls) {
            assertTrue(Regex("https://e-hentai.org/sitemap*.\\.xml.gz").matches(url))
        }
    }
}