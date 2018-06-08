package io.github.manamiproject.manami.gui.views.animelist

import io.github.manamiproject.manami.entities.Anime
import io.github.manamiproject.manami.entities.Episodes
import javafx.scene.control.TableCell
import javafx.scene.control.TableColumn
import javafx.scene.control.cell.TextFieldTableCell
import javafx.util.Callback
import javafx.util.StringConverter
import tornadofx.isInt


class EpisodesTableCellCallback : Callback<TableColumn<Anime, Episodes>, TableCell<Anime, Episodes>> {

    override fun call(tableColumn: TableColumn<Anime, Episodes>): TableCell<Anime, Episodes> {
        return object: TextFieldTableCell<Anime, Episodes>(EpisodesStringConverter()) {
        }
    }
}

class EpisodesStringConverter : StringConverter<Episodes>() {
    override fun toString(value: Episodes?) = value?.toString()

    override fun fromString(string: String?): Episodes {
        string?.let {
            if (string.isInt()) {
                return string.toInt()
            }
        }

        return 1
    }
}
