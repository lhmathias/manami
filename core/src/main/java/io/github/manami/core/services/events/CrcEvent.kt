package io.github.manami.core.services.events;

import java.nio.file.Path;

data class CrcEvent(
        var path: Path,
        var crcSum: String
) : AbstractEvent()