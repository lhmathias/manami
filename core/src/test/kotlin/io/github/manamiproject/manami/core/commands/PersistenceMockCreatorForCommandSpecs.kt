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


    fun createFilterListPersistenceMock(entry: FilterListEntry) : Persistence {
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


    fun createWatchListPersistenceMock(entry: WatchListEntry) : Persistence {
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