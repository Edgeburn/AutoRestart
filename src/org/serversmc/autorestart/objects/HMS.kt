package org.serversmc.autorestart.objects

import org.serversmc.autorestart.threads.*

object HMS {
	val rawH get() = MainThread.getTime() / 3600
	val rawM get() = MainThread.getTime() / 60
	val rawS get() = MainThread.getTime()
	
	val H get() = MainThread.getTime() / 3600
	val M get() = MainThread.getTime() / 60 - H * 60
	val S get() = MainThread.getTime() - H * 3600 - M * 60
}