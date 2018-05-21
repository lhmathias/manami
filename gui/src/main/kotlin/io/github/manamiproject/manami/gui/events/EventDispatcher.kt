package io.github.manamiproject.manami.gui.events

import io.github.manamiproject.manami.gui.views.MainView
import tornadofx.Controller

object EventDispatcher: Controller() {

    val test: MainView by inject()

    init {
        test.open()
    }
}