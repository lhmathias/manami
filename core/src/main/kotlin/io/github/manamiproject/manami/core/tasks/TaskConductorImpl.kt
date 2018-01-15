package io.github.manamiproject.manami.core.tasks

import io.github.manamiproject.manami.common.LoggerDelegate
import org.slf4j.Logger
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicBoolean

internal object TaskConductorImpl : TaskConductor {

    private val log: Logger by LoggerDelegate()
    private val taskList: MutableMap<String, BackgroundTask> = ConcurrentHashMap()
    private val allowNewTaks: AtomicBoolean = AtomicBoolean(true)


    override fun safelyStart(task: BackgroundTask) {
        if(!allowNewTaks.get()) {
            log.debug("Tried to invoke task of type [{}], but starting new tasks has been deactivated.", createIdentifier(task))

            return
        }

        if(isTaskInstanceAlreadyRunning(task)) {
            log.warn("Tried to invoke task of type [{}], but a task of this type is already running.", createIdentifier(task))
        } else {
            task.onComplete {
                taskList.remove(createIdentifier(task))
            }

            taskList.put(createIdentifier(task), task)
            log.info("Starting task of type [{}]", createIdentifier(task))
            task.start()
        }
    }

    private fun isTaskInstanceAlreadyRunning(task: BackgroundTask) = taskList.containsKey(createIdentifier(task))

    private fun createIdentifier(task: BackgroundTask) = task.javaClass.canonicalName

    override fun cancelAllTasks() {
        log.debug("Disallowing new BackgroundTasks")
        allowNewTaks.set(false)

        log.info("Cancelling all active tasks")
        taskList.values.forEach { it.cancel() }

        allowNewTaks.set(true)
        log.debug("Allowing new BackgroundTasks")
    }
}