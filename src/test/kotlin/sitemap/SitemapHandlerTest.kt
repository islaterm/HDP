package sitemap

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File
import java.nio.file.Paths
import javax.xml.parsers.SAXParser
import javax.xml.parsers.SAXParserFactory

internal class SitemapHandlerTest {


    private lateinit var handler: SitemapHandler
    private lateinit var saxParser: SAXParser
    private lateinit var testXML: File

    private val testPath = Paths.get("src", "test", "resources", "test.xml")

    @BeforeEach
    fun setUp() {
        val factory = SAXParserFactory.newInstance()
        saxParser = factory.newSAXParser()
        handler = SitemapHandler()
        testXML = File(testPath.toUri())
    }

    @Test
    fun getUrlList() {
        assertTrue(handler.urlList.isEmpty())   // La lista comienza vacía
        // Luego de parsear los elementos, la lista debería tener tamaño 2
        saxParser.parse(testXML, handler)
        assertEquals(2, handler.urlList.size)
        assertTrue(handler.urlList.containsAll(listOf("loc_1", "loc_2"))) // Se ve que tenga los elementos esperados
        // Si tiene tamaño 2, y los elementos esperados, entonces el parser efectivamente ignora los tags que no sean
        // loc
    }

    @Test
    fun reset() {
        saxParser.parse(testXML, handler)
        assertTrue(handler.urlList.isNotEmpty())    // Vemos que la lista de urls no esté vacía luego de parsear
        handler.reset()
        assertTrue(handler.urlList.isEmpty())   // We check that te url list is empty after reseting the handler
    }
}