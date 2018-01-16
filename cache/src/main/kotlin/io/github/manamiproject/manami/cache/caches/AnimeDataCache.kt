package io.github.manamiproject.manami.cache.caches

interface AnimeDataCache<KEY, VALUE> {

    fun get(key: KEY): VALUE

    fun populate(key: KEY, value: VALUE)
}