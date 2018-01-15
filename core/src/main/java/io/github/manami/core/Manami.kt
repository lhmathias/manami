package io.github.manami.core

import io.github.manami.common.LoggerDelegate
import io.github.manami.core.commands.*
import io.github.manami.core.config.Config
import io.github.manami.core.tasks.*
import io.github.manami.dto.entities.*
import io.github.manami.persistence.ApplicationPersistence
import io.github.manami.persistence.PersistenceFacade
import io.github.manami.persistence.PersistenceHandler
import org.slf4j.Logger
import java.nio.file.Path
import java.nio.file.Paths


private const val FILE_SUFFIX_XML = ".xml"
private const val FILE_SUFFIX_JSON = ".json"


/**
 * Main access to the features of the application. This class has got delegation as well as operational functionality.
 */
object Manami : ApplicationPersistence{

    private val log: Logger by LoggerDelegate()
    private val cmdService: CommandService = CommandServiceImpl
    private val config = Config
    private val persistence: PersistenceHandler = PersistenceFacade
    private val taskConductor: TaskConductor = TaskConductorImpl


    fun newList() {
        resetCommandHistory()
        config.file = Paths.get("./")
        persistence.clearAll()
    }


    /**
     * Clears the command stacks, the anime list and unsets the file.
     */
    private fun resetCommandHistory() {
        cmdService.clearAll()
        cmdService.setUnsaved(false)
    }


    fun open(file: Path) {
        persistence.clearAll()
        persistence.open(file)
        config.file = file
        taskConductor.safelyStart(ThumbnailBackloadTask(persistence))
        taskConductor.safelyStart(CacheInitializationTask(persistence))
    }


    fun export(file: Path) {
        persistence.exportToJsonFile(file)
    }


    fun importFile(file: Path) {
        when {
            file.toString().endsWith(FILE_SUFFIX_XML) -> persistence.importMalFile(file)
            file.toString().endsWith(FILE_SUFFIX_JSON) -> persistence.importJsonFile(file)
        }
    }


    fun save() {
        if (persistence.save(config.file)) {
            cmdService.setUnsaved(false)
            cmdService.resetDirtyFlag()
        }
    }


    override fun filterAnime(anime: MinimalEntry): Boolean {
        return cmdService.executeCommand(CmdAddFilterEntry(FilterListEntry.valueOf(anime), persistence))
    }


    override fun fetchFilterList(): MutableList<FilterListEntry> {
        return persistence.fetchFilterList()
    }


    override fun filterListEntryExists(infoLink: InfoLink): Boolean {
        return persistence.filterListEntryExists(infoLink)
    }


    override fun removeFromFilterList(anime: MinimalEntry): Boolean {
        return cmdService.executeCommand(CmdDeleteFilterEntry(FilterListEntry.valueOf(anime), persistence))
    }


    override fun addAnime(anime: Anime): Boolean {
        return cmdService.executeCommand(CmdAddAnime(anime, persistence))
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
        return cmdService.executeCommand(CmdAddWatchListEntry(WatchListEntry.valueOf(anime), persistence))
    }


    override fun removeFromWatchList(anime: MinimalEntry): Boolean {
        return cmdService.executeCommand(CmdDeleteWatchListEntry(WatchListEntry.valueOf(anime), persistence))
    }


    override fun removeAnime(anime: Anime): Boolean {
        return cmdService.executeCommand(CmdDeleteAnime(anime, persistence))
    }


    override fun updateOrCreate(entry: MinimalEntry) {
        persistence.updateOrCreate(entry)
    }


    fun search(searchString: String) {
        if (searchString.isNotBlank()) {
            log.info("Initiated search for [{}]", searchString)
            taskConductor.safelyStart(SearchService(searchString, persistence))
        }
    }


    fun exportList(list: MutableList<Anime>, file: Path) {
        if (file.toString().endsWith(FILE_SUFFIX_JSON)) {
            persistence.exportListToJsonFile(list, file)
        }
    }
}