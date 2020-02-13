package org.serversmc.autorestart.data

import org.serversmc.autorestart.core.TimerThread

object HMS {
	val H: Int
		get() = TimerThread.TIME / 3600
	val M: Int
		get() = TimerThread.TIME / 60 - H * 60
	val S: Int
		get() = TimerThread.TIME - H * 3600 - M * 60
}