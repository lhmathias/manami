package io.github.manamiproject.manami.core.tasks.events

import io.github.manamiproject.manami.entities.Anime

class ChecklistRelativizeLocationEvent(
        animeEntry: Anime,
        val relativizedPath: String
) : AbstractChecklistEvent(animeEntry) {

    init {
        type = ChecklistEventType.WARNING
        message = "This path can be converted to a relative path."
    }
}
