package io.github.manamiproject.manami.core

import io.github.manamiproject.manami.cache.Cache
import io.github.manamiproject.manami.cache.CacheFacade
import io.github.manamiproject.manami.common.LoggerDelegate
import io.github.manamiproject.manami.common.isValidFile
import io.github.manamiproject.manami.core.config.Config
import io.github.manamiproject.manami.core.commands.*
import io.github.manamiproject.manami.core.config.ConfigFileWatchdog
import io.github.manamiproject.manami.core.tasks.*
import io.github.manamiproject.manami.core.tasks.recommendations.RecommendationsCacheInitializationTask
import io.github.manamiproject.manami.core.tasks.search.SearchTask
import io.github.manamiproject.manami.core.tasks.thumbnails.ThumbnailBackloadTask
import io.github.manamiproject.manami.entities.*
import io.github.manamiproject.manami.persistence.ExternalPersistence
import io.github.manamiproject.manami.persistence.PersistenceFacade
import io.github.manamiproject.manami.persistence.Persistence
import org.slf4j.Logger
import java.nio.file.Path
import java.nio.file.Paths


private const val FILE_SUFFIX_XML = ".xml"
private const val FILE_SUFFIX_JSON = ".json"
private const val CURRENT_DIR = "."


object Manami : StatefulApplication, AnimeDataAccess, ExternalPersistence, AnimeModifier {

    private val log: Logger by LoggerDelegate()
    private val cmdService: CommandService = CommandServiceImpl
    private val config = Config
    private val persistence: Persistence = PersistenceFacade
    private val taskConductor: TaskConductor = TaskConductorImpl
    private val cache: Cache = CacheFacade


    fun init() {
        ConfigFileWatchdog(Paths.get(CURRENT_DIR)).validate()
    }


    override fun newList() {
        resetCommandHistory()
        config.file = Paths.get(CURRENT_DIR)
        persistence.clearAll()
    }


    private fun resetCommandHistory() {
        cmdService.clearAll()
        cmdService.setUnsaved(false)
    }


    override fun open(file: Path) {
        if(file.isValidFile()) {
            taskConductor.cancelAllTasks()
            persistence.clearAll()
            resetCommandHistory()
            persistence.open(file)
            config.file = file
            taskConductor.safelyStart(ThumbnailBackloadTask(persistence))
            taskConductor.safelyStart(RecommendationsCacheInitializationTask(persistence))
        }
    }


    override fun export(file: Path) {
        if(file.isValidFile()) {
            persistence.exportToJsonFile(file)
        }
    }


    override fun importFile(file: Path) {
        when {
            file.toString().endsWith(FILE_SUFFIX_XML) -> persistence.importMalFile(file)
            file.toString().endsWith(FILE_SUFFIX_JSON) -> persistence.importJsonFile(file)
        }
    }

    override fun saveAs(file: Path) {
        config.file = file
        save()
    }

    override fun save() {
        if (persistence.save(config.file)) {
            cmdService.setUnsaved(false)
        }
    }


    override fun filterAnime(anime: MinimalEntry): Boolean {
        return cmdService.executeCommand(CmdAddFilterEntry(FilterListEntry.valueOf(anime), persistence))
    }


    override fun fetchFilterList(): List<FilterListEntry> {
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


    override fun exit() {
        System.exit(0)
    }


    override fun fetchAnimeList(): List<Anime> {
        return persistence.fetchAnimeList()
    }


    override fun animeEntryExists(infoLink: InfoLink): Boolean {
        return persistence.animeEntryExists(infoLink)
    }


    override fun fetchWatchList(): List<WatchListEntry> {
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


    override fun search(searchString: String) {
        if (searchString.isNotBlank()) {
            log.info("Initiated search for [{}]", searchString)
            taskConductor.safelyStart(SearchTask(searchString, persistence))
        }
    }


    override fun exportList(list: List<Anime>, file: Path) {
        if (file.toString().endsWith(FILE_SUFFIX_JSON)) {
            persistence.exportListToJsonFile(list, file)
        }
    }


    override fun undo() = cmdService.undo()


    override fun redo() = cmdService.redo()


    override fun isFileUnsaved() = cmdService.isUnsaved()


    override fun fetchAnime(infoLink: InfoLink) = cache.fetchAnime(infoLink)


    override fun getConfigFile() = Config.file


    override fun changeTitle(anime: Anime, newTitle: Title) {
        cmdService.executeCommand(CmdChangeTitle(anime, newTitle, persistence))
    }


    override fun changeType(anime: Anime, newType: AnimeType) {
        cmdService.executeCommand(CmdChangeType(anime, newType, persistence))
    }


    override fun changeEpisodes(anime: Anime, newNumberOfEpisodes: Episodes) {
        cmdService.executeCommand(CmdChangeEpisodes(anime, newNumberOfEpisodes, persistence))
    }


    override fun changeInfoLink(anime: Anime, newInfoLink: InfoLink) {
        cmdService.executeCommand(CmdChangeInfoLink(anime, newInfoLink, persistence))
    }


    override fun changeLocation(anime: Anime, newLocation: Location) {
        cmdService.executeCommand(CmdChangeLocation(anime, newLocation, persistence))
    }


    override fun doneCommandsExist() = !cmdService.isEmptyDoneCommands()


    override fun undoneCommandsExist() = !cmdService.isEmptyUndoneCommands()


    fun invalidateCache() = cache.invalidate()
}