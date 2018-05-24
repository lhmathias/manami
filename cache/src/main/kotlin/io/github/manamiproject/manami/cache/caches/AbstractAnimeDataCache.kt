package io.github.manamiproject.manami.cache.caches

import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import com.google.common.cache.LoadingCache
import java.lang.Exception
import java.util.*

internal abstract class AbstractAnimeDataCache<KEY, VALUE> (
        load: (KEY) -> VALUE
) : AnimeDataCache<KEY, VALUE> {

    private val cache: LoadingCache<KEY, Optional<VALUE>> = CacheBuilder
            .newBuilder()
            .recordStats()
            .build(object : CacheLoader<KEY, Optional<VALUE>>() {

                @Throws(Exception::class)
                override fun load(key: KEY): Optional<VALUE> {
                    return Optional.ofNullable(load(key))
                }
            }
    )

    override fun get(key: KEY): VALUE? {
        val cacheOptional = cache.get(key)

        if(cacheOptional.isPresent) {
            return cacheOptional.get()
        }

        return null
    }

    override fun populate(key: KEY, value: VALUE) {
        cache.put(key, Optional.ofNullable(value))
    }

    override fun invalidate() {
        cache.invalidateAll()
    }
}