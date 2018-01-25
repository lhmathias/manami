package io.github.manamiproject.manami.core.tasks.events

import java.nio.file.Path

data class ChecklistCrcDiffersEvent(
        private var path: Path,
        private var crcSum: String
) : AbstractChecklistEvent() {

    init {
        type = ChecklistEventType.ERROR
        title = path.fileName.toString()
        message = "CRC sum [$crcSum] differs for this file."
    }
}