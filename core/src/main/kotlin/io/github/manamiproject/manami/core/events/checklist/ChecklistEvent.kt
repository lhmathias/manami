package io.github.manamiproject.manami.core.events.checklist

import io.github.manamiproject.manami.core.events.checklist.AbstractChecklistEvent.ChecklistEventType
import io.github.manamiproject.manami.entities.MinimalEntry


interface ChecklistEvent {

    var type: ChecklistEventType
    var title: String
    var message: String
    var anime: MinimalEntry?
}
