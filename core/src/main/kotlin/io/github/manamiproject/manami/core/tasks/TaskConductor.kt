package io.github.manamiproject.manami.core.tasks

/**
 * Orchestrates BackgroundTask.
 */
internal interface TaskConductor {

    /**
     * Will start a BackgroundTask if and only if no other task of the same type is already running.
     * @param task The task that will be executed.
     */
    fun safelyStart(task: BackgroundTask)

    /**
     * Will cancel all running tasks. While all the tasks are being terminated no new task can be started.
     */
    fun cancelAllTasks()
}