package io.github.manami.core.config

import com.google.common.eventbus.EventBus
import com.google.common.eventbus.Subscribe
import org.springframework.beans.factory.config.BeanPostProcessor
import javax.inject.Inject
import javax.inject.Named

@Named
internal class EventBusPostProcessor @Inject constructor(
        private val eventBus: EventBus
) : BeanPostProcessor {

    override fun postProcessBeforeInitialization(bean: Any, beanName: String): Any = bean

    override fun postProcessAfterInitialization(bean: Any, beanName: String): Any {
        val clazz = bean.javaClass
        val methods = clazz.methods

        methods.forEach {
            it.annotations
                    .filter { it is Subscribe }
                    .forEach { eventBus.register(bean) }
        }

        return bean
    }
}
