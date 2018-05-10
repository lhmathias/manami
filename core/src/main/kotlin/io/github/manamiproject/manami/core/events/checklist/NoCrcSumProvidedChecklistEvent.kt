package io.github.manamiproject.manami.core.events.checklist

import io.github.manamiproject.manami.core.events.checklist.AbstractChecklistEvent
import io.github.manamiproject.manami.core.events.checklist.AbstractChecklistEvent.ChecklistEventType.ERROR
import java.nio.file.Path

class NoCrcSumProvidedChecklistEvent(file: Path) : AbstractChecklistEvent() {

    init {
        type = ERROR
        title = file.fileName.toString()
        message = "File does not provide a CRC sum."
    }
}
