package io.github.manami.core.config

import io.github.manami.common.EventBus
import io.github.manami.core.tasks.events.OpenedFileChangedEvent
import java.nio.file.Path
import java.nio.file.Paths

/**
 * Contains the path for all configuration files as well as the path for the currently opened anime list file.
 */
internal object Config {

    /**
     * File which is currently being worked on.
     */
    var file: Path = Paths.get("./")
        set(file) {
            field = file
            EventBus.publish(OpenedFileChangedEvent())
        }
}
