package gallery

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File
import java.nio.file.Paths

internal class EHGalleryTest {

    private lateinit var gallery: EHGallery

    private val testJsonGalleryPath = Paths.get("src", "test", "resources", "TestGallery.json")
    @BeforeEach
    fun setUp() {
        val jsonString = File(testJsonGalleryPath.toUri()).readText()  // Reads a json file with test data for a gallery
        gallery = EHGallery(jsonString) // Creates the gallery from the json string
    }

    @Test
    fun constructorTest() {
        assertEquals("https://e-hentai.org/g/618395/0439fa3666/", gallery.url)
        assertEquals(618395, gallery.id)
        assertEquals("0439fa3666", gallery.token)
        assertEquals("403565--d887c6dfe8aae79ed0071551aa1bafeb4a5ee361", gallery.archiverKey)
        assertEquals(
            "(Kouroumu 8) [Handful☆Happiness! (Fuyuki Nanahara)] TOUHOU GUNMANIA A2 (Touhou Project)",
            gallery.title
        )
        assertEquals("(紅楼夢8) [Handful☆Happiness! (七原冬雪)] TOUHOU GUNMANIA A2 (東方Project)", gallery.titleJpn)
        assertEquals("Non-H", gallery.category)
        assertEquals(
            "https://ehgt.org/14/63/1463dfbc16847c9ebef92c46a90e21ca881b2a12-1729712-4271-6032-jpg_l.jpg",
            gallery.thumbnailLink
        )
        assertEquals("avexotsukaai", gallery.uploader)
        assertEquals(1376143500, gallery.posted)
        assertEquals(20, gallery.fileCount)
        assertEquals(51210504, gallery.fileSize)
        assertFalse(gallery.expugned)
        assertEquals(4.43, gallery.rating)
        assertEquals(0, gallery.torrentCount)
        // Checks if all the expected tags are in the gallery list, and compares the sizes of both
        assertTrue(
            gallery.tags.containsAll(
                listOf(
                    "parody:touhou project",
                    "group:handful happiness",
                    "artist:nanahara fuyuki",
                    "full color",
                    "artbook"
                )
            )
        )
        assertEquals(5, gallery.tags.size)
    }

    @Test
    fun serializeTest() {

    }
}