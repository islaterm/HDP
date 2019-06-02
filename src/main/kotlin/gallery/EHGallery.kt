package gallery

import com.fasterxml.jackson.databind.ObjectMapper

//region keys of the json dictionary
const val ID_KEY = "gid"
const val TOKEN_KEY = "token"
const val ARCHIVER_KEY = "archiver_key"
const val TITLE_KEY = "title"
const val TITLE_JPN_KEY = "title_jpn"
const val CATEGORY_KEY = "category"
const val THUMBNAIL_KEY = "thumb"
const val UPLOADER_KEY = "uploader"
const val POSTED_KEY = "posted"
const val FILECOUNT_KEY = "filecount"
const val FILESIZE_KEY = "filesize"
const val EXPUGNED_KEY = "expunged"
const val RATING_KEY = "rating"
const val TORRENT_COUNT_KEY = "torrentcount"
const val TAGS_KEY = "tags"
//endregion

/**
 * This class contains the metadata of a gallery of e-hentai.org
 *
 * @constructor initiates the values of the gallery from a json string
 * @param jsonString the string representing the gallery data
 * @author [Ignacio Slater Muñoz](islaterm@gmail.com)
 */
class EHGallery(jsonString: String) {
    val id: Int
    val token: String
    val url: String
    val archiverKey: String
    val tags: List<String>
    val torrentCount: Int
    val rating: Double
    val expugned: Boolean
    val fileSize: Int
    val fileCount: Int
    val posted: Int
    val uploader: String
    val thumbnailLink: String
    val category: String
    val titleJpn: String
    val title: String

    init {
        val jsonMap = ObjectMapper().readValue(jsonString, HashMap::class.java)
        id = jsonMap[ID_KEY] as Int
        token = jsonMap[TOKEN_KEY] as String
        url = "https://e-hentai.org/g/$id/$token/"
        archiverKey = jsonMap[ARCHIVER_KEY] as String
        title = jsonMap[TITLE_KEY] as String
        titleJpn = jsonMap[TITLE_JPN_KEY] as String
        category = jsonMap[CATEGORY_KEY] as String
        thumbnailLink = jsonMap[THUMBNAIL_KEY] as String
        uploader = jsonMap[UPLOADER_KEY] as String
        posted = (jsonMap[POSTED_KEY] as String).toInt()
        fileCount = (jsonMap[FILECOUNT_KEY] as String).toInt()
        fileSize = jsonMap[FILESIZE_KEY] as Int
        expugned = jsonMap[EXPUGNED_KEY] as Boolean
        rating = (jsonMap[RATING_KEY] as String).toDouble()
        torrentCount = (jsonMap[TORRENT_COUNT_KEY] as String).toInt()
        val auxTaglist = mutableListOf<String>()
        for (tag in jsonMap[TAGS_KEY] as List<*>) {
            auxTaglist.add(tag as String)
        }
        tags = auxTaglist.toList()
    }

    /**
     * Returns a pair of strings with a tsv version of this gallery
     */
    fun toTSV() = Pair(
        // Single column containing the basic gallery data
        "$id\t$token\t$archiverKey\t$title\t$titleJpn\t$category\t$thumbnailLink\t$uploader\t$posted\t" +
                "$fileCount\t$fileSize\t$expugned\t$rating\t$torrentCount\n",
        // Maps each tag so that every column is formed of the gallery id a tag category and the respective tag
        "${(tags.map {
            val aux = it.split(":") // Subtags are defined by ':'
            if (aux.size > 1) { // When there are subtags, the middle column indicates the tag category
                "$id\t${aux[0]}\t${aux[1]}"
            } else {    // When there are no subtags, the middle column stays empty (it has no category)
                "$id\t\t$it"
            }
        }).joinToString(System.lineSeparator())}${System.lineSeparator()}"
    )
}
