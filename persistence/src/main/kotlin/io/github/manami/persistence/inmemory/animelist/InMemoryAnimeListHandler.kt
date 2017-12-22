package io.github.manami.persistence.inmemory.animelist;

import io.github.manami.dto.comparator.MinimalEntryComByTitleAsc
import io.github.manami.dto.entities.Anime
import io.github.manami.dto.entities.InfoLink
import io.github.manami.persistence.AnimeListHandler
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Named

@Named
class InMemoryAnimeListHandler : AnimeListHandler {

    private val animeList: MutableMap<UUID, Anime> = ConcurrentHashMap()


    override fun addAnime(anime: Anime): Boolean {
        if (!anime.isValidAnime() || isInList(anime)) {
            return false
        }

        animeList.put(anime.id, anime)

        return true
    }


    override fun fetchAnimeList(): MutableList<Anime> {
        val sortList: MutableList<Anime> = animeList.values.toMutableList()
        Collections.sort(sortList, MinimalEntryComByTitleAsc())
        return sortList.toMutableList()
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


    override fun removeAnime(id: UUID): Boolean {
        return animeList.remove(id) != null
    }


    fun clear() {
        animeList.clear()
    }


    fun updateOrCreate(anime: Anime) {
        if (anime.isValidAnime()) {
            animeList.put(anime.id, anime)
        }
    }
}
