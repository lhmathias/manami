package io.github.manamiproject.manami.cache.caches

/**
 * Cache for anime data.
 */
internal interface AnimeDataCache<KEY, VALUE> {

    /**
     * Retrieve an entry based on a specific key.
     * @param key The key to identify a specific entry.
     * @return Either the value corresponding to the given key or null.
     */
    fun get(key: KEY): VALUE?

    /**
     * Populate cache with a new entry.
     * @param key The identifier for this entry
     * @param value The actual entry.
     */
    fun populate(key: KEY, value: VALUE)

    /**
     * Remove all entries from cache.
     */
    fun invalidate()
}