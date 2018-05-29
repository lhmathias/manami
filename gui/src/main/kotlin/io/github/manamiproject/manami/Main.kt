package io.github.manamiproject.manami

import io.github.manamiproject.manami.core.Manami
import io.github.manamiproject.manami.gui.views.SplashScreenView
import javafx.application.Application
import javafx.stage.Stage
import tornadofx.App
import tornadofx.find
import tornadofx.runAsync


fun main(vararg args: String) {
    Application.launch(Main::class.java, *args)
}


class Main : App(SplashScreenView::class) {

    override fun start(stage: Stage) {
        super.start(stage)

        find<SplashScreenView>().openModal()

        runAsync {
            Manami
        }
    }
}