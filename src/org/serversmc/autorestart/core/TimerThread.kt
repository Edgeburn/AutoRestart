package org.serversmc.autorestart.core

import org.bukkit.*
import org.serversmc.autorestart.core.Main.Companion.AutoRestart
import org.serversmc.autorestart.core.TimeManager.calculateInterval
import org.serversmc.autorestart.core.TimeManager.calculateTimestamp
import org.serversmc.autorestart.utils.*
import org.serversmc.autorestart.utils.Console.consoleSender
import org.serversmc.autorestart.utils.Console.err
import org.serversmc.autorestart.utils.Messenger.broadcastMaxplayersAlert
import org.serversmc.autorestart.utils.Messenger.broadcastMaxplayersPreShutdown
import org.serversmc.autorestart.utils.Messenger.broadcastPauseReminder
import org.serversmc.autorestart.utils.Messenger.broadcastReminderMinutes
import org.serversmc.autorestart.utils.Messenger.broadcastReminderSeconds
import org.serversmc.autorestart.utils.Messenger.broadcastShutdown
import java.util.*
import kotlin.collections.ArrayList

object TimeManager {

	fun calculateInterval() {
		TimerThread.TIME = (ConfigManager.getDouble("main.modes.interval") * 3600.0).toInt()
	}

	fun calculateTimestamp() {
		// Initialize variables
		val timestamps = ConfigManager.getTimeStampList("main.modes.timestamp")
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
			Console.warn("There are no accepted timestamps available! Please check config to ensure that you have followed the correct format.")
			return
		}
		// Get smallest difference
		val time = differences.min()!!
		// Convert milliseconds to time
		TimerThread.TIME = time.toInt() / 1000
	}

}

object TimerThread {
	
	var TIME = 0
	var PAUSED = false
	private var PAUSED_TIMER = 0
	var loopId = 0
	var maxplayersId = 0
	var shutdownId = 0
	
	fun calculateTimer() {
		when(ConfigManager.getString("main.restart_mode").toUpperCase()) {
			"INTERVAL" -> calculateInterval()
			"TIMESTAMP" -> calculateTimestamp()
			else -> {
				err("Restart mode \"${ConfigManager.getString("main.restart_mode")}\" in 'Main.yml:main.restart_mode' was not found! Switching to 'interval' mode!")
				calculateInterval()
			}
		}
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
				if (PAUSED_TIMER == ConfigManager.getInt("reminder.paused_reminder") * 60) {
					broadcastPauseReminder()
					PAUSED_TIMER = 0
				}
				return@Runnable
			}
			PAUSED_TIMER = 0
			// Minutes Reminder
			if (ConfigManager.getBoolean("reminder.enabled.minutes")) ConfigManager.getIntegerList("reminder.minutes").forEach { if (TIME == it * 60) broadcastReminderMinutes() }
			// Seconds Reminder
			if (ConfigManager.getBoolean("reminder.enabled.seconds") && (TIME <= ConfigManager.getInt("reminder.seconds"))) broadcastReminderSeconds()
			// Command Execute
			if (ConfigManager.getBoolean("commands.enabled") && (TIME == ConfigManager.getInt("commands.seconds")))
				ConfigManager.getStringListColor("commands.list").forEach { Bukkit.dispatchCommand(consoleSender, it) }
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
				broadcastMaxplayersAlert()
				maxplayersId = Bukkit.getScheduler().scheduleSyncRepeatingTask(AutoRestart, {
					// Start Shutdown wait
					if (Bukkit.getOnlinePlayers().size <= Config.MaxPlayers_Amount) {
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
