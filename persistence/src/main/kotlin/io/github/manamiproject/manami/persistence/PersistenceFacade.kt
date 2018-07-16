package io.github.manamiproject.manami.persistence

import io.github.manamiproject.manami.common.EventBus
import io.github.manamiproject.manami.entities.*
import io.github.manamiproject.manami.persistence.events.AnimeListChangedEvent
import io.github.manamiproject.manami.persistence.events.FilterListChangedEvent
import io.github.manamiproject.manami.persistence.events.WatchListChangedEvent
import io.github.manamiproject.manami.persistence.exporter.json.JsonExporter
import io.github.manamiproject.manami.persistence.exporter.xml.XmlExporter
import io.github.manamiproject.manami.persistence.importer.json.JsonImporter
import io.github.manamiproject.manami.persistence.importer.xml.XmlImporter
import io.github.manamiproject.manami.persistence.importer.xml.XmlImporter.XmlStrategy.*
import io.github.manamiproject.manami.persistence.importer.xml.parser.MalSaxParser
import io.github.manamiproject.manami.persistence.importer.xml.parser.ManamiSaxParser
import io.github.manamiproject.manami.persistence.inmemory.InMemoryPersistence
import io.github.manamiproject.manami.persistence.inmemory.animelist.InMemoryAnimeList
import io.github.manamiproject.manami.persistence.inmemory.filterlist.InMemoryFilterList
import io.github.manamiproject.manami.persistence.inmemory.watchlist.InMemoryWatchList
import java.nio.file.Path


/**
 * Single point of contact for everything related to the persistence.
 * This is a facade which is used by the application to hide the actual persistence strategy.
 */
object PersistenceFacade : Persistence {

    private val strategy: InternalPersistence = InMemoryPersistence(
            InMemoryAnimeList(),
            InMemoryFilterList(),
            InMemoryWatchList()
    )
    private val jsonExporter = JsonExporter(strategy)
    private val jsonImporter = JsonImporter(strategy)
    private val xmlExporter  = XmlExporter(strategy)
    private val xmlImporter = XmlImporter(
            ManamiSaxParser(strategy),
            MalSaxParser(strategy)
    )


    override fun filterAnime(anime: MinimalEntry): Boolean {
        if (anime.isValid()) {
            if(strategy.animeEntryExists(anime.infoLink)) {
                return false
            }

            if (strategy.filterAnime(anime)) {
                EventBus.publish(FilterListChangedEvent)
                removeFromWatchList(anime)

                return true
            }

        }

        return false
    }


    override fun fetchFilterList(): List<FilterListEntry> {
        return strategy.fetchFilterList()
    }


    override fun filterListEntryExists(infoLink: InfoLink): Boolean {
        return strategy.filterListEntryExists(infoLink)
    }


    override fun removeFromFilterList(anime: MinimalEntry): Boolean {
        if (!anime.isValid()) {
            return false
        }

        if (strategy.removeFromFilterList(anime)) {
            EventBus.publish(FilterListChangedEvent)
            return true
        }

        return false
    }


    override fun addAnime(anime: Anime): Boolean {
        if (anime.isValid() && strategy.addAnime(anime)) {
            if (anime.infoLink.isValid()) {
                removeFromFilterList(anime)
                removeFromWatchList(anime)
            }

            EventBus.publish(AnimeListChangedEvent)

            return true
        }

        return false
    }


    override fun fetchAnimeList(): List<Anime> {
        return strategy.fetchAnimeList()
    }


    override fun animeEntryExists(infoLink: InfoLink): Boolean {
        return strategy.animeEntryExists(infoLink)
    }


    override fun removeAnime(anime: Anime): Boolean {
        if (strategy.removeAnime(anime)) {
            EventBus.publish(AnimeListChangedEvent)
            return true
        }

        return false
    }


    override fun watchAnime(anime: MinimalEntry): Boolean {
        if (anime.isValid()) {
            if(strategy.animeEntryExists(anime.infoLink)) {
                return false
            }

            if (strategy.watchAnime(anime)) {
                EventBus.publish(WatchListChangedEvent)
                removeFromFilterList(anime)

                return true
            }
        }

        return false
    }


    override fun fetchWatchList(): List<WatchListEntry> {
        return strategy.fetchWatchList()
    }


    override fun watchListEntryExists(infoLink: InfoLink): Boolean {
        return strategy.watchListEntryExists(infoLink)
    }


    override fun removeFromWatchList(anime: MinimalEntry): Boolean {
        if (!anime.isValid()) {
            return false
        }

        if (strategy.removeFromWatchList(anime)) {
            EventBus.publish(WatchListChangedEvent)
            return true
        }

        return false
    }


    override fun clearAll() {
        strategy.clearAll()
        notifyAllListsChanged()
    }


    override fun open(file: Path) {
        xmlImporter.using(MANAMI).importFile(file)
        notifyAllListsChanged()
    }


    override fun importMalFile(file: Path) {
        xmlImporter.using(MAL).importFile(file)
        notifyAllListsChanged()
    }


    override fun importJsonFile(file: Path) {
        jsonImporter.importFile(file)
        notifyAllListsChanged()
    }


    override fun exportListToJsonFile(list: List<Anime>, file: Path) {
        jsonExporter.exportList(list, file)
    }


    override fun exportToJsonFile(file: Path) {
        jsonExporter.save(file)
    }


    override fun save(file: Path) = xmlExporter.save(file)


    private fun notifyAllListsChanged() {
        EventBus.publish(AnimeListChangedEvent)
        EventBus.publish(FilterListChangedEvent)
        EventBus.publish(WatchListChangedEvent)
    }
}
