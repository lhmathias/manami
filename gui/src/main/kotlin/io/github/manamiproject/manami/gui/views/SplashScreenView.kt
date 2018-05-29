package io.github.manamiproject.manami.gui.views

import io.github.manamiproject.manami.common.EventBus
import tornadofx.View
import tornadofx.label
import tornadofx.runLater
import tornadofx.stackpane

class SplashScreenView : View("Please wait") {

    init {
        EventBus.register(this)
    }

    override val root = stackpane {
        setPrefSize(300.0, 100.0)
        label("Loading...")
    }

    fun replaceWithMainView() {
        runLater {
            replaceWith<MainView>()
        }
    }
}