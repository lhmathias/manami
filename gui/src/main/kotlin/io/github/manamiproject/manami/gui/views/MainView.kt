package io.github.manamiproject.manami.gui.views

import io.github.manamiproject.manami.core.Manami
import io.github.manamiproject.manami.entities.Anime
import io.github.manamiproject.manami.entities.InfoLink
import io.github.manamiproject.manami.gui.components.FileChoosers
import io.github.manamiproject.manami.gui.components.Icons.createIconDelete
import io.github.manamiproject.manami.gui.components.Icons.createIconExit
import io.github.manamiproject.manami.gui.components.Icons.createIconExport
import io.github.manamiproject.manami.gui.components.Icons.createIconFile
import io.github.manamiproject.manami.gui.components.Icons.createIconFileText
import io.github.manamiproject.manami.gui.components.Icons.createIconFilterList
import io.github.manamiproject.manami.gui.components.Icons.createIconFolderOpen
import io.github.manamiproject.manami.gui.components.Icons.createIconImport
import io.github.manamiproject.manami.gui.components.Icons.createIconQuestion
import io.github.manamiproject.manami.gui.components.Icons.createIconRedo
import io.github.manamiproject.manami.gui.components.Icons.createIconSave
import io.github.manamiproject.manami.gui.components.Icons.createIconUndo
import io.github.manamiproject.manami.gui.components.Icons.createIconWatchList
import javafx.scene.Parent
import javafx.scene.control.*
import tornadofx.View
import java.nio.file.Path
import java.nio.file.Paths

private const val FILE_SUFFIX_XML = ".xml"

class MainView : View() {

    private val manami = Manami

    override val root: Parent by fxml()
    private val tvAnimeList: TableView<Anime> by fxid()
    private val colAnimeListNumber: TableColumn<Anime, Anime> by fxid()
    private val colAnimeListTitle: TableColumn<Anime, String> by fxid()
    private val colAnimeListType: TableColumn<Anime, String> by fxid()
    private val colAnimeListEpisodes: TableColumn<Anime, Int> by fxid()
    private val colAnimeListLink: TableColumn<Anime, InfoLink> by fxid()
    private val colAnimeListLocation: TableColumn<Anime, String> by fxid()
    private val tabPane: TabPane by fxid()
    private val tabAnimeList: Tab by fxid()
    private val miNewList: MenuItem by fxid()
    private val miNewEntry: MenuItem by fxid()
    private val miOpen: MenuItem by fxid()
    private val miImport: MenuItem by fxid()
    private val miCheckList: MenuItem by fxid()
    private val miSave: MenuItem by fxid()
    private val miSaveAs: MenuItem by fxid()
    private val miExit: MenuItem by fxid()
    private val miRedo: MenuItem by fxid()
    private val miUndo: MenuItem by fxid()
    private val miExport: MenuItem by fxid()
    private val miDeleteEntry: MenuItem by fxid()
    private val cmiDeleteEntry: MenuItem by fxid()
    private val miRelatedAnime: MenuItem by fxid()
    private val miRecommendations: MenuItem by fxid()
    private val miFilterList: MenuItem by fxid()
    private val miWatchList: MenuItem by fxid()
    private val miTagList: MenuItem by fxid()
    private val miAbout: MenuItem by fxid()
    private val txtSearchString: TextField by fxid()
    private val btnSearch: Button by fxid()

    init {
        this.primaryStage.isMaximized = true
        initMenuItemGlyphs()
    }

    private fun initMenuItemGlyphs() {
        miNewList.graphic = createIconFileText()
        miNewEntry.graphic = createIconFile()
        miOpen.graphic = createIconFolderOpen()
        miSave.graphic = createIconSave()
        miImport.graphic = createIconImport()
        miExport.graphic = createIconExport()
        miExit.graphic = createIconExit()
        miUndo.graphic = createIconUndo()
        miRedo.graphic = createIconRedo()
        miDeleteEntry.graphic = createIconDelete()
        miFilterList.graphic = createIconFilterList()
        miWatchList.graphic = createIconWatchList()
        miAbout.graphic = createIconQuestion()
        cmiDeleteEntry.graphic = createIconDelete()
    }

    fun exit() {
        //TODO: Check file is unsaved
        manami.exit()
    }

    fun deleteEntry() {}

    fun newList() {}

    fun showNewEntry() {}

    fun open() {
        FileChoosers.showOpenFileDialog(primaryStage)?.let {
            //TODO: check for open file
            //TODO: clear everything
            manami.open(it)
        }
    }

    fun importFile() {
        FileChoosers.showImportFileDialog(primaryStage)?.let {
            //TODO: check for open file
            manami.importFile(it)
        }
    }

    fun export() {
        FileChoosers.showExportDialog(primaryStage)?.let {
            manami.export(it)
        }
    }

    fun save() {
        manami.save()
    }

    fun saveAs() {
        FileChoosers.showSaveAsFileDialog(primaryStage)?.let {
            val file: Path = it

            if(it.endsWith(FILE_SUFFIX_XML) || it.endsWith(FILE_SUFFIX_XML.toUpperCase())) {
                Paths.get("${it.fileName}${FILE_SUFFIX_XML}")
            }

            manami.saveAs(file)
        }
    }

    fun undo() {}

    fun redo() {}

    fun showRecommendationsTab() {}

    fun showRelatedAnimeTab() {}

    fun showTagListTab() {}

    fun showCheckListTab() {}

    fun showFilterTab() {}

    fun showWatchListTab() {}
    
    fun showAbout() {
        AboutView.showAbout()
    }
}