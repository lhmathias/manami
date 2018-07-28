package io.github.manamiproject.manami.gui.views.watchlist

import io.github.manamiproject.manami.core.Manami
import io.github.manamiproject.manami.entities.Title
import io.github.manamiproject.manami.entities.WatchListEntry
import io.github.manamiproject.manami.gui.components.HyperlinkBuilder.buildHyperlinkFrom
import io.github.manamiproject.manami.gui.components.Icons.createIconDelete
import io.github.manamiproject.manami.gui.components.Icons.createIconFilterList
import javafx.collections.FXCollections
import javafx.geometry.Pos.CENTER
import javafx.geometry.Pos.CENTER_LEFT
import javafx.scene.control.Tab
import javafx.scene.control.TabPane
import javafx.scene.control.TableColumn
import javafx.scene.text.Text
import tornadofx.*


private const val SPACER = 25.0

class WatchListTabView : View() {
    override val root = TabPane()

    private val manami = Manami
    private val watchListEntries = FXCollections.observableArrayList<WatchListEntry>()
    private var titleColumn: TableColumn<WatchListEntry, Title>? = null

    val tab = Tab("Watch List").apply {
        content = tableview(watchListEntries) {
            column("Thumbnail", WatchListEntry::thumbnail).cellFormat {
                graphic = imageview(this.rowItem.thumbnail.toString())
            }

            column("Title", WatchListEntry::title).apply {
                titleColumn = this
            }.cellFormat { title ->
                rowItem.infoLink.url?.let {
                    graphic = buildHyperlinkFrom(title, it)
                }
            }


            column("Actions", WatchListEntry::infoLink).cellFormat {
                val watchListEntry = rowItem

                graphic = hbox(spacing = 5, alignment = CENTER) {
                    button("", createIconFilterList()).action {
                        runAsync {
                            manami.filterAnime(watchListEntry)
                        }
                    }
                    button("", createIconDelete()).action {
                        runAsync {
                            manami.removeFromWatchList(watchListEntry)
                        }
                    }
                }
            }
        }
    }

    fun updateEntries() {
        watchListEntries.clear()
        watchListEntries.addAll(manami.fetchWatchList()) //FIXME: probably too expensive on huge lists
        resizeTitleColumn()
    }

    private fun resizeTitleColumn() {
        watchListEntries
                .map(WatchListEntry::title)
                .map { title -> Pair(title, title.length) }
                .maxBy { titleLengthPair -> titleLengthPair.second }
                ?.let { titleLengthPair ->
                    val width = Text(titleLengthPair.first).layoutBounds.width + SPACER
                    titleColumn?.prefWidth = width
                }
    }
}