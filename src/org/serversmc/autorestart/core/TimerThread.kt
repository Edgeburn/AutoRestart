package org.serversmc.autorestart.core

import org.bukkit.Bukkit
import org.serversmc.autorestart.core.Main.Companion.AutoRestart
import org.serversmc.autorestart.utils.Config
import org.serversmc.autorestart.utils.Console.consoleSender
import org.serversmc.autorestart.utils.Messenger.broadcastMaxplayersAlert
import org.serversmc.autorestart.utils.Messenger.broadcastMaxplayersPreShutdown
import org.serversmc.autorestart.utils.Messenger.broadcastPauseReminder
import org.serversmc.autorestart.utils.Messenger.broadcastReminderMinutes
import org.serversmc.autorestart.utils.Messenger.broadcastReminderSeconds
import org.serversmc.autorestart.utils.Messenger.broadcastShutdown

object TimerThread {
	
	var TIME = 50000
	var PAUSED = false
	private var PAUSED_TIMER = 0
	var loopId = 0
	var maxplayersId = 0
	var shutdownId = 0
	
	fun run() {
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
				if (PAUSED_TIMER == Config.getInt("reminder.paused_reminder") * 60) {
					broadcastPauseReminder()
					PAUSED_TIMER = 0
				}
				return@Runnable
			}
			PAUSED_TIMER = 0
			// Minutes Reminder
			if (Config.getBoolean("reminder.enabled.minutes")) Config.getIntegerList("reminder.minutes").forEach { if (TIME == it * 60) broadcastReminderMinutes() }
			// Seconds Reminder
			if (Config.getBoolean("reminder.enabled.seconds") && (TIME <= Config.getInt("reminder.seconds"))) broadcastReminderSeconds()
			// Command Execute
			if (Config.getBoolean("commands.enabled") && (TIME == Config.getInt("commands.seconds")))
				Config.getStringList("commands.list").forEach { Bukkit.dispatchCommand(consoleSender, it) }
			// Timer decrement
			TIME--
		}, 0L, 20L)
	}
	
	private var maxplayers = fun() {
		// Check if max_players is enabled
		if (Config.getBoolean("max_players")) {
			// Check if player count is over configured amount
			if (Bukkit.getOnlinePlayers().size > Config.getInt("max_players.amount")) {
				// Broadcast alert
				broadcastMaxplayersAlert()
				maxplayersId = Bukkit.getScheduler().scheduleSyncRepeatingTask(AutoRestart, {
					// Start Shutdown wait
					if (Bukkit.getOnlinePlayers().size <= Config.getInt("max_players.amount")) {
						return@scheduleSyncRepeatingTask
					}
					// Broadcast pre shutdown message
					broadcastMaxplayersPreShutdown()
					Bukkit.getScheduler().cancelTask(maxplayersId)
				}, 0L, 1L)
			}
		}
		Bukkit.getScheduler().callSyncMethod(AutoRestart, shutdown)
	}
	
	private var shutdown = fun() {
		broadcastShutdown()
		// Player kick / restart message
		Bukkit.getScheduler().callSyncMethod(AutoRestart) {
			Bukkit.getOnlinePlayers().forEach { player ->
				player.kickPlayer(Config.getString("main.kick_message"))
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
