package io.github.manamiproject.manami

import com.google.common.eventbus.Subscribe
import io.github.manamiproject.manami.cache.offlinedatabase.OfflineDatabaseUpdatedSuccessfullyEvent
import io.github.manamiproject.manami.common.EventBus
import io.github.manamiproject.manami.core.Manami
import io.github.manamiproject.manami.gui.views.MainView
import javafx.application.Application
import javafx.stage.Stage
import tornadofx.*


fun main(vararg args: String) {
    Application.launch(Main::class.java, *args)
}


class Main : App(SplashScreen::class) {

    override fun start(stage: Stage) {
        super.start(stage)

        find<SplashScreen>().openModal()

        runAsync {
            Manami
        }
    }
}


class SplashScreen : View("Please wait") {

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