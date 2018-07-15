package io.github.manamiproject.manami.gui.views

import javafx.scene.control.Alert
import javafx.scene.control.Alert.AlertType.CONFIRMATION
import javafx.scene.control.ButtonBar.ButtonData.*
import javafx.scene.control.ButtonType
import java.util.*

object UnsavedChangesDialogView {

    enum class DialogDecision {
        YES, NO, CANCEL
    }

    fun showUnsavedChangesDialog(): DialogDecision {
        val alert = Alert(CONFIRMATION).apply {
            title = "Unsaved Changes"
            headerText = "Your changes will be lost if you don't save them."
            contentText = "Do you want to save your changes?"
        }

        alert.buttonTypes.setAll(
            ButtonType("Yes", YES),
            ButtonType("No", NO),
            ButtonType("Cancel", CANCEL_CLOSE)
        )

        val result: Optional<ButtonType> = alert.showAndWait()

        if(!result.isPresent) {
            return DialogDecision.CANCEL
        }

        return when(result.get().buttonData) {
            YES -> DialogDecision.YES
            NO -> DialogDecision.NO
            else -> DialogDecision.CANCEL
        }
    }
}