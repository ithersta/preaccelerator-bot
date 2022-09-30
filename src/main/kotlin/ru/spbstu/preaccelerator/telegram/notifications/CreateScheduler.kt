package ru.spbstu.preaccelerator.telegram.notifications

import org.quartz.impl.StdSchedulerFactory
import java.util.*

fun createScheduler(name: String) = StdSchedulerFactory(Properties().apply {
    setProperty("org.quartz.scheduler.instanceName", name)
    setProperty("org.quartz.threadPool.threadCount", "1")
    setProperty("org.quartz.jobStore.class", "org.quartz.simpl.RAMJobStore")
}).scheduler
