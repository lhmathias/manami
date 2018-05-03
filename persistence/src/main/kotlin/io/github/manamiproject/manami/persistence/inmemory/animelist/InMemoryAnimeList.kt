package io.github.manamiproject.manami.persistence.inmemory.animelist

import io.github.manamiproject.manami.entities.comparator.MinimalEntryCompByTitleAsc
import io.github.manamiproject.manami.entities.Anime
import io.github.manamiproject.manami.entities.InfoLink
import io.github.manamiproject.manami.persistence.AnimeList
import java.util.*
import java.util.concurrent.ConcurrentHashMap


internal class InMemoryAnimeList : AnimeList {

    private val animeList: MutableMap<UUID, Anime> = ConcurrentHashMap()
    private val infoLinkIndex: MutableSet<InfoLink> = ConcurrentHashMap.newKeySet()


    override fun addAnime(anime: Anime): Boolean {
        if (!anime.isValid() || isInList(anime)) {
            return false
        }

        animeList[anime.id] = anime

        if(anime.infoLink.isValid()) {
            infoLinkIndex.add(anime.infoLink)
        }

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
        return if (infoLink.isValid()) {
            infoLinkIndex.contains(infoLink)
        } else {
            false
        }
    }


    override fun removeAnime(anime: Anime): Boolean {
        val removedEntry = animeList.remove(anime.id)

        return if(removedEntry != null) {
            if(removedEntry.infoLink.isValid()) {
                infoLinkIndex.remove(removedEntry.infoLink)
            }
            true
        } else {
            false
        }
    }


    fun clear() {
        animeList.clear()
        infoLinkIndex.clear()
    }


    fun updateOrCreate(anime: Anime) {
        if (anime.isValid()) {
            animeList[anime.id] = anime
            infoLinkIndex.add(anime.infoLink)
        }
    }
}
