package io.github.manamiproject.manami.core.tasks.checklist

import io.github.manamiproject.manami.common.*
import io.github.manamiproject.manami.core.config.Config
import io.github.manamiproject.manami.core.events.checklist.*
import io.github.manamiproject.manami.entities.Anime
import io.github.manamiproject.manami.persistence.utility.PathResolver.buildPath
import io.github.manamiproject.manami.persistence.utility.PathResolver.buildRelativizedPath
import org.slf4j.Logger
import java.nio.file.DirectoryStream
import java.nio.file.Files
import java.nio.file.Files.newDirectoryStream
import java.nio.file.Path
import java.nio.file.Paths


internal class LocationsChecker(
        private val animeList: List<Anime>
) : Checker {

    private val log: Logger by LoggerDelegate()

    override fun estimateWorkload(): Int {
        return animeList.size
    }

    override fun check() {
        animeList.forEach(this::checkEntry)
    }

    private fun checkEntry(anime: Anime) {
        log.debug("Checking location of [{}]", anime.title)

        if(!hasLocationBeenSet(anime)) {
            return
        }

        doesLocationExist(anime)?.let {
            newDirectoryStream(it).use {
                doesNumberOfFilesDifferNumberOfEpisodes(it, anime)
            }

            canLocationBeConvertedToRelativePath(anime)
        }
    }


    private fun hasLocationBeenSet(anime: Anime): Boolean {
        return if (anime.location.isBlank() || anime.location == ".") {
            EventBus.publish(LocationNotSetChecklistEvent(anime))
            false
        } else {
            true
        }
    }


    private fun doesLocationExist(anime: Anime): Path? {
        val directory: Path? = buildPath(anime.location, Config.file.parent)

        if (directory == null) {
            EventBus.publish(LocationNotExistsChecklistEvent(anime))
        }

        return directory
    }


    private fun doesNumberOfFilesDifferNumberOfEpisodes(it: DirectoryStream<Path>, anime: Anime) {
        var fileCounter = 0

        it.filter { it.isRegularFile() }.forEach {
            fileCounter = fileCounter.inc()
        }

        if (fileCounter == 0) {
            EventBus.publish(LocationIsEmptyChecklistEvent(anime))
        } else if (fileCounter != anime.episodes) {
            EventBus.publish(LocationNumberOfFilesNotMatchEpisodesChecklistEvent(anime))
        }
    }


    private fun canLocationBeConvertedToRelativePath(anime: Anime) {
        val dir: Path = Paths.get(anime.location)
        if (dir.exists() && dir.isDirectory()) {
            val relativizedLocation = buildRelativizedPath(anime.location, Config.file.parent)
            EventBus.publish(RelativizeLocationChecklistEvent(anime, relativizedLocation))
        }
    }
}