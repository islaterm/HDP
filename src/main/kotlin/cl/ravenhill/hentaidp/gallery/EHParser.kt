package cl.ravenhill.hentaidp.gallery

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import khttp.post
import khttp.responses.Response
import org.slf4j.LoggerFactory
import java.io.File
import java.net.ConnectException
import java.net.SocketTimeoutException

/**
 * Parser for the cl.ravenhill.hentaidp.gallery data of e-hentai.org
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
  private val logger = LoggerFactory.getLogger(javaClass)
  private val cache: ArrayList<String>
  private val cacheFile = File("${System.getProperty("user.dir")}/src/main/cache.json")

  init {
    cache = try {
      ObjectMapper().readValue(cacheFile, object : TypeReference<ArrayList<String>>() {})
    } catch (e: Exception) {
      arrayListOf()
    }
  }

  /**
   * Parses a list of urls to tsv format
   *
   * @param save if the data should be saved to files
   * @param urls links to the galleries to parse
   * @return a pair containing te galleries metadata and its tags
   */
  fun parseToTSV(save: Boolean, urls: List<String>): Pair<String, String> {
    var urlsToParse = urls
    val keys = mutableListOf<List<String>>()
    val parsedMetadata = StringBuilder()
    val parsedTags = StringBuilder()
    var response: Response
    val totalGalleries = urlsToParse.size
    while (urlsToParse.isNotEmpty()) {
      // Takes the first 25 urls and creates a list of pairs (id, token)
      urlsToParse.take(25).forEach {
        val idTokenPair = it.replace("https://e-hentai.org/g/", "").split("/")
        if (idTokenPair[0] !in cache) {
          keys.add(it.replace("https://e-hentai.org/g/", "").split("/"))
        } else {
          logger.info("$it already processed, skipping.")
        }
      }
      if (keys.isNotEmpty()) {
        val data = mapOf(
          "method" to "gdata",
          "gidlist" to keys,
          "namespace" to 1
        )
        logger.info("Fetched ${totalGalleries - urlsToParse.size} / $totalGalleries galleries")
        Thread.sleep(10_000)  // Waiting to avoid overloading the server
        logger.info("Retrieving info for $data")
        var retryDelay = 1000L
        while (true) {
          try {
            response = post("https://api.e-hentai.org/api.php", json = data)
            break
          } catch (e: Exception) {
            when (e) {
              is SocketTimeoutException, is ConnectException -> {
                logger.error("${e.cause}")
                logger.error(e.message)
                logger.warn("Waiting for ${retryDelay / 1000} seconds.")
                Thread.sleep(retryDelay)
                retryDelay *= 2
              }
              else -> throw e
            }
          }
        }


        val galleries =
          ObjectMapper().readValue("${response.jsonObject["gmetadata"]}", ArrayList::class.java)
        for (gallery in galleries) {
          try {
            val (gData, gTags) = EHGallery(gallery as Map<*, *>).toTSV()
            logger.info("Gallery data: $gData")
            logger.info("Gallery tags: $gTags")
            if (gData.isNotBlank()) {
              parsedMetadata.append(gData)
            }
            if (gTags.isNotBlank()) {
              parsedTags.append(gTags)
            }
          } catch (e: Exception) {
            logger.error(
              "Couldn't parse gallery.\n\t" +
                  "$gallery is not a valid gallery"
            )
          }
        }
        if (save) {
          galleriesDataFile?.appendText("$parsedMetadata")
          galleriesTagsFile?.appendText("$parsedTags")
          parsedMetadata.clear()
          parsedTags.clear()
        }
      }
      urlsToParse = urlsToParse.drop(25) // Drops the first 25 urls
      keys.forEach {
        cache.add(it[0])
      }
      keys.clear()
      ObjectMapper().writeValue(cacheFile, cache)
    }
    return Pair("$parsedMetadata", "$parsedTags")
  }
}