package org.serversmc.autorestart.data

import org.serversmc.autorestart.core.TimerThread.TIME

object HMS {
	val H: Int get() = TIME / 3600
	val M: Int get() = TIME / 60 - H * 60
	val S: Int get() = TIME - H * 3600 - M * 60
}