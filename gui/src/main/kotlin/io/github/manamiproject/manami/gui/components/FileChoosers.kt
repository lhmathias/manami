package io.github.manamiproject.manami.gui.components

import javafx.stage.DirectoryChooser
import javafx.stage.FileChooser
import javafx.stage.FileChooser.ExtensionFilter
import javafx.stage.Stage
import java.nio.file.Path

private const val FILE_SUFFIX_XML = "*.xml"

object FileChoosers {

    private val XML_FILTER = ExtensionFilter("XML", FILE_SUFFIX_XML)
    private val JSON_FILTER = ExtensionFilter("JSON", "*.json")
    private val XML_MAL_FILTER = ExtensionFilter("myanimelist.net", FILE_SUFFIX_XML)


    fun showOpenFileDialog(stage: Stage): Path? {
        val fileChooser = FileChooser().apply {
            title = "Select your anime list..."
            extensionFilters.addAll(XML_FILTER)
        }

        fileChooser.showOpenDialog(stage)?.let {
            return it.toPath()
        }

        return null
    }

    fun showImportFileDialog(stage: Stage): Path? {
        val fileChooser = FileChooser().apply {
            title = "Import file..."
            extensionFilters.addAll(XML_MAL_FILTER, JSON_FILTER)
        }

        fileChooser.showOpenDialog(stage)?.let {
            return it.toPath()
        }

        return null
    }

    fun showSaveAsFileDialog(stage: Stage): Path? {
        val fileChooser = FileChooser().apply {
            title = "Save your anime list as..."
            extensionFilters.addAll(XML_FILTER)
        }

        fileChooser.showSaveDialog(stage)?.let {
            return it.toPath()
        }

        return null
    }

    fun showExportDialog(stage: Stage): Path? {
        val fileChooser = FileChooser().apply {
            title = "Export anime list as..."
            extensionFilters.addAll(JSON_FILTER)
        }

        fileChooser.showSaveDialog(stage)?.let {
            return it.toPath()
        }

        return null
    }

    fun showBrowseForFolderDialog(stage: Stage): Path? {
        val directoryChooser = DirectoryChooser().apply {
            title = "Browse for directory..."
        }

        directoryChooser.showDialog(stage)?.let {
            return it.toPath()
        }

        return null
    }
}