package io.github.manami.persistence

import io.github.manami.dto.entities.MinimalEntry

interface ApplicationPersistence : AnimeListHandler, WatchListHandler, FilterListHandler {

    fun updateOrCreate(entry: MinimalEntry)
}
