package io.github.manami.core

import com.google.common.eventbus.EventBus
import io.github.manami.cache.Cache
import io.github.manami.core.commands.CommandService
import io.github.manami.core.config.Config
import io.github.manami.core.services.CacheInitializationService
import io.github.manami.core.services.SearchService
import io.github.manami.core.services.ServiceRepository
import io.github.manami.core.services.ThumbnailBackloadService
import io.github.manami.dto.LoggerDelegate
import io.github.manami.dto.entities.*
import io.github.manami.persistence.ApplicationPersistence
import io.github.manami.persistence.PersistenceFacade
import org.slf4j.Logger
import java.nio.file.Path
import java.util.*
import javax.inject.Inject
import javax.inject.Named


private const val FILE_SUFFIX_XML = ".xml"
private const val FILE_SUFFIX_JSON = ".json"


/**
 * Main access to the features of the application. This class has got delegation as well as operational functionality.
 */
@Named
class Manami @Inject constructor(
        private val cmdService: CommandService,
        private val config: Config,
        private val persistence: PersistenceFacade,
        private val serviceRepo: ServiceRepository,
        private val cache: Cache,
        private val eventBus: EventBus
) : ApplicationPersistence {

    private val log: Logger by LoggerDelegate()


    /**
     * Creates a new empty List.
     */
    fun newList() {
        resetCommandHistory()
        config.file = null
        persistence.clearAll()
    }


    /**
     * Clears the command stacks, the anime list and unsets the file.
     */
    private fun resetCommandHistory() {
        cmdService.clearAll()
        cmdService.setUnsaved(false)
    }


    /**
     * Opens a file.
     *
     * @param file File to open.
     */
    fun open(file: Path) {
        persistence.clearAll()
        persistence.open(file)
        config.file = file
        serviceRepo.startService(ThumbnailBackloadService(cache, persistence))
        serviceRepo.startService(CacheInitializationService(cache, persistence.fetchAnimeList()))
    }


    /**
     * Exports the file.
     *
     * @param file File to exportToJsonFile to.
     */
    fun export(file: Path) {
        persistence.exportToJsonFile(file)
    }


    /**
     * Imports a file either XML (MAL List) or JSON.
     *
     * @param file File to import.
     */
    fun importFile(file: Path) {
        when {
            file.toString().endsWith(FILE_SUFFIX_XML) -> persistence.importMalFile(file)
            file.toString().endsWith(FILE_SUFFIX_JSON) -> persistence.importJsonFile(file)
        }
    }


    /**
     * Saves the opened file.
     */
    fun save() {
        if (persistence.save(config.file)) {
            cmdService.setUnsaved(false)
            cmdService.resetDirtyFlag()
        }
    }


    override fun filterAnime(anime: MinimalEntry): Boolean {
        return persistence.filterAnime(anime)
    }


    override fun fetchFilterList(): MutableList<FilterListEntry> {
        return persistence.fetchFilterList()
    }


    override fun filterListEntryExists(infoLink: InfoLink): Boolean {
        return persistence.filterListEntryExists(infoLink)
    }


    override fun removeFromFilterList(infoLink: InfoLink): Boolean {
        return persistence.removeFromFilterList(infoLink)
    }


    override fun addAnime(anime: Anime): Boolean {
        return persistence.addAnime(anime)
    }


    /**
     * Does everything needed before exiting.
     */
    fun exit() {
        System.exit(0)
    }


    override fun fetchAnimeList(): MutableList<Anime> {
        return persistence.fetchAnimeList()
    }


    override fun animeEntryExists(infoLink: InfoLink): Boolean {
        return persistence.animeEntryExists(infoLink)
    }


    override fun fetchWatchList(): MutableList<WatchListEntry> {
        return persistence.fetchWatchList()
    }


    override fun watchListEntryExists(infoLink: InfoLink): Boolean {
        return persistence.watchListEntryExists(infoLink)
    }


    override fun watchAnime(anime: MinimalEntry): Boolean {
        return persistence.watchAnime(anime)
    }


    override fun removeFromWatchList(infoLink: InfoLink): Boolean {
        return persistence.removeFromWatchList(infoLink)
    }


    override fun removeAnime(id: UUID): Boolean {
        return persistence.removeAnime(id)
    }


    override fun updateOrCreate(entry: MinimalEntry) {
        persistence.updateOrCreate(entry)
    }


    /**
     * Searches for a specific string and fires an event with the search results.
     */
    fun search(searchString: String) {
        if (searchString.isNotBlank()) {
            log.info("Initiated search for [{}]", searchString)
            serviceRepo.startService(SearchService(searchString, persistence, eventBus))
        }
    }


    fun exportList(list: MutableList<Anime>, file: Path) {
        if (file.toString().endsWith(FILE_SUFFIX_JSON)) {
            persistence.exportListToJsonFile(list, file)
        }
    }
}