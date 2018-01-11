package io.github.manami.core.services


/**
 * A Task running in background
 */
interface BackgroundTask {

    /**
     * Start the task execution.
     * BackgroundTasks should always be started using {@see TaskConductorImpl}
     */
    fun start()

    /**
     * Cancels the execution of the BackgroundTask
     */
    fun cancel()

    /**
     * Action that will take place after the execution has been completed.
     * This can be used for clean-up or tear-down.
     */
    fun onComplete(action: () -> Unit)
}