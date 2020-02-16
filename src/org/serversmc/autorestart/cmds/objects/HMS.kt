package org.serversmc.autorestart.cmds.objects

import org.serversmc.autorestart.core.*

object HMS {
	val H get() = TimerThread.TIME / 3600
	val M get() = TimerThread.TIME / 60 - H * 60
	val S get() = TimerThread.TIME - H * 3600 - M * 60
}