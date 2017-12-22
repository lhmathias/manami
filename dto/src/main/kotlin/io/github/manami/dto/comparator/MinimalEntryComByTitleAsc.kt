package io.github.manami.dto.comparator

import io.github.manami.dto.entities.MinimalEntry
import java.util.*


class MinimalEntryComByTitleAsc : Comparator<MinimalEntry> {

    override fun compare(objA: MinimalEntry, objB: MinimalEntry): Int {
        var ret = 0

        if (objA.title.isNotBlank() && objB.title.isNotBlank()) {
            ret = objA.title.compareTo(objB.title, ignoreCase = true)
        }

        return ret
    }
}
