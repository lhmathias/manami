package io.github.manami.persistence

import io.github.manami.dto.entities.MinimalEntry

interface ApplicationPersistence : AnimeList, WatchList, FilterList {

    fun updateOrCreate(entry: MinimalEntry)
}
