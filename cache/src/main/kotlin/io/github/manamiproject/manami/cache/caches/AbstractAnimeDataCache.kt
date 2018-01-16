package io.github.manamiproject.manami.cache.caches

import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import com.google.common.cache.LoadingCache

abstract class AbstractAnimeDataCache<KEY, VALUE> (
        load: (KEY) -> VALUE
) : AnimeDataCache<KEY, VALUE> {

    private val cache: LoadingCache<KEY, VALUE> = CacheBuilder
            .newBuilder()
            .recordStats()
            .build(object : CacheLoader<KEY, VALUE>() {

                @Throws(Exception::class)
                override fun load(key: KEY): VALUE {
                    return load(key)
                }
            }
    )

    override fun get(key: KEY): VALUE {
        return cache.get(key)
    }

    override fun populate(key: KEY, value: VALUE) {
        cache.put(key, value)
    }
}