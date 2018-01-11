package io.github.manami.persistence

import com.google.common.eventbus.EventBus
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
import io.github.manami.persistence.importer.xml.postprocessor.ImportMigrationPostProcessor
import java.nio.file.Path
import javax.inject.Inject
import javax.inject.Named

/**
 * This is a facade which is used by the application to hide which strategy is actually used.
 */
@Named
internal class PersistenceFacade
    @Inject
    constructor(
        @Named("inMemoryStrategy") private val strategy: InternalPersistenceHandler, //Currently used db persistence strategy.
        private val eventBus: EventBus
) : PersistenceHandler {

    private val jsonExporter = JsonExporter(strategy)
    private val jsonImporter = JsonImporter(strategy)
    private val xmlExporter  = XmlExporter(strategy)
    private val xmlImporter = XmlImporter(
            ManamiSaxParser(strategy, ImportMigrationPostProcessor(eventBus)),
            MalSaxParser(strategy)
    )

    override fun filterAnime(anime: MinimalEntry): Boolean {
        if (anime.isValidMinimalEntry()) {
            if (strategy.filterAnime(anime)) {
                eventBus.post(FilterListChangedEvent)

                if(removeFromWatchList(anime)) {
                    eventBus.post(WatchListChangedEvent)
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
        if (!anime.isValidMinimalEntry()) {
            return false
        }

        if (strategy.removeFromFilterList(anime)) {
            eventBus.post(FilterListChangedEvent)
            return true
        }

        return false
    }


    override fun addAnime(anime: Anime): Boolean {
        if (anime.isValidAnime() && strategy.addAnime(anime)) {
            if (anime.infoLink.isValid()) {
                if(removeFromFilterList(anime)) {
                    eventBus.post(FilterListChangedEvent)
                }

                if(removeFromWatchList(anime)) {
                    eventBus.post(WatchListChangedEvent)
                }
            }

            eventBus.post(AnimeListChangedEvent)
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
            eventBus.post(AnimeListChangedEvent)
            return true
        }

        return false
    }


    override fun watchAnime(anime: MinimalEntry): Boolean {
        if (anime.isValidMinimalEntry()) {
            if (strategy.watchAnime(anime)) {
                eventBus.post(WatchListChangedEvent)

                if(removeFromFilterList(anime)) {
                    eventBus.post(FilterListChangedEvent)
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
        if (!anime.isValidMinimalEntry()) {
            return false
        }

        if (strategy.removeFromWatchList(anime)) {
            eventBus.post(WatchListChangedEvent)
            return true
        }

        return false
    }


    override fun clearAll() {
        strategy.clearAll()
        eventBus.post(AnimeListChangedEvent)
        eventBus.post(FilterListChangedEvent)
        eventBus.post(WatchListChangedEvent)
    }


    override fun addAnimeList(list: MutableList<Anime>) {
        val validAnimeList: MutableList<Anime> = mutableListOf()

        list.filter { it.isValidAnime() }.toCollection(validAnimeList)

        strategy.addAnimeList(validAnimeList)
        eventBus.post(AnimeListChangedEvent)
    }


    override fun addFilterList(list: MutableList<FilterListEntry>) {
        val validEntryList: MutableList<FilterListEntry> = mutableListOf()

        list.filter { it.isValidMinimalEntry() }.toCollection(validEntryList)

        strategy.addFilterList(validEntryList)
        eventBus.post(FilterListChangedEvent)
    }


    override fun addWatchList(list: MutableList<WatchListEntry>) {
        val validEntryList: MutableList<WatchListEntry> = mutableListOf()

        list.filter { it.isValidMinimalEntry() }.toCollection(validEntryList)


        strategy.addWatchList(validEntryList)
        eventBus.post(WatchListChangedEvent)
    }


    override fun updateOrCreate(entry: MinimalEntry) {
        when (entry) {
            is Anime -> {
                if(entry.isValidAnime()) {
                    strategy.updateOrCreate(entry)
                    eventBus.post(AnimeListChangedEvent)
                }
            }
            is FilterListEntry -> {
                if(entry.isValidMinimalEntry()) {
                    strategy.updateOrCreate(entry)
                    eventBus.post(FilterListChangedEvent)
                }
            }
            is WatchListEntry -> {
                if(entry.isValidMinimalEntry()) {
                    strategy.updateOrCreate(entry)
                    eventBus.post(WatchListChangedEvent)
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
