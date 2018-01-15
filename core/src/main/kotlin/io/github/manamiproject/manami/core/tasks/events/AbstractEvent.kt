package io.github.manamiproject.manami.core.tasks.events

import io.github.manamiproject.manami.dto.entities.Anime

abstract class AbstractEvent(
        override var anime: Anime? = null
) : Event {

    init {
        anime?.let {
            it.title.let {
                title = it
            }
        }
    }

    override var type: EventType = EventType.INFO

    override var title: String = ""

    override var message = ""

    enum class EventType {
        ERROR, WARNING, INFO;
    }
}
