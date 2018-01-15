package io.github.manami.common

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.reflect.KProperty

class LoggerDelegate {
    operator fun getValue(thisRef: Any, property: KProperty<*>): Logger {
        return LoggerFactory.getLogger(thisRef::class.java)
    }
}