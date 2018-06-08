package io.github.manamiproject.manami.gui.views.animelist

import io.github.manamiproject.manami.core.Manami
import io.github.manamiproject.manami.entities.*
import javafx.scene.control.*
import tornadofx.*

class AnimeListTabView : View() {
    override val root = TabPane()

    //cmiDeleteEntry.graphic = Icons.createIconDelete()
    val tab = Tab("Anime List").apply {
        content = tableview(Manami.fetchAnimeList().observable()) {
            isEditable = true

            column("Title", Anime::title).apply {
                cellFactory = TitleTableCellCallback()
                isEditable = true
            }

            column("Type", Anime::type).apply {
                cellFactory = AnimeTypeTableCellCallback()
                isEditable = true
            }

            column("Episodes", Anime::episodes).apply {
                cellFactory = EpisodesTableCellCallback()
                isEditable = true
            }

            column("InfoLink", Anime::infoLink).apply {
                cellFactory = InfoLinkTableCellCallback()
                isEditable = true
            }

            column("Location", Anime::location).apply {
                cellFactory = LocationTableCellCallback()
                isEditable = true
            }

            //TODO: contextmenu entry for deleting an entry
        }
    }
}