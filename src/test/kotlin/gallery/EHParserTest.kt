package gallery

import com.fasterxml.jackson.databind.ObjectMapper
import khttp.post
import org.json.JSONArray
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File
import java.nio.file.Paths


internal class EHParserTest {
    private lateinit var testGallery1: Pair<String, String>
    private lateinit var testGallery2: Pair<String, String>

    private lateinit var parser: EHParser
    private lateinit var galleryTestFile: File
    private lateinit var tagsTestFile: File

    private val galleryTestPath = Paths.get("src", "test", "resources", "OutputTestGalleries.tsv")
    private val tagsTestPath = Paths.get("src", "test", "resources", "OutputTestTags.tsv")

    @BeforeEach
    fun setUp() {
        galleryTestFile = File(galleryTestPath.toUri())
        tagsTestFile = File(tagsTestPath.toUri())
        galleryTestFile.createNewFile()
        tagsTestFile.createNewFile()
        parser = EHParser(galleryTestFile, tagsTestFile)
        testGallery1 = EHGallery(
            ObjectMapper().readValue(
                initGallery("https://e-hentai.org/g/618395/0439fa3666/"),
                HashMap::class.java
            )
        ).toTSV()
        testGallery2 = EHGallery(
            ObjectMapper().readValue(
                initGallery("https://e-hentai.org/g/1348751/bdf3afc737/"),
                HashMap::class.java
            )
        ).toTSV()
    }

    @AfterEach
    fun tearDown() {
        galleryTestFile.delete()
        tagsTestFile.delete()
    }

    @Test
    fun parseToTSVTest() {
        // The files start empty
        checkEmptyFiles()
        // Try parsing 1 url
        var parsedResult = parser.parseToTSV(false, "https://e-hentai.org/g/618395/0439fa3666/")
        // If save is set to false, the files shouldn't be written
        checkEmptyFiles()
        // Checks if the url was parsed correctly
        testParsedUrl(testGallery1, parsedResult)

        // Try parsing multiple urls
        parsedResult = parser.parseToTSV(
            false,
            "https://e-hentai.org/g/618395/0439fa3666/",
            "https://e-hentai.org/g/1348751/bdf3afc737/"
        )
        checkEmptyFiles()
        //  Checks if the urls where parsed correctly
        val expectedResult = Pair(
            "${testGallery1.first}${testGallery2.first}",
            "${testGallery1.second}${testGallery2.second}"
        )
        testParsedUrl(expectedResult, parsedResult)

        // Try parsing and saving multiple urls
        parser.parseToTSV(
            true,
            "https://e-hentai.org/g/618395/0439fa3666/",
            "https://e-hentai.org/g/1348751/bdf3afc737/"
        )
        //  Checks if the files where written
        assertTrue(galleryTestFile.readLines().isNotEmpty())
        assertTrue(tagsTestFile.readLines().isNotEmpty())
        // Checks if the files where written correctly
        parsedResult = Pair(galleryTestFile.readText(), tagsTestFile.readText())
        testParsedUrl(expectedResult, parsedResult)
    }

    /**
     * Checks if the urls were parsed correctly
     */
    private fun testParsedUrl(expected: Pair<String, String>, actual: Pair<String, String>) {
        val expectedMetadata = tsvToList(expected.first)
        val parsedMetadata = tsvToList(actual.first)

        assertEquals(expectedMetadata.size, parsedMetadata.size)
        assertArrayEquals(expectedMetadata.toTypedArray(), parsedMetadata.toTypedArray())

        val expectedTags = tsvToList(expected.second)
        val parsedTags = tsvToList(actual.second)
        assertArrayEquals(expectedTags.toTypedArray(), parsedTags.toTypedArray())
    }

    /**
     * Transforms a tsv string to a list
     */
    private fun tsvToList(tsv: String): List<String> {
        val valuesList = mutableListOf<String>()
        for (line in tsv.trim().split(Regex("[\r\n]+")))
            valuesList.addAll(line.split("\t"))
        return valuesList.sorted()
    }

    /**
     * Checks if the test files are empty.
     */
    private fun checkEmptyFiles() {
        assertTrue(galleryTestFile.readLines().isEmpty())
        assertTrue(tagsTestFile.readLines().isEmpty())
    }

    private fun initGallery(url: String): String {
        val (galleryId, galleryToken) = url.replace("https://e-hentai.org/g/", "").split("/")
        val data = mapOf(
            "method" to "gdata",
            "gidlist" to listOf(listOf(galleryId, galleryToken)),
            "namespace" to 1
        )
        val response = post("https://api.e-hentai.org/api.php", json = data)
        return (response.jsonObject["gmetadata"] as JSONArray)[0].toString()
    }
}