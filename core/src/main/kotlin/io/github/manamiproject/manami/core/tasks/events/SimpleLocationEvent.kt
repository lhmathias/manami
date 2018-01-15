package io.github.manamiproject.manami.core.tasks.events

import io.github.manamiproject.manami.dto.entities.Anime

class SimpleLocationEvent(
        private val animeEntry: Anime
) : AbstractEvent(animeEntry)