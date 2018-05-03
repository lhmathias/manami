package io.github.manamiproject.manami.entities.comparator

import io.github.manamiproject.manami.entities.MinimalEntry
import java.util.*


object MinimalEntryCompByTitleAsc : Comparator<MinimalEntry> {

    override fun compare(objA: MinimalEntry, objB: MinimalEntry): Int {
        var ret = 0

        if (objA.title.isNotBlank() && objB.title.isNotBlank()) {
            ret = objA.title.compareTo(objB.title, ignoreCase = true)
        }

        return ret
    }
}
