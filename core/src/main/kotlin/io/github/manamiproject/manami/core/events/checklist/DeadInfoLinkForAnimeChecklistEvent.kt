package io.github.manamiproject.manami.core.events.checklist

import io.github.manamiproject.manami.entities.Anime

/**
 * This event indicates that an anime entry in the anime list contains an infolink which is dead.
 */
class DeadInfoLinkForAnimeChecklistEvent(
        animeEntry: Anime
) : AbstractChecklistEvent(animeEntry) {

    init {
        type = ChecklistEventType.WARNING
        message = "The infolink for this anime is a dead link [${animeEntry.title}]"
    }
}
