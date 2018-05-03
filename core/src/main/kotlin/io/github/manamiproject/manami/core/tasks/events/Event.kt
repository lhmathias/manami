package io.github.manamiproject.manami.core.tasks.events

import io.github.manamiproject.manami.core.tasks.events.AbstractChecklistEvent.ChecklistEventType
import io.github.manamiproject.manami.entities.MinimalEntry


interface Event {

    var type: ChecklistEventType
    var title: String
    var message: String
    var anime: MinimalEntry?
}
