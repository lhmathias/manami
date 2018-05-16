package io.github.manamiproject.manami.core.tasks.relatedanime

import com.google.common.eventbus.Subscribe
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.mock
import io.github.manamiproject.manami.cache.Cache
import io.github.manamiproject.manami.common.EventBus
import io.github.manamiproject.manami.core.events.ProgressState
import io.github.manamiproject.manami.core.events.relatedanime.RelatedAnimeIdentifiedEvent
import io.github.manamiproject.manami.entities.InfoLink
import io.github.manamiproject.manami.entities.NORMALIZED_ANIME_DOMAIN
import io.github.manamiproject.manami.persistence.Persistence
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on


private class EventBusListener {

    var progressStateEventReceived = 0
    var relatedAnimeIdentifiedEvents: MutableList<RelatedAnimeIdentifiedEvent> = mutableListOf()

    @Subscribe
    fun listen(obj: ProgressState) {
        progressStateEventReceived++
    }

    @Subscribe
    fun listen(event: RelatedAnimeIdentifiedEvent) {
        relatedAnimeIdentifiedEvents.add(event)
    }
}

object FindDirectlyRelatedAnimeTaskSpec : Spek({

    given("anime without any relations") {
        val eventBusListener = EventBusListener()
        val infoLink = InfoLink("${NORMALIZED_ANIME_DOMAIN.MAL}7")
        val cacheMock: Cache = mock {
            on {
                fetchRelatedAnime(eq(infoLink))
            }.doReturn(setOf())
        }
        val persistenceMock: Persistence = mock { }

        val task = FindDirectlyRelatedAnimeTask(
                cacheMock,
                persistenceMock,
                setOf(infoLink)
        )

        beforeEachTest {
            EventBus.register(eventBusListener)
        }

        afterEachTest {
            EventBus.unregister(eventBusListener)
        }

        on("execute task") {
            task.execute()

            it("must receive one initial progress state event") {
                assertThat(eventBusListener.progressStateEventReceived).isOne()
            }

            it("must receive exactly one event for identified anime") {
                assertThat(eventBusListener.relatedAnimeIdentifiedEvents.size).isOne()
            }

            it("must not contain related anime") {
                assertThat(eventBusListener.relatedAnimeIdentifiedEvents[0].relatedAnime).isEmpty()
            }
        }
    }

})