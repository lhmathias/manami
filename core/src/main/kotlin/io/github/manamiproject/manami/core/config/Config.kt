package io.github.manamiproject.manami.core.config

import io.github.manamiproject.manami.common.EventBus
import io.github.manamiproject.manami.core.events.OpenedFileChangedEvent
import java.nio.file.Path
import java.nio.file.Paths

/**
 * Contains the path for the currently opened anime list file.
 */
internal object Config {

    /**
     * File which is currently being worked on.
     */
    var file: Path = Paths.get("./")
        set(file) {
            field = file
            EventBus.publish(OpenedFileChangedEvent)
        }
}
