package io.github.manami.persistence.exporter.json

import io.github.manami.dto.entities.Anime
import io.github.manami.dto.entities.FilterListEntry
import io.github.manami.dto.entities.WatchListEntry
import io.github.manami.persistence.ApplicationPersistence
import io.github.manami.persistence.exporter.Exporter
import org.json.JSONWriter
import java.io.PrintWriter
import java.nio.file.Path
import javax.inject.Inject
import javax.inject.Named

/**
 * Exports a list to valid json.
 */
@Named("jsonExporter")
internal class JsonExporter @Inject constructor(private val persistence: ApplicationPersistence) : Exporter {

    override fun save(file: Path): Boolean {
        PrintWriter(file.toFile()).use { printWriter ->
            val writer: JSONWriter = JSONWriter(printWriter).apply {
                array()
                array()
            }

            for (element: Anime in persistence.fetchAnimeList()) {
                writer.`object`()
                        .key("title").value(element.title)
                        .key("type").value(element.type)
                        .key("episodes").value(element.episodes)
                        .key("infoLink").value(element.infoLink)
                        .key("location").value(element.location)
                        .endObject()
            }

            writer.let {
                it.endArray()
                it.array()
            }

            for (element: WatchListEntry in persistence.fetchWatchList()) {
                writer.`object`()
                        .key("thumbnail").value(element.thumbnail)
                        .key("title").value(element.title)
                        .key("infoLink").value(element.infoLink).endObject()
            }

            writer.let {
                it.endArray()
                it.array()
            }

            for (element: FilterListEntry in persistence.fetchFilterList()) {
                writer.`object`()
                        .key("thumbnail").value(element.thumbnail)
                        .key("title").value(element.title)
                        .key("infoLink").value(element.infoLink).endObject()
            }

            writer.let {
                it.endArray()
                it.endArray()
            }

            printWriter.flush()
        }

        return true
    }


    fun exportList(list: MutableList<Anime>, file: Path): Boolean {
        PrintWriter(file.toFile()).use { printWriter ->
            val writer = JSONWriter(printWriter).apply {
                array()
                array()
            }

            for (element: Anime in list) {
                writer.`object`()
                        .key("title").value(element.title)
                        .key("type").value(element.type)
                        .key("episodes").value(element.episodes)
                        .key("infoLink").value(element.infoLink)
                        .key("location").value(element.location)
                        .endObject()
            }

            writer.let {
                it.endArray()
                it.endArray()
            }

            printWriter.flush()
        }

        return true
    }
}
