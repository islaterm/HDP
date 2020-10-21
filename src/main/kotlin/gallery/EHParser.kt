package gallery

import com.fasterxml.jackson.databind.ObjectMapper
import khttp.post
import khttp.responses.Response
import java.io.File

/**
 * Parser for the gallery data of e-hentai.org
 *
 * @constructor sets the files to save the parsed data
 * @param galleriesDataFile file to save the galleries metadata
 * @param galleriesTagsFile file to save the galleries tags
 * @author [Ignacio Slater Muñoz](islaterm@gmail.com)
 */
class EHParser(
  private val galleriesDataFile: File? = null,
  private val galleriesTagsFile: File? = null
) {

  /**
   * Parses a list of urls to tsv format
   *
   * @param save if the data should be saved to files
   * @param urls links to the galleries to parse
   * @return a pair containing te galleries metadata and its tags
   */
  fun parseToTSV(save: Boolean, vararg urls: String): Pair<String, String> {
    var urlsToParse = urls.toList()
    val keys = mutableListOf<List<String>>()
    val parsedMetadata = StringBuilder()
    val parsedTags = StringBuilder()
    var response: Response
    while (urlsToParse.isNotEmpty()) {
      // Takes the first 25 urls and creates a list of pairs (id, token)
      urlsToParse.take(25).forEach {
        keys.add(it.replace("https://e-hentai.org/g/", "").split("/"))
      }
      val data = mapOf(
        "method" to "gdata",
        "gidlist" to keys,
        "namespace" to 1
      )
      response = post("https://api.e-hentai.org/api.php", json = data)

      val galleries =
        ObjectMapper().readValue("${response.jsonObject["gmetadata"]}", ArrayList::class.java)
      for (gallery in galleries) {
        val (gData, gTags) = EHGallery(gallery as Map<*, *>).toTSV()
        parsedMetadata.append(gData)
        parsedTags.append(gTags)
      }
      if (save) {
        galleriesDataFile?.appendText("$parsedMetadata")
        galleriesTagsFile?.appendText("$parsedTags")
        parsedMetadata.clear()
        parsedTags.clear()
      }
      urlsToParse = urls.drop(25) // Drops the first 25 urls
      keys.clear()
    }
    return Pair("$parsedMetadata", "$parsedTags")
  }
}