package io.github.manamiproject.manami.gui.views.animelist

import io.github.manamiproject.manami.entities.Anime
import io.github.manamiproject.manami.entities.Title
import javafx.scene.control.TableCell
import javafx.scene.control.TableColumn
import javafx.scene.control.cell.TextFieldTableCell
import javafx.util.Callback
import javafx.util.converter.DefaultStringConverter

class TitleTableCellCallback : Callback<TableColumn<Anime, Title>, TableCell<Anime, Title>> {
    override fun call(param: TableColumn<Anime, Title>): TableCell<Anime, Title> {
        return object: TextFieldTableCell<Anime, Title>(DefaultStringConverter()) {

            override fun updateItem(title: String?, empty: Boolean) {
                title?.let {
                    if(title.isNotEmpty()) {
                        super.updateItem(title, empty)
                    } else {
                        super.updateItem(item, empty)
                    }
                }
            }
        }
    }
}