package io.github.manami.core.services

import java.util.*


interface BackgroundService {

    fun start()

    fun cancel()

    /**
     * Checks whether the service is running or not.
     *
     * @return True if the service is running.
     */
    fun isRunning(): Boolean

    fun reset()

    fun addObserver(observer: Observer)
}
