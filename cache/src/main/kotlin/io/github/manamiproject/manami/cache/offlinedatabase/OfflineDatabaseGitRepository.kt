package io.github.manamiproject.manami.cache.offlinedatabase

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.github.manamiproject.manami.cache.populator.CacheEntrySource
import io.github.manamiproject.manami.common.*
import io.github.manamiproject.manami.entities.AnimeType
import io.github.manamiproject.manami.entities.Anime
import io.github.manamiproject.manami.entities.InfoLink
import io.github.manamiproject.manami.entities.NORMALIZED_ANIME_DOMAIN
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.api.errors.GitAPIException
import org.slf4j.Logger
import java.io.IOException
import java.lang.Exception
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

internal class OfflineDatabaseGitRepository : CacheEntrySource {

    private val log: Logger by LoggerDelegate()
    private val repo: Path = Paths.get("database")
    private val databaseFile: Path = repo.resolve("anime-offline-database.json")
    private val deadEntriesFile: Path = repo.resolve("not-found.json")
    private val animeEntries: MutableMap<InfoLink, Anime?> = mutableMapOf()
    private val relatedAnimeEntries: MutableMap<InfoLink, Set<InfoLink>> = mutableMapOf()
    private val gson: Gson = GsonBuilder().apply {
        registerTypeAdapter(AnimeType::class.java, AnimeTypeDeserializer())
    }.create()

    init {
        if (repo.exists()) {
            updateRepo()
        } else {
            cloneRepo()
        }

        try {
            readDatabaseFile()
            readDeadEntriesFile()
        } catch (e: IOException) {
            log.error("Error reading database files: ", e)
        }
    }


    private fun updateRepo() {
        log.info("Updating offline database.")

        try {
            Git.open(repo.toFile())
                    .pull()
                    .call()

            EventBus.publish(OfflineDatabaseUpdatedSuccessfullyEvent)
        } catch (e: Exception) {
            log.error("Error updating offline database: ", e)
        }
    }

    private fun cloneRepo() {
        log.info("Initially cloning the offline database repository.")

        try {
            repo.createDirectories()
            Git.cloneRepository()
                    .setURI("https://github.com/manami-project/anime-offline-database.git")
                    .setDirectory(repo.toFile())
                    .call()
        } catch (e: GitAPIException) {
            log.error("Error cloning the repository of the offline database: ", e)
        } catch (e: IOException) {
            log.error("Error creating folder for the offline database: ", e)
        }
    }

    private fun readDatabaseFile() {
        if (databaseFile.exists() && databaseFile.isRegularFile()) {
            log.debug("Reading database file")
            val content = String(databaseFile.readAllBytes())
            val animeMetaData: AnimeMetaData? = gson.fromJson(content, AnimeMetaData::class.java)

            animeMetaData?.data?.forEach { entry ->
                entry.sources.forEach { infoLinkUrl ->
                    val infoLink = InfoLink(infoLinkUrl)

                    val anime = Anime(
                            entry.title,
                            infoLink,
                            entry.episodes,
                            entry.type,
                            "/",
                            entry.thumbnail,
                            entry.picture,
                            entry.id
                    )

                    animeEntries[infoLink] = anime

                    val relatedAnime: MutableSet<InfoLink> = mutableSetOf()

                    entry?.relations
                            ?.filter { url -> url.host == infoLink.url?.host }
                            ?.forEach { relation -> relatedAnime.add(InfoLink(relation)) }

                    relatedAnimeEntries[infoLink] = relatedAnime
                }
            }
        }
    }

    private fun readDeadEntriesFile() {
        if (deadEntriesFile.exists() && deadEntriesFile.isRegularFile()) {
            log.debug("reading not-found file")
            val content = String(deadEntriesFile.readAllBytes())
            val deadEntries: DeadEntries? = gson.fromJson(content, DeadEntries::class.java)

            deadEntries?.mal?.forEach { id ->
                animeEntries[InfoLink("${NORMALIZED_ANIME_DOMAIN.MAL.value}$id")] = null
            }

            deadEntries?.anidb?.forEach { id ->
                animeEntries[InfoLink("${NORMALIZED_ANIME_DOMAIN.ANIDB.value}$id")] = null
            }
        }
    }

    override fun getAnimeCacheEntries(): Map<InfoLink, Anime?> {
        return animeEntries
    }

    override fun getRelatedAnimeCacheEntries(): Map<InfoLink, Set<InfoLink>> {
        return relatedAnimeEntries
    }
}