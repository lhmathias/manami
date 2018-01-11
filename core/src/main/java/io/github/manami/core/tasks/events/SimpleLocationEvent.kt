package io.github.manami.core.tasks.events

import io.github.manami.dto.entities.Anime

class SimpleLocationEvent(
        private val animeEntry: Anime
) : AbstractEvent(animeEntry)