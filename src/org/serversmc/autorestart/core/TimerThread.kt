package org.serversmc.autorestart.core

import org.bukkit.*
import org.serversmc.autorestart.core.Main.Companion.AutoRestart
import org.serversmc.autorestart.utils.*
import org.serversmc.utils.Console.consoleSender

object TimerThread {
	
	var TIME = 0
	var PAUSED = false
	var loopId = 0
	var maxplayersId = 0
	var shutdownId = 0
	
	private var PAUSED_TIMER = 0
	
	fun run() {
		Config.Main_RestartMode.calculate()
		// Start and store loopId
		loopId = Bukkit.getScheduler().scheduleSyncRepeatingTask(AutoRestart, Runnable {
			// Timer end break
			if (TIME == 0) {
				Bukkit.getScheduler().cancelTask(loopId)
				Bukkit.getScheduler().callSyncMethod(AutoRestart, maxplayers)
				return@Runnable
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
				return@Runnable
			}
			PAUSED_TIMER = 0
			// Minutes Reminder
			if (Config.Reminder_Enabled_Minutes) Config.Reminder_Minutes.forEach { if (TIME == it * 60) Messenger.broadcast(Messenger.Global.MINUTES) }
			// Seconds Reminder
			if (Config.Reminder_Enabled_Seconds && (TIME <= Config.Reminder_Seconds)) Messenger.broadcast(Messenger.Global.SECONDS)
			// Command Execute
			if (Config.Commands_Enabled && (TIME == Config.Commands_Seconds)) Config.Commands_List.forEach { Bukkit.dispatchCommand(consoleSender, it) }
			// Play shutdown sound
			if (TIME == Config.Sounds_Shutdown_Seconds) SoundManager.playShutdown()
			// Timer decrement
			TIME--
		}, 0L, 20L)
	}
	
	private var maxplayers = fun() {
		// Check if max_players is enabled
		if (Config.MaxPlayers_Enabled) {
			// Check if player count is over configured amount
			if (Bukkit.getOnlinePlayers().size > Config.MaxPlayers_Amount) {
				// Broadcast alert
				Messenger.broadcast(Messenger.Global.MAXPLAYERS_ALERT)
				maxplayersId = Bukkit.getScheduler().scheduleSyncRepeatingTask(AutoRestart, {
					// Start Shutdown wait
					if (Bukkit.getOnlinePlayers().size <= Config.MaxPlayers_Amount) {
						return@scheduleSyncRepeatingTask
					}
					// Broadcast pre shutdown message
					Messenger.broadcast(Messenger.Global.MAXPLAYERS_PRESHUTDOWN)
					Bukkit.getScheduler().cancelTask(maxplayersId)
				}, 0L, 1L)
			}
		}
		Bukkit.getScheduler().callSyncMethod(AutoRestart, shutdown)
	}
	
	private var shutdown = fun() {
		Messenger.broadcast(Messenger.Global.SHUTDOWN)
		// Player kick / restart message
		Bukkit.getScheduler().callSyncMethod(AutoRestart) {
			Bukkit.getOnlinePlayers().forEach { player ->
				player.kickPlayer(Config.Main_KickMessage)
			}
		}
		TIME = 5
		// Wait until players are successfully kicked, unless timeout is called
		shutdownId = Bukkit.getScheduler().scheduleSyncRepeatingTask(AutoRestart, {
			if ((TIME == 0) or !Bukkit.getOnlinePlayers().isEmpty()) {
				Bukkit.getScheduler().cancelTask(shutdownId)
				Bukkit.dispatchCommand(consoleSender, "restart")
			}
			TIME--
		}, 0L, 1L)
	}
}
