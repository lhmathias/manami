package io.github.manamiproject.manami.core.events.checklist

import io.github.manamiproject.manami.core.events.checklist.AbstractChecklistEvent.ChecklistEventType.ERROR
import io.github.manamiproject.manami.entities.Anime

class LocationNotSetChecklistEvent(
        animeEntry: Anime
) : AbstractChecklistEvent(animeEntry) {

    init {
        type = ERROR
        message = "Location has not been set."
    }
}
