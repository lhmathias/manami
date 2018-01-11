package io.github.manami.core.tasks

interface TaskConductor {

    /**
     * Will start a {@see BackgroundTask} if and only if no other task of the same type is already running.
     * @param task The task that will be executed
     */
    fun safelyStart(task: BackgroundTask)

    /**
     * Will cancel all running tasks. While all the tasks are being terminated no new task can be started.
     */
    fun cancelAllTasks()
}