package io.github.manami.core.services

import io.github.manami.dto.LoggerDelegate
import org.slf4j.Logger
import java.util.concurrent.Callable
import java.util.concurrent.FutureTask


internal abstract class AbstractTask : BackgroundTask {

    private val log: Logger by LoggerDelegate()
    private var onCompleteAction: () -> Unit = { log.info("Task executed successfully.") }

    private val task = FutureTask<Unit>(Callable {
        execute()
        onCompleteAction()
    })

    override fun start() {
        task.run()
    }

    override fun cancel() {
        task.cancel(true)
    }

    abstract fun execute()

    override fun onComplete(action: () -> Unit) {
        onCompleteAction = action
    }
}
