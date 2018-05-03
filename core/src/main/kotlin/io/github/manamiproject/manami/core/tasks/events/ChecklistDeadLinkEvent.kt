package io.github.manamiproject.manami.core.tasks.events

import io.github.manamiproject.manami.entities.MinimalEntry


internal class ChecklistDeadLinkEvent(
        animeEntry: MinimalEntry
) : AbstractChecklistEvent(animeEntry) {

    init {
        type = ChecklistEventType.WARNING
        message = "The infolink doesn't seem to exist anymore [${animeEntry.infoLink}]."
    }
}
