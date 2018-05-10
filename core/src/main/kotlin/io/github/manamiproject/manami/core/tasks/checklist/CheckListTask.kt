package io.github.manamiproject.manami.core.tasks.checklist

import io.github.manamiproject.manami.common.EventBus
import io.github.manamiproject.manami.core.events.checklist.CheckListReportWorkloadEvent
import io.github.manamiproject.manami.core.tasks.AbstractTask

/**
 * Does all checks provided in the configuration.
 * Always start {@link BackgroundTask}s using the {@link ServiceRepository}!
 */
internal class CheckListTask(
        val config: CheckListConfig
) : AbstractTask() {

    override fun execute() {
        EventBus.publish(CheckListReportWorkloadEvent(config.checker.sumBy(Checker::estimateWorkload)))
        config.checker.forEach(Checker::check)
    }
}