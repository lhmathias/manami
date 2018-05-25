package io.github.manamiproject.manami.persistence

import io.github.manamiproject.manami.entities.MinimalEntry

interface ExternalPersistence : AnimeList, WatchList, FilterList {

    fun updateOrCreate(entry: MinimalEntry)
}
