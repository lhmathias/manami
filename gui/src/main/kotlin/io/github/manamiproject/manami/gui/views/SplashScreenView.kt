package io.github.manamiproject.manami.gui.views

import com.google.common.eventbus.Subscribe
import io.github.manamiproject.manami.cache.offlinedatabase.OfflineDatabaseUpdatedSuccessfullyEvent
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

    @Subscribe
    fun offlineDatabaseSuccessfullyUpdated(obj: OfflineDatabaseUpdatedSuccessfullyEvent) {
        runLater {
            replaceWith<MainView>()
        }
    }
}