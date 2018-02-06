package io.github.manamiproject.manami.cache.offlinedatabase

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.github.manamiproject.manami.common.LoggerDelegate
import io.github.manamiproject.manami.dto.AnimeType
import io.github.manamiproject.manami.dto.entities.InfoLink
import io.github.manamiproject.manami.dto.entities.NORMALIZED_ANIME_DOMAIN
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.api.errors.GitAPIException
import org.slf4j.Logger
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

internal class OfflineDatabaseGitRepository {

    private val log: Logger by LoggerDelegate()
    private val repo: Path = Paths.get("database")
    private val databaseFile: Path = repo.resolve("anime-offline-database.json")
    private val deadEntriesFile: Path = repo.resolve("not-found.json")
    private val gson: Gson = GsonBuilder().apply {
        registerTypeAdapter(AnimeType::class.java, AnimeTypeDeserializer())
    }.create()

    val database: Database = Database()


    init {
        if (Files.exists(repo)) {
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
        } catch (e: Exception) {
            log.error("Error updating offline database: ", e)
        }
    }


    private fun cloneRepo() {
        log.info("Initially cloning the offline database repository.")

        try {
            Files.createDirectories(repo)
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
        if (Files.exists(databaseFile) && Files.isRegularFile(databaseFile)) {
            log.debug("Reading database file")
            val content = String(Files.readAllBytes(databaseFile))
            val animeMetaData: AnimeMetaData? = gson.fromJson(content, AnimeMetaData::class.java)

            animeMetaData?.data?.forEach(database::addAnimeEntry)
        }
    }


    private fun readDeadEntriesFile() {
        if (Files.exists(deadEntriesFile) && Files.isRegularFile(deadEntriesFile)) {
            log.debug("reading not-found file")
            val content = String(Files.readAllBytes(deadEntriesFile))
            val deadEntries: DeadEntries? = gson.fromJson(content, DeadEntries::class.java)

            deadEntries?.mal?.forEach { id ->
                database.addDeadEntry(InfoLink("${NORMALIZED_ANIME_DOMAIN.MAL.value}$id"))
            }

            deadEntries?.anidb?.forEach { id ->
                database.addDeadEntry(InfoLink("${NORMALIZED_ANIME_DOMAIN.ANIDB.value}$id"))
            }
        }
    }
}