package io.github.manamiproject.manami.cache.populator

import io.github.manamiproject.manami.entities.Anime
import io.github.manamiproject.manami.entities.InfoLink

/**
 * To populate a cache you will need a source. This interface defines the structure of this source.
 */
interface CacheEntrySource {

    /**
     * Get the list of anime. This list is based in InfoLinks. The anime entry for an InfoLink can
     * be null. This means that the given InfoLink is a dead link.
     * @return The mapping of InfoLink and Anime or an empty Map.
     */
    fun getAnimeCacheEntries(): Map<InfoLink, Anime?>

    /**
     * Get the list related anime represented by a Set of InfoLinks. InfoLink has either a Set of
     * InfoLinks which are directly related to the given InfoLink or an empty Set if there are no
     * related anime.
     * @return The mapping of InfoLink and related anime or an empty map.
     */
    fun getRelatedAnimeCacheEntries(): Map<InfoLink, Set<InfoLink>>
}