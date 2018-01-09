package io.github.manami.core.config

import com.google.common.eventbus.EventBus
import io.github.manami.core.services.events.OpenedFileChangedEvent
import java.nio.file.Path
import java.nio.file.Paths
import javax.inject.Inject
import javax.inject.Named

/**
 * Contains the path for all configuration files as well as the path for the currently opened anime list file.
 */
@Named
class Config @Inject constructor(
        private val eventBus: EventBus
) {

    /**
     * File which is currently being worked on.
     */
    var file: Path = Paths.get(".")
        set(file) {
            field = file
            eventBus.post(OpenedFileChangedEvent())
        }
}
