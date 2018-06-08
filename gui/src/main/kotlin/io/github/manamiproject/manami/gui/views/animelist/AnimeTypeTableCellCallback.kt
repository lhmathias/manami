package io.github.manamiproject.manami.gui.views.animelist

import io.github.manamiproject.manami.entities.Anime
import io.github.manamiproject.manami.entities.AnimeType
import javafx.scene.control.TableCell
import javafx.scene.control.TableColumn
import javafx.scene.control.cell.ChoiceBoxTableCell
import javafx.util.Callback
import javafx.util.StringConverter

class AnimeTypeTableCellCallback : Callback<TableColumn<Anime, AnimeType>, TableCell<Anime, AnimeType>> {

    override fun call(tableColumn: TableColumn<Anime, AnimeType>): TableCell<Anime, AnimeType> {
        return object: ChoiceBoxTableCell<Anime, AnimeType>(AnimeTypeStringConverter()) {

            init {
                items.clear()

                AnimeType.values().forEach {
                    items.add(it)
                }
            }
        }
    }
}

private class AnimeTypeStringConverter : StringConverter<AnimeType>() {
    override fun toString(obj: AnimeType?) = obj?.value

    override fun fromString(string: String?) = string?.let { return@let AnimeType.findByName(string) }
}