package io.github.manamiproject.manami.gui

import io.github.manamiproject.manami.core.Manami
import io.github.manamiproject.manami.gui.views.MainView
import javafx.application.Application
import javafx.application.Platform
import javafx.event.EventHandler
import javafx.stage.Stage
import tornadofx.App


/**
 * Entry point of the application.
 */
class Main : App(MainView::class) {

    override fun start(stage: Stage) {
        super.start(stage)
        initNativeCloseButton(stage)
    }

    private fun initNativeCloseButton(stage: Stage) {
        Platform.setImplicitExit(false)
        stage.onCloseRequest = EventHandler {
            Manami.exit()
        }
    }
}

fun main(vararg args: String) {
    Application.launch(Main::class.java, *args)
}
