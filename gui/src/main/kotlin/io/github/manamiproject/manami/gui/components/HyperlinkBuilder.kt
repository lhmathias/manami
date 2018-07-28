package io.github.manamiproject.manami.gui.components

import javafx.event.EventHandler
import javafx.scene.control.Hyperlink
import javafx.scene.input.MouseButton.PRIMARY
import java.awt.Desktop.getDesktop
import java.net.URL

object HyperlinkBuilder {

    fun buildHyperlinkFrom(caption: String, url: URL): Hyperlink {
        val hyperlink = Hyperlink(caption)

        hyperlink.onMouseClicked = EventHandler { event ->
            if (event.button == PRIMARY) {
                getDesktop().browse(url.toURI())
            }
        }

        return hyperlink
    }
}