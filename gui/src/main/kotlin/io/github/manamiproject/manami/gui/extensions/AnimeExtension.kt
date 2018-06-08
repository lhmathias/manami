package io.github.manamiproject.manami.gui.extensions

import io.github.manamiproject.manami.entities.Anime
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty

//FIXME: semms to be obsolete
fun Anime.titleProperty() = SimpleStringProperty(this.title)
fun Anime.episodesProperty() = SimpleIntegerProperty(this.episodes)
fun Anime.infoLinkProperty() = SimpleObjectProperty(this.infoLink)
fun Anime.typeProperty() = SimpleObjectProperty(this.type)