package io.github.manamiproject.manami.gui.views

import javafx.scene.Parent
import javafx.scene.layout.BorderPane
import tornadofx.View

internal class MainView: View() {

    val borderPane: BorderPane by fxml()

    override val root: Parent
        get() = borderPane

    init {
        this.primaryStage.isMaximized = true
    }


    fun deleteEntry() {}

    fun newList() {}

    fun showNewEntry() {}

    fun open() {}

    fun importFile() {}

    fun export() {}

    fun save() {}

    fun saveAs() {}

    fun exit() {}

    fun undo() {}

    fun redo() {}

    fun showRecommendationsTab() {}

    fun showRelatedAnimeTab() {}

    fun showTagListTab() {}

    fun showCheckListTab() {}

    fun showFilterTab() {}

    fun showWatchListTab() {}
    
    fun showAbout() {}
}