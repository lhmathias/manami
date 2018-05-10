package io.github.manamiproject.manami.core.events.checklist

import io.github.manamiproject.manami.core.events.checklist.AbstractChecklistEvent.ChecklistEventType.ERROR
import io.github.manamiproject.manami.entities.Anime

class LocationIsEmptyChecklistEvent(
        animeEntry: Anime
) : AbstractChecklistEvent(animeEntry) {

    init {
        type = ERROR
        message = "Location is empty."
    }
}