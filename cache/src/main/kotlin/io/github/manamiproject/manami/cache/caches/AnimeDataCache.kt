package io.github.manamiproject.manami.cache.caches

internal interface AnimeDataCache<KEY, VALUE> {

    fun get(key: KEY): VALUE

    fun populate(key: KEY, value: VALUE)

    fun invalidate()
}