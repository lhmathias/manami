package io.github.manami.persistence.inmemory.animelist;

import io.github.manami.dto.comparator.MinimalEntryComByTitleAsc
import io.github.manami.dto.entities.Anime
import io.github.manami.dto.entities.Anime.Companion.isValidAnime
import io.github.manami.dto.entities.InfoLink
import io.github.manami.persistence.AnimeListHandler
import java.util.*

//FIXME: @Named
class InMemoryAnimeListHandler : AnimeListHandler {

    private val animeList: MutableMap<UUID, Anime> = mutableMapOf()


    override fun addAnime(anime: Anime): Boolean {
        if (!isValidAnime(anime) || isInList(anime)) {
            return false;
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
            animeList.map { infoLink == it.value.infoLink }
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
        if (isValidAnime(anime)) {
            animeList.put(anime.id, anime)
        }
    }
}
