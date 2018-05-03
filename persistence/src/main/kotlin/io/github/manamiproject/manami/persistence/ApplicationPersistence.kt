package io.github.manamiproject.manami.persistence

import io.github.manamiproject.manami.entities.MinimalEntry

interface ApplicationPersistence : AnimeList, WatchList, FilterList {

    fun updateOrCreate(entry: MinimalEntry)
}
