package io.github.manami.persistence.importer.xml.postprocessor

import io.github.manami.dto.LoggerDelegate
import io.github.manami.dto.entities.*
import io.github.manami.persistence.utility.Version
import org.slf4j.Logger
import java.net.MalformedURLException
import java.net.URL


internal class ImportMigrationPostProcessor(
        private val documentVersion: String,
        private val animeListEntries: MutableList<Anime>,
        private val filterListEntries: MutableList<FilterListEntry>,
        private val watchListEntries: MutableList<WatchListEntry>
) {

    private val log: Logger by LoggerDelegate()

    private val parsedDocumentVersion: Version = Version(documentVersion)

    fun execute() {
        if (!Version.isValid(documentVersion)) {
            log.warn("SKIPPING import migration post processor. Document version could not be identified.")
            return
        }

        log.info("Starting post processing.")

        migrateTo_2_10_3()
        migrateTo_2_14_2()
    }


    /**
     * Converts info links from http to https (Finally MAL! Took you long enough.)
     */
    private fun migrateTo_2_14_2() {
        if (parsedDocumentVersion.isNewerThan("2.14.2")) {
            log.info("SKIPPING migration to 2.14.2.")
            return
        }

        log.info("Migrating list to version 2.14.2.")
        animeListEntries.forEach(this::migrateMalInfoLinkToHttps)
        filterListEntries.forEach(this::migrateMalInfoLinkToHttps)
        watchListEntries.forEach(this::migrateMalInfoLinkToHttps)
    }


    private fun migrateMalInfoLinkToHttps(anime: MinimalEntry) {
        if (!anime.infoLink.isValid()) {
            return
        }

        if (anime.infoLink.url.toString().startsWith("http://myanimelist.net") || anime.infoLink.url.toString().startsWith("http://www.myanimelist.net")) {
            anime.infoLink = InfoLink(anime.infoLink.url.toString().replace("http", "https"))
        }
    }


    /**
     * Converts the picture links of MAL to the new CDN URL.
     */
    private fun migrateTo_2_10_3() {
        if (parsedDocumentVersion.isNewerThan("2.10.3")) {
            log.info("SKIPPING migration to 2.10.3.")
            return
        }

        log.info("Migrating list to version 2.10.3.")
        filterListEntries.forEach(this::migrateCdnUrl)
        watchListEntries.forEach(this::migrateCdnUrl)
    }


    private fun migrateCdnUrl(anime: MinimalEntry) {
        val oldCdnUrl = "http://cdn.myanimelist.net"
        val newCdnUrl = "https://myanimelist.cdn-dena.com"
        val thumbnail = anime.thumbnail.toString()

        try {
            if (thumbnail.startsWith(oldCdnUrl)) {
                anime.thumbnail = URL(thumbnail.replace(oldCdnUrl, newCdnUrl))
            }
        } catch (e: MalformedURLException) {
            log.error("Converting thumbnail URL for [{}] created an error", anime.title, e)
        }
    }
}
