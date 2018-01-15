package io.github.manami.persistence

import io.github.manami.common.EventBus
import io.github.manami.dto.entities.*
import io.github.manami.persistence.events.AnimeListChangedEvent
import io.github.manami.persistence.events.FilterListChangedEvent
import io.github.manami.persistence.events.WatchListChangedEvent
import io.github.manami.persistence.exporter.json.JsonExporter
import io.github.manami.persistence.exporter.xml.XmlExporter
import io.github.manami.persistence.importer.json.JsonImporter
import io.github.manami.persistence.importer.xml.XmlImporter
import io.github.manami.persistence.importer.xml.parser.MalSaxParser
import io.github.manami.persistence.importer.xml.parser.ManamiSaxParser
import io.github.manami.persistence.inmemory.InMemoryPersistence
import io.github.manami.persistence.inmemory.animelist.InMemoryAnimeList
import io.github.manami.persistence.inmemory.filterlist.InMemoryFilterList
import io.github.manami.persistence.inmemory.watchlist.InMemoryWatchList
import java.nio.file.Path


/**
 * This is a facade which is used by the application to hide which strategy is actually used.
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
            if (strategy.filterAnime(anime)) {
                EventBus.publish(FilterListChangedEvent)

                if(removeFromWatchList(anime)) {
                    EventBus.publish(WatchListChangedEvent)
                }

                return true
            }

        }
        return false
    }


    override fun fetchFilterList(): MutableList<FilterListEntry> {
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
                if(removeFromFilterList(anime)) {
                    EventBus.publish(FilterListChangedEvent)
                }

                if(removeFromWatchList(anime)) {
                    EventBus.publish(WatchListChangedEvent)
                }
            }

            EventBus.publish(AnimeListChangedEvent)
            return true
        }

        return false
    }


    override fun fetchAnimeList(): MutableList<Anime> {
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
            if (strategy.watchAnime(anime)) {
                EventBus.publish(WatchListChangedEvent)

                if(removeFromFilterList(anime)) {
                    EventBus.publish(FilterListChangedEvent)
                }

                return true
            }
        }

        return false
    }


    override fun fetchWatchList(): MutableList<WatchListEntry> {
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
        EventBus.publish(AnimeListChangedEvent)
        EventBus.publish(FilterListChangedEvent)
        EventBus.publish(WatchListChangedEvent)
    }


    override fun addAnimeList(list: MutableList<Anime>) {
        val validAnimeList: MutableList<Anime> = mutableListOf()

        list.filter { it.isValid() }.toCollection(validAnimeList)

        strategy.addAnimeList(validAnimeList)
        EventBus.publish(AnimeListChangedEvent)
    }


    override fun addFilterList(list: MutableList<FilterListEntry>) {
        val validEntryList: MutableList<FilterListEntry> = mutableListOf()

        list.filter { it.isValid() }.toCollection(validEntryList)

        strategy.addFilterList(validEntryList)
        EventBus.publish(FilterListChangedEvent)
    }


    override fun addWatchList(list: MutableList<WatchListEntry>) {
        val validEntryList: MutableList<WatchListEntry> = mutableListOf()

        list.filter { it.isValid() }.toCollection(validEntryList)


        strategy.addWatchList(validEntryList)
        EventBus.publish(WatchListChangedEvent)
    }


    override fun updateOrCreate(entry: MinimalEntry) {
        when (entry) {
            is Anime -> {
                if(entry.isValid()) {
                    strategy.updateOrCreate(entry)
                    EventBus.publish(AnimeListChangedEvent)
                }
            }
            is FilterListEntry -> {
                if(entry.isValid()) {
                    strategy.updateOrCreate(entry)
                    EventBus.publish(FilterListChangedEvent)
                }
            }
            is WatchListEntry -> {
                if(entry.isValid()) {
                    strategy.updateOrCreate(entry)
                    EventBus.publish(WatchListChangedEvent)
                }
            }
        }
    }


    override fun open(file: Path) {
        xmlImporter.using(XmlImporter.XmlStrategy.MANAMI).importFile(file)
    }


    override fun importMalFile(file: Path) {
        xmlImporter.using(XmlImporter.XmlStrategy.MAL).importFile(file)
    }


    override fun exportListToJsonFile(list: MutableList<Anime>, file: Path) {
        jsonExporter.exportList(list, file)
    }


    override fun importJsonFile(file: Path) {
        jsonImporter.importFile(file)
    }


    override fun exportToJsonFile(file: Path) {
        jsonExporter.save(file)
    }


    override fun save(file: Path) = xmlExporter.save(file)
}
