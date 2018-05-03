package io.github.manamiproject.manami.persistence.inmemory.animelist

import io.github.manamiproject.manami.entities.comparator.MinimalEntryCompByTitleAsc
import io.github.manamiproject.manami.entities.Anime
import io.github.manamiproject.manami.entities.InfoLink
import io.github.manamiproject.manami.persistence.AnimeList
import java.util.*
import java.util.concurrent.ConcurrentHashMap


internal class InMemoryAnimeList : AnimeList {

    private val animeList: MutableMap<UUID, Anime> = ConcurrentHashMap()


    override fun addAnime(anime: Anime): Boolean {
        if (!anime.isValid() || isInList(anime)) {
            return false
        }

        animeList[anime.id] = anime

        return true
    }


    override fun fetchAnimeList(): MutableList<Anime> {
        return animeList.values.sortedWith(MinimalEntryCompByTitleAsc).toMutableList()
    }


    override fun animeEntryExists(infoLink: InfoLink): Boolean {
        return isInList(infoLink)
    }

    private fun isInList(anime: Anime): Boolean {
        return animeList.containsKey(anime.id) || isInList(anime.infoLink)
    }


    private fun isInList(infoLink: InfoLink): Boolean {
        if (infoLink.isValid()) {
            animeList.values.firstOrNull { it.infoLink == infoLink }?.let {
                return true
            }
        }

        return false
    }


    override fun removeAnime(anime: Anime): Boolean {
        return animeList.remove(anime.id) != null
    }


    fun clear() {
        animeList.clear()
    }


    fun updateOrCreate(anime: Anime) {
        if (anime.isValid()) {
            animeList[anime.id] = anime
        }
    }
}
