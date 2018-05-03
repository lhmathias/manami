package io.github.manamiproject.manami.persistence.importer.xml.postprocessor

import io.github.manamiproject.manami.common.EventBus
import io.github.manamiproject.manami.common.LoggerDelegate
import io.github.manamiproject.manami.entities.*
import io.github.manamiproject.manami.persistence.utility.Version
import org.slf4j.Logger
import java.net.URL

internal object ImportMigrationPostProcessor {

    private val log: Logger by LoggerDelegate()

    private var parsedDocumentVersion: Version = Version("0.0.0")
    private var animeListEntries: MutableList<Anime> = mutableListOf()
    private var filterListEntries: MutableList<FilterListEntry> = mutableListOf()
    private var watchListEntries: MutableList<WatchListEntry> = mutableListOf()

    fun process(importDocument: ImportDocument) {
        if (!Version.isValid(importDocument.documentVersion)) {
            log.warn("Document version is not valid.")
            EventBus.publish(FileImportExceptionEvent("Document version is not valid. Unable to import file."))
            return
        }

        log.info("Starting post processing.")

        parsedDocumentVersion = Version(importDocument.documentVersion)
        animeListEntries = importDocument.animeListEntries
        filterListEntries = importDocument.filterListEntries
        watchListEntries = importDocument.watchListEntries

        migrateTo021003()
        migrateTo021402()
    }


    /**
     * Migrates a file to version 2.14.2.
     * Converts info links from http to https (Finally MAL! Took you long enough.)
     */
    private fun migrateTo021402() {
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

        if (anime.infoLink.toString().startsWith("http://myanimelist.net") || anime.infoLink.toString().startsWith("http://www.myanimelist.net")) {
            anime.infoLink = InfoLink(anime.infoLink.toString().replace("http", "https"))
        }
    }


    /**
     * Migrates document to version 2.10.3.
     * Converts the picture links of MAL to the new CDN URL.
     */
    private fun migrateTo021003() {
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

        if (thumbnail.startsWith(oldCdnUrl)) {
            anime.thumbnail = URL(thumbnail.replace(oldCdnUrl, newCdnUrl))
        }
    }
}
