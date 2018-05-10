package io.github.manamiproject.manami.core.events.checklist

import io.github.manamiproject.manami.entities.WatchListEntry


internal class DeadWatchListEntryChecklistEvent(
        watchListEntry: WatchListEntry
) : AbstractChecklistEvent(watchListEntry) {

    init {
        type = ChecklistEventType.WARNING
        message = "The infolink for this watch list entry doesn't seem to exist anymore [${watchListEntry.infoLink}]."
    }
}