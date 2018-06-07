package io.github.manamiproject.manami

import io.github.manamiproject.manami.common.EventBus
import io.github.manamiproject.manami.core.Manami
import io.github.manamiproject.manami.gui.events.EventDispatcher
import io.github.manamiproject.manami.gui.views.SplashScreenView
import javafx.application.Application
import javafx.stage.Stage
import tornadofx.App
import tornadofx.runAsync


fun main(vararg args: String) {
    EventBus.register(EventDispatcher)
    Application.launch(Main::class.java, *args)
}


class Main : App(SplashScreenView::class) {

    override fun start(stage: Stage) {
        super.start(stage)

        runAsync {
            Manami
        }
    }
}