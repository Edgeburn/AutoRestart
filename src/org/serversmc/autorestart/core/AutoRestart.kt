package org.serversmc.autorestart.core

import org.bstats.bukkit.*
import org.bukkit.*
import org.bukkit.configuration.file.*
import org.bukkit.plugin.java.*
import org.serversmc.autorestart.cmds.*
import org.serversmc.autorestart.core.TimerThread.loopId
import org.serversmc.autorestart.core.TimerThread.maxplayersId
import org.serversmc.autorestart.core.TimerThread.shutdownId
import org.serversmc.autorestart.core.UpdateChecker.UPDATE_FOUND
import org.serversmc.autorestart.events.*
import org.serversmc.autorestart.utils.*
import org.serversmc.autorestart.utils.Console.catchError
import org.serversmc.autorestart.utils.Console.info
import org.serversmc.autorestart.utils.Console.warn
import java.io.*
import java.net.*

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
			ConfigManager.initializeConfig()
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

object UpdateChecker {
	
	private var LATEST_BUILD: Int? = null
	var LATEST_VERSION: String? = null
	var UPDATE_FOUND: Boolean? = null
	
	private val url = URL("https://gitlab.com/dennislysenko/autorestart-v4/-/raw/master/res/plugin.yml")
	private val pluginYml = InputStreamReader(Main.AutoRestart.getResource("plugin.yml") as InputStream)
	private val yaml = YamlConfiguration.loadConfiguration(pluginYml)
	
	fun checkUpdate() {
		try {
			// Fetch latest version string
			YamlConfiguration().apply {
				load(InputStreamReader(url.openStream()))
				LATEST_BUILD = getInt("build")
				LATEST_VERSION = getString("version")?.trim() ?: "null"
			}
			// Check if latest build was fetched
			if (LATEST_BUILD == null) return
			// Check if version is up to date
			val currentBuild = yaml.getInt("build", -1)
			if (currentBuild == -1) return
			UPDATE_FOUND = LATEST_BUILD!! > currentBuild
		} catch (e: Exception) {
		}
	}
	
}