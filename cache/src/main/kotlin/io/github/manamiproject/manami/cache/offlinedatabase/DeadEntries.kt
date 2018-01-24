package io.github.manamiproject.manami.cache.offlinedatabase

internal data class DeadEntries(
        var mal: Set<Int>?,
        var anidb: Set<Int>?
)