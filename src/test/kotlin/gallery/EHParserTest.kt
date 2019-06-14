package gallery

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File
import java.nio.file.Paths

const val TEST_GALLERY_1 = "{\n" +
        " \"gmetadata\": [\n" +
        "   {\n" +
        "     \"gid\": 618395,\n" +
        "     \"token\": \"0439fa3666\",\n" +
        "     \"archiver_key\": \"403565--d887c6dfe8aae79ed0071551aa1bafeb4a5ee361\",\n" +
        "     \"title\": \"(Kouroumu 8) [Handful☆Happiness! (Fuyuki Nanahara)] TOUHOU GUNMANIA A2 (Touhou Project)\",\n" +
        "     \"title_jpn\": \"(紅楼夢8) [Handful☆Happiness! (七原冬雪)] TOUHOU GUNMANIA A2 (東方Project)\",\n" +
        "     \"category\": \"Non-H\",\n" +
        "     \"thumb\": \"https://ehgt.org/14/63/1463dfbc16847c9ebef92c46a90e21ca881b2a12-1729712-4271-6032-jpg_l.jpg\",\n" +
        "     \"uploader\": \"avexotsukaai\",\n" +
        "     \"posted\": \"1376143500\",\n" +
        "     \"filecount\": \"20\",\n" +
        "     \"filesize\": 51210504,\n" +
        "     \"expunged\": false,\n" +
        "     \"rating\": \"4.43\",\n" +
        "     \"torrentcount\": \"0\",\n" +
        "     \"tags\": [\n" +
        "       \"parody:touhou project\",\n" +
        "       \"group:handful happiness\",\n" +
        "       \"artist:nanahara fuyuki\",\n" +
        "       \"full color\",\n" +
        "       \"artbook\"\n" +
        "     ]\n" +
        "   }\n" +
        " ]\n" +
        "}"

const val TEST_GALLERY_2 = "{\"gmetadata\": [{\n" +
        "  \"gid\": 352960,\n" +
        "  \"thumb\": \"https://ehgt.org/be/44/be4486a100360c77be054bed3e17979b0bd91254-233546-560-420-jpg_l.jpg\",\n" +
        "  \"rating\": \"2.89\",\n" +
        "  \"filesize\": 309998096,\n" +
        "  \"title\": \"[yagesawa bunko] RRXXクレ○ッツ警部総集編\",\n" +
        "  \"torrentcount\": \"0\",\n" +
        "  \"token\": \"8b8f39991b\",\n" +
        "  \"posted\": \"1302267407\",\n" +
        "  \"tags\": [\n" +
        "    \"parody:rumble roses\",\n" +
        "    \"character:dixie clemets\",\n" +
        "    \"female:bikini\",\n" +
        "    \"female:bondage\",\n" +
        "    \"female:collar\",\n" +
        "    \"female:small breasts\",\n" +
        "    \"female:swimsuit\",\n" +
        "    \"mosaic censorship\"\n" +
        "  ],\n" +
        "  \"archiver_key\": \"433462--a5ae5e69b9b9e5f2f3c9f658f7b39ea8c4c91339\",\n" +
        "  \"filecount\": \"443\",\n" +
        "  \"expunged\": false,\n" +
        "  \"uploader\": \"martin4096\",\n" +
        "  \"title_jpn\": \"\",\n" +
        "  \"category\": \"Cosplay\"\n" +
        "}]}"

internal class EHParserTest {
    private val testGallery1 = EHGallery(TEST_GALLERY_1).toTSV()
    private val testGallery2 = EHGallery(TEST_GALLERY_2).toTSV()

    private lateinit var parser: EHParser
    private lateinit var galleryTestFile: File
    private lateinit var tagsTestFile: File

    private val galleryTestPath = Paths.get("resources", "OutputTestGalleries.tsv")
    private val tagsTestPath = Paths.get("resources", "OutputTestTags.tsv")

    @BeforeEach
    fun setUp() {
        parser = EHParser()
        galleryTestFile = File(galleryTestPath.toUri())
        tagsTestFile = File(tagsTestPath.toUri())
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
        assertEquals(testGallery1, parsedResult)
        // Try parsing multiple urls
        parsedResult = parser.parseToTSV(
            false,
            "https://e-hentai.org/g/618395/0439fa3666/",
            "https://e-hentai.org/g/352960/8b8f39991b/"
        )
        checkEmptyFiles()
        val expectedGData = testGallery1.first + System.lineSeparator() + testGallery2.first
        val expectedTags = testGallery1.second + System.lineSeparator() + testGallery2.second
        assertEquals(expectedGData, parsedResult.first)
        assertEquals(expectedTags, parsedResult.second)
        TODO("parse urls with saving")
    }

    /**
     * Checks if the test files are empty.
     */
    private fun checkEmptyFiles() {
        assertTrue(galleryTestFile.readLines().isEmpty())
        assertTrue(tagsTestFile.readLines().isEmpty())
    }
}