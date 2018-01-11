package io.github.manami.core.tasks.events

import io.github.manami.core.tasks.events.AbstractEvent.EventType
import io.github.manami.dto.entities.Anime


interface Event {

    var type: EventType
    var title: String
    var message: String
    var anime: Anime?
}
