package io.github.manami.persistence;

import java.util.List;

import io.github.manami.dto.entities.Anime;
import io.github.manami.dto.entities.MinimalEntry;

interface PersistenceHandler : ApplicationPersistence {

    fun clearAll();
    
    fun addAnimeList(list: MutableList<Anime>)

    fun addFilterList(list: MutableList<? extends MinimalEntry>)

    fun addWatchList(list: MutableList<? extends MinimalEntry>)
}
