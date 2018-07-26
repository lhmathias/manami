package io.github.manamiproject.manami.core.commands

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doAnswer
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.mock
import io.github.manamiproject.manami.entities.Anime
import io.github.manamiproject.manami.entities.FilterListEntry
import io.github.manamiproject.manami.entities.InfoLink
import io.github.manamiproject.manami.entities.WatchListEntry
import io.github.manamiproject.manami.persistence.Persistence

object PersistenceMockCreatorForCommandSpecs {

    fun createAnimeListPersistenceMock() : Persistence {
        return mock {
            val empty = Anime("", InfoLink(""))
            var persistedAnime = empty

            on {
                addAnime(any())
            } doAnswer {
                var ret = true

                it.getArgument<Anime>(0)?.let { anime ->
                    if(persistedAnime == anime) {
                        ret = false
                    } else {
                        persistedAnime = anime
                    }
                }

                ret
            }

            on {
                removeAnime(any())
            } doAnswer {
                var ret = true

                it.getArgument<Anime>(0)?.let { anime ->
                    if(persistedAnime == anime) {
                        persistedAnime = empty
                    } else {
                        ret = false
                    }
                }

                ret
            }

            on {
                animeEntryExists(any())
            } doAnswer {
                it.getArgument<Anime>(0)?.let { anime->
                    persistedAnime.infoLink == anime.infoLink
                }
            }

            on {
                fetchAnimeList()
            } doAnswer {
                val ret = mutableListOf<Anime>()

                if(persistedAnime != empty) {
                    ret.add(persistedAnime)
                }

                ret
            }
        }
    }


    fun createFilterListPersistenceMock() : Persistence {
        return mock {
            val empty = FilterListEntry("", InfoLink(""))
            var persistedFilterListEntry = empty

            on {
                filterAnime(any())
            } doAnswer {
                var ret = true

                it.getArgument<FilterListEntry>(0)?.let { filterListEntry ->
                    if(persistedFilterListEntry == filterListEntry) {
                        ret = false
                    } else {
                        persistedFilterListEntry = filterListEntry
                    }
                }

                ret
            }

            on {
                removeFromFilterList(any())
            } doAnswer {
                var ret = true

                it.getArgument<FilterListEntry>(0)?.let { filterListEntry ->
                    if(persistedFilterListEntry == filterListEntry) {
                        persistedFilterListEntry = empty
                    } else {
                        ret = false
                    }
                }

                ret
            }

            on {
                filterListEntryExists(any())
            } doAnswer {
                it.getArgument<Anime>(0)?.let { filterListEntry->
                    persistedFilterListEntry.infoLink == filterListEntry.infoLink
                }
            }

            on {
                fetchFilterList()
            } doAnswer {
                val ret = mutableListOf<FilterListEntry>()

                if(persistedFilterListEntry != empty) {
                    ret.add(persistedFilterListEntry)
                }

                ret
            }
        }
    }


    fun createWatchListPersistenceMock() : Persistence {
        return mock {
            val empty = WatchListEntry("", InfoLink(""))
            var persistedWatchListEntry = empty

            on {
                watchAnime(any())
            } doAnswer {
                var ret = true

                it.getArgument<WatchListEntry>(0)?.let { watchListEntry ->
                    if(persistedWatchListEntry == watchListEntry) {
                        ret = false
                    } else {
                        persistedWatchListEntry = watchListEntry
                    }
                }

                ret
            }

            on {
                removeFromWatchList(any())
            } doAnswer {
                var ret = true

                it.getArgument<WatchListEntry>(0)?.let { watchListEntry ->
                    if(persistedWatchListEntry == watchListEntry) {
                        persistedWatchListEntry = empty
                    } else {
                        ret = false
                    }
                }

                ret
            }

            on {
                watchListEntryExists(any())
            } doAnswer {
                it.getArgument<Anime>(0)?.let { watchListEntry->
                    persistedWatchListEntry.infoLink == watchListEntry.infoLink
                }
            }

            on {
                fetchWatchList()
            } doAnswer {
                val ret = mutableListOf<WatchListEntry>()

                if(persistedWatchListEntry != empty) {
                    ret.add(persistedWatchListEntry)
                }

                ret
            }
        }
    }


    fun createSimpleAnimeListPersistenceMock(anime: Anime) : Persistence {
        return mock {
            var animeEntryExists = false

            on {
                addAnime(eq(anime))
            } doAnswer {
                var ret = true

                if(animeEntryExists) {
                    ret = false
                } else {
                    animeEntryExists = true
                }

                ret
            }

            on {
                removeAnime(eq(anime))
            } doAnswer {
                var ret = true

                if(animeEntryExists) {
                    animeEntryExists = false
                } else {
                    ret = false
                }

                ret
            }

            on {
                animeEntryExists(eq(anime.infoLink))
            } doAnswer {
                animeEntryExists
            }
        }
    }


    fun createSimpleFilterListPersistenceMock(entry: FilterListEntry) : Persistence {
        return mock {
            var filterListEntryExists = false

            on {
                filterAnime(eq(entry))
            } doAnswer {
                var ret = true

                if(filterListEntryExists) {
                    ret = false
                } else {
                    filterListEntryExists = true
                }

                ret
            }

            on {
                removeFromFilterList(eq(entry))
            } doAnswer {
                var ret = true

                if(filterListEntryExists) {
                    filterListEntryExists = false
                } else {
                    ret = false
                }

                ret
            }

            on {
                filterListEntryExists(eq(entry.infoLink))
            } doAnswer {
                filterListEntryExists
            }
        }
    }


    fun createSimpleWatchListPersistenceMock(entry: WatchListEntry) : Persistence {
        return mock {
            var watchListEntryExists = false

            on {
                watchAnime(eq(entry))
            } doAnswer {
                var ret = true

                if(watchListEntryExists) {
                    ret = false
                } else {
                    watchListEntryExists = true
                }

                ret
            }

            on {
                removeFromWatchList(eq(entry))
            } doAnswer {
                var ret = true

                if(watchListEntryExists) {
                    watchListEntryExists = false
                } else {
                    ret = false
                }

                ret
            }

            on {
                watchListEntryExists(eq(entry.infoLink))
            } doAnswer {
                watchListEntryExists
            }
        }
    }
}