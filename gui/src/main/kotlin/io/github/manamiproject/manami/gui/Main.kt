package io.github.manamiproject.manami.gui

import io.github.manamiproject.manami.gui.views.MainView
import javafx.application.Application
import tornadofx.App


/**
 * Entry point of the application.
 */
class Main : App(MainView::class)

fun main(vararg args: String) {
  Application.launch(Main::class.java, *args)
}
