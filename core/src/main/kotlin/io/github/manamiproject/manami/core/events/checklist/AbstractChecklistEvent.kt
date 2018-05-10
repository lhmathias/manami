package io.github.manamiproject.manami.core.events.checklist

import io.github.manamiproject.manami.entities.MinimalEntry

abstract class AbstractChecklistEvent(
        override var anime: MinimalEntry? = null
) : ChecklistEvent {

    init {
        anime?.let {
            it.title.let {
                title = it
            }
        }
    }

    override var type: ChecklistEventType = ChecklistEventType.INFO

    override var title: String = ""

    override var message = ""

    enum class ChecklistEventType {
        ERROR, WARNING, INFO;
    }
}
