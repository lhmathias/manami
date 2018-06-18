package io.github.manamiproject.manami.gui.views.animelist

import io.github.manamiproject.manami.core.Manami
import io.github.manamiproject.manami.entities.*
import javafx.event.EventHandler
import javafx.scene.control.*
import tornadofx.*

class AnimeListTabView : View() {
    override val root = TabPane()

    val tab = Tab("Anime List").apply {
        content = tableview(Manami.fetchAnimeList().observable()) {
            isEditable = true

            column("Title", Anime::title).apply {
                isEditable = true
                cellFactory = TitleTableCellCallback()
                comparator = Comparator { o1, o2 -> o1.compareTo(o2, ignoreCase = true) }
                onEditCommit = EventHandler {
                    Manami.changeTitle(it.rowValue, it.newValue as Title)
                }
            }

            column("Type", Anime::type).apply {
                isEditable = true
                cellFactory = AnimeTypeTableCellCallback()
                comparator = Comparator { o1, o2 -> o1.value.compareTo(o2.value) }
                onEditCommit = EventHandler {
                    Manami.changeType(it.rowValue, it.newValue as AnimeType)
                }
            }

            column("Episodes", Anime::episodes).apply {
                isEditable = true
                cellFactory = EpisodesTableCellCallback()
                comparator = Comparator { o1, o2 -> o1.compareTo(o2) }
                onEditCommit = EventHandler {
                    Manami.changeEpisodes(it.rowValue, it.newValue)
                }
            }

            column("InfoLink", Anime::infoLink).apply {
                isEditable = true
                cellFactory = InfoLinkTableCellCallback()
                comparator = Comparator { o1, o2 -> o1.toString().compareTo(o2.toString()) }
                onEditCommit = EventHandler {
                    Manami.changeInfoLink(it.rowValue, it.newValue)
                }
            }

            column("Location", Anime::location).apply {
                isEditable = true
                cellFactory = LocationTableCellCallback()
                comparator = Comparator { o1, o2 -> o1.compareTo(o2) }
                onEditCommit = EventHandler {
                    Manami.changeLocation(it.rowValue, it.newValue)
                }
            }

            //cmiDeleteEntry.graphic = Icons.createIconDelete()
            //TODO: contextmenu entry for deleting an entry
        }
    }
}