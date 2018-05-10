package io.github.manamiproject.manami.core.events.checklist

import io.github.manamiproject.manami.core.events.checklist.AbstractChecklistEvent.ChecklistEventType.WARNING
import io.github.manamiproject.manami.entities.Anime

class RelativizeLocationChecklistEvent(
        animeEntry: Anime,
        val relativizedPath: String
) : AbstractChecklistEvent(animeEntry) {

    init {
        type = WARNING
        message = "This path can be converted to a relative path."
    }
}
