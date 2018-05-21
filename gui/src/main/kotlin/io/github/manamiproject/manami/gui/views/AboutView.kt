package io.github.manamiproject.manami.gui.views

import io.github.manamiproject.manami.persistence.utility.ToolVersion.getToolVersion
import javafx.scene.control.Alert

object AboutView {

    fun showAbout() {
        val alert = Alert(Alert.AlertType.INFORMATION).apply {
            title = "About"
            headerText = "Version: ${getToolVersion()}"
            contentText = "Free non-commercial software. (AGPLv3)\n\nhttps://github.com/manami-project/manami"
        }

        alert.showAndWait()
    }
}