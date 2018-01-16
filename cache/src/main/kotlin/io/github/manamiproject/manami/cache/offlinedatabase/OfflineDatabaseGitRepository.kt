package io.github.manamiproject.manami.cache.offlinedatabase

import com.google.gson.Gson
import io.github.manamiproject.manami.common.LoggerDelegate
import io.github.manamiproject.manami.dto.entities.InfoLink
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.api.errors.GitAPIException
import org.slf4j.Logger
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

class OfflineDatabaseGitRepository {

    private val log: Logger by LoggerDelegate()
    private val repo: Path = Paths.get("..").resolve("..").resolve("database")
    private val databaseFile: Path = repo.resolve("manami-offline-database.json")
    private val deadEntriesFile: Path = repo.resolve("notFound.json")
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
            // TODO Auto-generated catch block
            e.printStackTrace()
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
                    .setURI("https://github.com/manami-project/manami-offline-database.git")
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
            val content = String(Files.readAllBytes(databaseFile))
            val animeMetaData: AnimeMetaData? = Gson().fromJson(content, AnimeMetaData::class.java)

            animeMetaData?.data?.forEach(database::addAnimeEntry)
        }
    }


    private fun readDeadEntriesFile() {
        if (Files.exists(deadEntriesFile) && Files.isRegularFile(deadEntriesFile)) {
            val content = String(Files.readAllBytes(deadEntriesFile))
            val deadEntries: DeadEntries? = Gson().fromJson(content, DeadEntries::class.java)

            deadEntries?.mal?.forEach { id ->
                createMalInfoLinkFromId(id).let { urlString -> database.addDeadEntry(urlString) }
            }

            deadEntries?.anidb?.forEach { id ->
                createAnidbInfoLinkFromId(id).let { urlString -> database.addDeadEntry(urlString) }
            }
        }
    }


    private fun createMalInfoLinkFromId(id: Int) = InfoLink("http://myanimelist.net/anime/$id")


    private fun createAnidbInfoLinkFromId(id: Int) = InfoLink("https://anidb.net/a$id")
}