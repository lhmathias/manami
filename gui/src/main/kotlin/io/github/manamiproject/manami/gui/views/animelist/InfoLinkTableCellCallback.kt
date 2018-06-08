package io.github.manamiproject.manami.gui.views.animelist

import io.github.manamiproject.manami.entities.Anime
import io.github.manamiproject.manami.entities.InfoLink
import javafx.scene.control.TableCell
import javafx.scene.control.TableColumn
import javafx.scene.control.cell.TextFieldTableCell
import javafx.util.Callback
import javafx.util.StringConverter

class InfoLinkTableCellCallback : Callback<TableColumn<Anime, InfoLink>, TableCell<Anime, InfoLink>> {

    override fun call(tableColumn: TableColumn<Anime, InfoLink>): TableCell<Anime, InfoLink> {
        return object: TextFieldTableCell<Anime, InfoLink>(InfoLinkStringConverter()) {

            override fun updateItem(newInfoLink: InfoLink?, empty: Boolean) {
                newInfoLink?.let {
                    if(it.isValid() || it.toString().isEmpty()) {
                        super.updateItem(it, empty)
                    }
                }
            }
        }
    }
}

private class InfoLinkStringConverter : StringConverter<InfoLink>() {
    override fun toString(obj: InfoLink?) = obj?.toString()

    override fun fromString(string: String?) = string?.let { return@let InfoLink(it) }
}