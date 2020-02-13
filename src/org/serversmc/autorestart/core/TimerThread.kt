package org.serversmc.autorestart.core

import org.bukkit.Bukkit
import org.serversmc.autorestart.core.Main.Companion.AutoRestart
import org.serversmc.autorestart.utils.Config
import org.serversmc.autorestart.utils.Console.consoleSender
import org.serversmc.autorestart.utils.Console.err
import org.serversmc.autorestart.utils.Console.warn
import org.serversmc.autorestart.utils.Messenger.broadcastMaxplayersAlert
import org.serversmc.autorestart.utils.Messenger.broadcastMaxplayersPreShutdown
import org.serversmc.autorestart.utils.Messenger.broadcastPauseReminder
import org.serversmc.autorestart.utils.Messenger.broadcastReminderMinutes
import org.serversmc.autorestart.utils.Messenger.broadcastReminderSeconds
import org.serversmc.autorestart.utils.Messenger.broadcastShutdown
import java.util.*
import kotlin.collections.ArrayList

object TimerThread {
	
	var TIME = 0
	var PAUSED = false
	private var PAUSED_TIMER = 0
	var loopId = 0
	var maxplayersId = 0
	var shutdownId = 0
	
	fun calculateTimer() {
		when(Config.getString("main.restart_mode").toUpperCase()) {
			"INTERVAL" -> return
			"TIMESTAMP" -> {
				// Initialize variables
				val timestamps = Config.getTimeStampList("main.modes.timestamp")
				val differences = ArrayList<Long>()
				// Convert timestamps to differences in milliseconds
				timestamps.forEach {
					// Check if timestamp is valid
					if (it.h < 0 || it.h > 23) err("$it hour mark is out of range: 0 - 23")
					if (it.m < 0 || it.m > 59) err("$it minute mark is out of range: 0 - 59")
					// Add converted time to differences list
					differences.add(it.getTimeInMillis() - Calendar.getInstance().timeInMillis)
				}
				// Check if list is empty
				if (differences.isEmpty()) {
					warn("There are no accepted timestamps available! Please check config to ensure that you have followed the correct format.")
					return
				}
				// Get smallest difference
				val time = differences.min()!!
				// Convert milliseconds to time
				TIME = time.toInt() / 1000
			}
			else -> err("Restart mode \"${Config.getString("main.restart_mode")}\" in 'Main.yml:main.restart_mode' was not found! Switching to 'interval' mode!")
		}
		
		// Default restart mode
		TIME = (Config.getDouble("main.modes.interval") * 360.0).toInt()
	}
	
	fun run() {
		calculateTimer()
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
