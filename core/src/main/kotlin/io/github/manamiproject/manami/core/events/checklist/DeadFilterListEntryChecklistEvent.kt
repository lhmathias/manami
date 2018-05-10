package io.github.manamiproject.manami.core.events.checklist

import io.github.manamiproject.manami.entities.FilterListEntry

class DeadFilterListEntryChecklistEvent(
        filterListEntry: FilterListEntry
) : AbstractChecklistEvent(filterListEntry) {

    init {
        type = ChecklistEventType.WARNING
        message = "The infolink for this filter list entry doesn't seem to exist anymore [${filterListEntry.infoLink}]."
    }
}