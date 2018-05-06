package io.github.manamiproject.manami.core.config

import com.google.common.eventbus.Subscribe
import io.github.manamiproject.manami.common.EventBus
import io.github.manamiproject.manami.core.tasks.events.OpenedFileChangedEvent
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import java.nio.file.Paths


private class EventBusListener {

    var openedFileChangedEvent = 0

    @Subscribe
    fun listen(obj: OpenedFileChangedEvent) {
        openedFileChangedEvent++
    }
}

object ConfigSpec : Spek({


    given("a valid path") {
        val newFile = Paths.get("./resources/test_anime_list.xml")
        val eventBusListener = EventBusListener()
        EventBus.register(eventBusListener)

        on("setting config file") {
            Config.file = newFile

            it("must throw an OpenedFileChangedEvent") {
                assertThat(eventBusListener.openedFileChangedEvent).isOne()
            }

            it("must contain new file") {
                assertThat(Config.file).isEqualTo(newFile)
            }
        }

    }
})