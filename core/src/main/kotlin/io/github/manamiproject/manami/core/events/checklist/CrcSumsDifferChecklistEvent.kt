package io.github.manamiproject.manami.core.events.checklist

import io.github.manamiproject.manami.core.events.checklist.AbstractChecklistEvent.ChecklistEventType.ERROR
import java.nio.file.Path

data class CrcSumsDifferChecklistEvent(
        private var path: Path,
        private var crcSum: String
) : AbstractChecklistEvent() {

    init {
        type = ERROR
        title = path.fileName.toString()
        message = "Calculated CRC sum [$crcSum] differs from crc sum in filename."
    }
}