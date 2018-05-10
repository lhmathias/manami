package io.github.manamiproject.manami.core.events.checklist

import io.github.manamiproject.manami.core.events.checklist.AbstractChecklistEvent.ChecklistEventType.WARNING
import io.github.manamiproject.manami.entities.Anime

class LocationNumberOfFilesNotMatchEpisodesChecklistEvent(
        animeEntry: Anime
) : AbstractChecklistEvent(animeEntry) {

    init {
        type = WARNING
        message = "Number of files differs from number of episodes [${animeEntry.episodes}]."
    }
}
