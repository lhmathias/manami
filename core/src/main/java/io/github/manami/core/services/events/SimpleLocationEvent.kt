package io.github.manami.core.services.events

import io.github.manami.dto.entities.Anime

class SimpleLocationEvent(
        private val animeEntry: Anime
) : AbstractEvent(animeEntry)