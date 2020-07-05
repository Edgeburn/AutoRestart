package org.serversmc.autorestart.threads

import org.bukkit.*
import org.serversmc.autorestart.utils.*

object MainThread : Thread() {
	
	private var TIME = 0
	private var PAUSED = false
	private var PAUSED_TIMER = 0
	
	override fun run() {
		// Calculate Shutdown
		Config.Main_RestartMode.calculate()
		// Start Main Loop
		while (true) {
			// Timer end break
			if (TIME == 0) {
				MaxPlayerThread.start()
				break
			}
			// Check if timer is paused
			if (PAUSED) {
				PAUSED_TIMER++
				// Check if paused reminder is ready
				if (PAUSED_TIMER == Config.Reminder_PauseReminder * 60) {
					Bukkit.getOnlinePlayers().forEach {
						if (!it.hasPermission("autorestart.resume")) return@forEach
						Messenger.message(it, Messenger.Private.PAUSE_REMINDER)
					}
					PAUSED_TIMER = Config.Reminder_PauseReminder
				}
				continue
			}
			PAUSED_TIMER = 0
			// Minutes Reminder
			if (Config.Reminder_Enabled_Minutes) Config.Reminder_Minutes.forEach { if (TIME == it * 60) Messenger.broadcast(Messenger.Global.MINUTES) }
			// Seconds Reminder
			if (Config.Reminder_Enabled_Seconds && (TIME <= Config.Reminder_Seconds)) Messenger.broadcast(Messenger.Global.SECONDS)
			// Command Execute
			if (Config.Commands_Enabled && (TIME == Config.Commands_Seconds)) Config.Commands_List.forEach { Bukkit.dispatchCommand(Console.consoleSender, it) }
			// Play shutdown sound
			if (TIME == Config.Sounds_Shutdown_Seconds) SoundManager.playShutdown()
			// Timer decrement
			TIME--
			// Delay Runnable
			sleep(1000L)
		}
	}
	
	fun isPaused() = PAUSED
	
	fun pauseTimer() {
		PAUSED = true
	}
	
	fun resumeTimer() {
		PAUSED = false
	}
	
	fun getTime() = TIME
	
	fun updateTime(time: Int) {
		TIME = time
	}
	
}