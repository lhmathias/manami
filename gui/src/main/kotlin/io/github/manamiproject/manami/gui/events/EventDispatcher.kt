package io.github.manamiproject.manami.gui.events

import com.google.common.eventbus.Subscribe
import io.github.manamiproject.manami.cache.offlinedatabase.OfflineDatabaseUpdatedSuccessfullyEvent
import io.github.manamiproject.manami.core.events.OpenedFileChangedEvent
import io.github.manamiproject.manami.gui.views.MainView
import io.github.manamiproject.manami.gui.views.SplashScreenView
import tornadofx.Controller

object EventDispatcher: Controller() {

    private val splashScreenView: SplashScreenView by inject()
    private val mainView: MainView by inject()

    @Subscribe
    fun offlineDatabaseSuccessfullyUpdated(obj: OfflineDatabaseUpdatedSuccessfullyEvent) {
        splashScreenView.replaceWithMainView()
    }

    @Subscribe
    fun openFileChanged(obj: OpenedFileChangedEvent) {
        mainView.fileChanged(obj.fileName)
    }
}