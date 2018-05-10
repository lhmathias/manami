package io.github.manamiproject.manami.core.tasks.checklist

import io.github.manamiproject.manami.common.EventBus
import io.github.manamiproject.manami.common.LoggerDelegate
import io.github.manamiproject.manami.core.config.Config
import io.github.manamiproject.manami.core.events.checklist.NoCrcSumProvidedChecklistEvent
import io.github.manamiproject.manami.core.events.checklist.CrcSumsDifferChecklistEvent
import io.github.manamiproject.manami.entities.Anime
import io.github.manamiproject.manami.persistence.utility.PathResolver.buildPath
import org.slf4j.Logger
import java.lang.Long.toHexString
import java.nio.file.Files
import java.nio.file.Files.isRegularFile
import java.nio.file.Files.newDirectoryStream
import java.nio.file.LinkOption.NOFOLLOW_LINKS
import java.util.zip.CRC32

internal class CrcSumChecker(
        private val animeList: List<Anime>
) : Checker {

    private val log: Logger by LoggerDelegate()

    override fun estimateWorkload(): Int {
        var fileCounter = 0

        animeList
                .filter { it.location.isNotBlank() && it.location != "." }
                .map { it.location }
                .forEach {
                    buildPath(it, Config.file.parent)?.let {
                        fileCounter += Files.list(it).filter { isRegularFile(it) }.count().toInt()
                    }
                }

        return fileCounter
    }

    override fun check() {
        animeList
                .filter { it.location.isNotBlank() && it.location != "." }
                .forEach(this::checkCrc)
    }

    private fun checkCrc(anime: Anime) {
        log.debug("Checking CRC32 sum of {}", anime.title)

        buildPath(anime.location, Config.file.fileName)?.let {
            newDirectoryStream(it).use {
                it.filter { isRegularFile(it, NOFOLLOW_LINKS) }.forEach {
                    val crc32 = CRC32()
                    crc32.update(Files.readAllBytes(it))
                    val crcSum: String = toHexString(crc32.value)

                    val matchResult: MatchResult? = Regex("\\[.{8}\\]").find(it.fileName.toString())

                    if (matchResult != null) {
                        val crcInTitle: String = matchResult.value
                                .replace("[", "")
                                .replace("]", "")

                        if (!crcSum.equals(crcInTitle, ignoreCase = true)) {
                            EventBus.publish(CrcSumsDifferChecklistEvent(it, crcSum))
                        }
                    } else {
                        EventBus.publish(NoCrcSumProvidedChecklistEvent(it))
                    }
                }
            }
        }
    }
}