package cl.ravenhill.hentai.dp.gf.gallery

// region : keys of the json dictionary
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
// endregion

/**
 * This class contains the metadata of a cl.ravenhill.hentaidp.gallery of e-hentai.org
 *
 * @constructor initiates the values of the cl.ravenhill.hentaidp.gallery from a json string
 * @param jsonMap the string representing the cl.ravenhill.hentaidp.gallery data
 * @author [Ignacio Slater Muñoz](islaterm@gmail.com)
 */
class EHGallery(jsonMap: Map<*, *>) {
  val id = jsonMap[ID_KEY] as Int
  val token = jsonMap[TOKEN_KEY] as String
  val archiverKey = jsonMap[ARCHIVER_KEY] as String
  val torrentCount = (jsonMap[TORRENT_COUNT_KEY] as String).toInt()
  val rating = (jsonMap[RATING_KEY] as String).toDouble()
  val expugned = jsonMap[EXPUGNED_KEY] as Boolean
  val fileSize = jsonMap[FILESIZE_KEY] as Int
  val fileCount = (jsonMap[FILECOUNT_KEY] as String).toInt()
  val posted = (jsonMap[POSTED_KEY] as String).toInt()
  val uploader = jsonMap[UPLOADER_KEY] as String
  val thumbnailLink = jsonMap[THUMBNAIL_KEY] as String
  val category = jsonMap[CATEGORY_KEY] as String
  val titleJpn = jsonMap[TITLE_JPN_KEY] as String
  val title = jsonMap[TITLE_KEY] as String
  val url = "https://e-hentai.org/g/$id/$token/"
  val tags = List((jsonMap[TAGS_KEY] as List<*>).size) {
    (jsonMap[TAGS_KEY] as List<*>)[it] as String
  }

  /**
   * Returns a pair of strings with a tsv version of this cl.ravenhill.hentaidp.gallery
   */
  fun toTSV() = Pair(
    // Single column containing the basic cl.ravenhill.hentaidp.gallery data
    "${
      ("$id\t" +
          "$token\t" +
          "$archiverKey\t" +
          "$title\t" +
          "$titleJpn\t" +
          "$category\t" +
          "$thumbnailLink\t" +
          "$uploader\t" +
          "$posted\t" +
          "$fileCount\t" +
          "$fileSize\t" +
          "$expugned\t" +
          "$rating\t" +
          "$torrentCount").replace("\n", " ")
    }\n",
    // Maps each tag so that every column is formed of the cl.ravenhill.hentaidp.gallery id a tag category and the respective tag
    "${
      (tags.map {
        val aux = it.split(":") // Subtags are defined by ':'
        if (aux.size > 1) { // When there are subtags, the middle column indicates the tag category
          "$id\t${aux[0]}\t${aux[1]}"
        } else {    // When there are no subtags, the middle column stays empty (it has no category)
          "$id\t\t$it"
        }
      }).joinToString(System.lineSeparator())
    }${System.lineSeparator()}"
  )
}
