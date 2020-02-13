package org.serversmc.autorestart.core

import org.bstats.bukkit.Metrics
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import org.serversmc.autorestart.cmds.CAutoRestart
import org.serversmc.autorestart.core.TimerThread.loopId
import org.serversmc.autorestart.core.TimerThread.maxplayersId
import org.serversmc.autorestart.core.TimerThread.shutdownId
import org.serversmc.autorestart.events.EventPlayerJoin
import org.serversmc.autorestart.utils.Config
import org.serversmc.autorestart.utils.Console.catchError
import org.serversmc.autorestart.utils.Console.info
import org.serversmc.autorestart.utils.Console.warn
import org.serversmc.autorestart.utils.UpdateChecker
import org.serversmc.autorestart.utils.UpdateChecker.UPDATE_FOUND

class Main : JavaPlugin() {
	
	companion object {
		lateinit var AutoRestart: Main
	}
	
	val version = description.version
	
	override fun onEnable() {
		AutoRestart = this
		try {
			// Start metrics
			Metrics(this, 2345)
			// Setup plugin folder is does not exist
			dataFolder.mkdirs()
			// Configuration Files
			Config.initializeConfig()
			// Event Register
			Bukkit.getPluginManager().apply {
				registerEvents(EventPlayerJoin, AutoRestart)
			}
			// Command Setup
			getCommand("autore")!!.apply {
				setExecutor(CAutoRestart)
				tabCompleter = CAutoRestart
			}
			// Check for updates
			UpdateChecker.checkUpdate()
			// Display update message after server Done
			Bukkit.getScheduler().scheduleSyncDelayedTask(this) {
				when {
					UPDATE_FOUND == null -> warn("No Internet to check for updates")
					UPDATE_FOUND!! -> warn("There is a new version of AutoRestart! Go get it now! Latest version: v${UpdateChecker.LATEST_VERSION}")
					else -> info("Up to date!")
				}
			}
			// Timer Thread
			TimerThread.run()
			// Done
			info("Loaded")
		} catch (e: Exception) {
			catchError(e, "UNFILTERED ERROR")
		}
	}
	
	override fun onDisable() {
		arrayOf(loopId, shutdownId, maxplayersId).forEach { if (it != 0) Bukkit.getScheduler().cancelTask(it) }
		info("Done")
	}
}