package io.github.manamiproject.manami.gui.views.animelist

import io.github.manamiproject.manami.entities.Anime
import io.github.manamiproject.manami.entities.Location
import javafx.scene.control.TableCell
import javafx.scene.control.TableColumn
import javafx.scene.control.cell.TextFieldTableCell
import javafx.util.Callback
import javafx.util.converter.DefaultStringConverter

class LocationTableCellCallback : Callback<TableColumn<Anime, Location>, TableCell<Anime, Location>> {
    override fun call(param: TableColumn<Anime, Location>): TableCell<Anime, Location> {
        return object: TextFieldTableCell<Anime, Location>(DefaultStringConverter()) {

            override fun updateItem(title: String?, empty: Boolean) {
                title?.let {
                    if(title.isNotEmpty()) {
                        super.updateItem(title, empty)
                    }
                }
            }
        }
    }
}