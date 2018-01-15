package io.github.manami.common

import com.google.common.eventbus.EventBus

object EventBus {

    private val eventBus: EventBus = EventBus()

    fun register(obj: Any) {
        eventBus.register(obj)
    }

    fun unregister(obj: Any) {
        eventBus.unregister(obj)
    }

    fun publish(obj: Any) {
        eventBus.post(obj)
    }
}