package io.github.manamiproject.manami.core.tasks.events;

import io.github.manamiproject.manami.entities.MinimalEntry


/**
 * Contains a {@link List} of {@link MinimalEntry} for each list typeChecklist.
 */
class SearchResultEvent(val searchString: String) {

    private val animeListSearchResultList: MutableList<MinimalEntry> = mutableListOf()
    private val filterListSearchResultList: MutableList<MinimalEntry> = mutableListOf()
    private val watchListSearchResultList: MutableList<MinimalEntry> = mutableListOf()

    /**
     * @return The list containing search results from anime list.
     */
    fun getAnimeListSearchResultList() = animeListSearchResultList.toList()


    /**
     * @return The list containing search results from filter list.
     */
    fun getFilterListSearchResultList() = filterListSearchResultList.toList()


    /**
     * @return The list containing search results from watch list.
     */
    fun getWatchListSearchResultList() = watchListSearchResultList.toList()


    fun addAnimeListSearchResult(entry: MinimalEntry) {
        animeListSearchResultList.add(entry)
    }

    fun addFilterListSearchResult(entry: MinimalEntry) {
        filterListSearchResultList.add(entry)
    }


    fun addWatchListSearchResult(entry: MinimalEntry) {
        watchListSearchResultList.add(entry)
    }
}
