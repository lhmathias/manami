package io.github.manamiproject.manami.core.tasks.events

import java.nio.file.Path;

data class CrcEvent(
        var path: Path,
        var crcSum: String
) : AbstractEvent()