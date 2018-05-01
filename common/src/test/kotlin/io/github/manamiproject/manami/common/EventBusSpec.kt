package io.github.manamiproject.manami.common

import com.google.common.eventbus.Subscribe
import io.github.manamiproject.manami.common.EventBus
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on


private class EventBusListener {

    var event: TestEvent? = null
    var numberOfMessagesReceived = 0

    @Subscribe
    fun listen(obj: TestEvent) {
        event = obj
        numberOfMessagesReceived++
    }
}

class EventBusSpec : Spek({

    given("an event bus subscriber registered to the eventbus") {
        val eventBusListener = EventBusListener()

        beforeEachTest {
            eventBusListener.event = null
            eventBusListener.numberOfMessagesReceived = 0
            EventBus.register(eventBusListener)
        }

        on("publish an event") {
            val testEvent = TestEvent("A sample event")
            EventBus.publish(testEvent)

            it("exactly one event has been received") {
                assertThat(eventBusListener.numberOfMessagesReceived).isOne()
            }

            it("the event is the same that had been published") {
                assertThat(eventBusListener.event).isEqualTo(testEvent)
            }
        }

        on("unregistering the listener and firing a new event") {
            EventBus.unregister(eventBusListener)
            EventBus.publish(TestEvent("Another sample event"))

            it("must prevent the listener from receiving events") {
                assertThat(eventBusListener.numberOfMessagesReceived).isZero()
            }

            it("must result in no event being received") {
                assertThat(eventBusListener.event).isNull()
            }
        }
    }
})

private data class TestEvent(var message: String)