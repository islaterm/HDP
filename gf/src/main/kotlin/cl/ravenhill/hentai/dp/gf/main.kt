package cl.ravenhill.hentai.dp.gf

import cl.ravenhill.hentai.dp.gf.gallery.EHParser
import cl.ravenhill.hentai.dp.gf.sitemap.EHSitemapParser
import java.io.File

private val srcDir = "${System.getProperty("user.dir")}/src/main"
private val metadataFile = "$srcDir/resources/GalleriesMetadata.tsv"
private val tagsFile = "$srcDir/resources/GalleriesTags.tsv"

fun main() {
  val sitemap = EHSitemapParser()
  sitemap.parseSitemaps()
  val fetcher = EHParser(File(metadataFile), File(tagsFile))
  println(fetcher.parseToTSV(save = true, sitemap.galleriesURL))
}