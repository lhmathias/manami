package io.github.manamiproject.manami.core.commands

import io.github.manamiproject.manami.entities.Anime
import io.github.manamiproject.manami.entities.FilterListEntry
import io.github.manamiproject.manami.entities.MinimalEntry
import io.github.manamiproject.manami.entities.WatchListEntry
import io.github.manamiproject.manami.persistence.Persistence
import java.net.URL

internal class CmdChangeThumbnail(
        private val anime: MinimalEntry,
        private val newThumbnail: URL,
        private val persistence: Persistence
) : Command {


    override fun execute(): Boolean {
        anime.thumbnail = newThumbnail

        return when (anime) {
            is Anime -> updateAnimeListEntry(anime)
            is WatchListEntry -> updateWatchListEntry(anime)
            is FilterListEntry -> updateFilterListEntry(anime)
            else -> false
        }
    }

    private fun updateAnimeListEntry(anime: Anime): Boolean {
        val isAnimeRemoved = persistence.removeAnime(anime)
        val isAnimeAddedToAnimeList = persistence.addAnime(anime.copy(thumbnail = newThumbnail))

        return isAnimeRemoved && isAnimeAddedToAnimeList
    }

    private fun updateWatchListEntry(anime: WatchListEntry): Boolean {
        val isAnimeRemoved = persistence.removeFromWatchList(anime)
        val isAnimeAddedToWatchList = persistence.watchAnime(anime.copy(thumbnail = newThumbnail))

        return isAnimeRemoved && isAnimeAddedToWatchList
    }

    private fun updateFilterListEntry(anime: FilterListEntry): Boolean {
        val isAnimeRemoved = persistence.removeFromFilterList(anime)
        val isAnimeAddedToFilterList = persistence.filterAnime(anime.copy(thumbnail = newThumbnail))

        return isAnimeRemoved && isAnimeAddedToFilterList
    }
}