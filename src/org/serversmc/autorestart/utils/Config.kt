package org.serversmc.autorestart.utils

import org.bukkit.ChatColor
import org.bukkit.configuration.file.YamlConfiguration
import org.serversmc.autorestart.core.Main.Companion.AutoRestart
import org.serversmc.autorestart.data.*
import java.io.File
import java.io.InputStream
import java.io.InputStreamReader
import java.util.*
import kotlin.collections.ArrayList

object Config {
	
	private val GLOBAL_CONFIG = YamlConfiguration()
	
	private val FILE_MAIN = File(AutoRestart.dataFolder, "Main.yml")
	private val FILE_REMINDER = File(AutoRestart.dataFolder, "Reminder.yml")
	private val FILE_GLOBAL_BROADCAST = File(AutoRestart.dataFolder, "GlobalBroadcast.yml")
	private val FILE_PRIVATE_MESSAGES = File(AutoRestart.dataFolder, "PrivateMessages.yml")
	private val FILE_GLOBAL_POPUPS = File(AutoRestart.dataFolder, "GlobalPopups.yml")
	private val FILE_PRIVATE_POPUPS = File(AutoRestart.dataFolder, "PrivatePopups.yml")
	private val FILE_COMMANDS = File(AutoRestart.dataFolder, "Commands.yml")
	private val FILE_MAXPLAYERS = File(AutoRestart.dataFolder, "MaxPlayers.yml")
	
	private data class ConfigFile(var file: File) {
		var yamlConfiguration = YamlConfiguration.loadConfiguration(file)
		var version = 0
	}
	
	fun getPopup(path: String): Popup = Popup(GLOBAL_CONFIG.getConfigurationSection(path)!!)
	fun getString(path: String): String = ChatColor.translateAlternateColorCodes('&', GLOBAL_CONFIG.getString(path).toString())
	fun getInt(path: String): Int = GLOBAL_CONFIG.getInt(path)
	fun getDouble(path: String): Double = GLOBAL_CONFIG.getDouble(path)
	fun getBoolean(path: String): Boolean = GLOBAL_CONFIG.getBoolean(path)
	fun getIntegerList(path: String): MutableList<Int> = GLOBAL_CONFIG.getIntegerList(path)
	fun getStringList(path: String): MutableList<String> = ArrayList<String>().apply { GLOBAL_CONFIG.getStringList(path).forEach { add(ChatColor.translateAlternateColorCodes('&', it)) } }
	fun getTimeStampList(path: String): MutableList<TimeStamp> = TimeStampManager.parseStringList(GLOBAL_CONFIG.getStringList(path))
	
	private lateinit var configs: ArrayList<ConfigFile>
	
	private fun initializeConfigList() {
		configs = ArrayList<ConfigFile>().apply {
			add(ConfigFile(FILE_MAIN))
			add(ConfigFile(FILE_REMINDER))
			add(ConfigFile(FILE_GLOBAL_BROADCAST))
			add(ConfigFile(FILE_PRIVATE_MESSAGES))
			add(ConfigFile(FILE_GLOBAL_POPUPS))
			add(ConfigFile(FILE_PRIVATE_POPUPS))
			add(ConfigFile(FILE_COMMANDS))
			add(ConfigFile(FILE_MAXPLAYERS))
		}
	}
	
	private fun combineSubConfigs() {
		// Combine config to one Main config
		configs.forEach { config ->
			val yaml = config.yamlConfiguration
			for (key in yaml.getKeys(true)) GLOBAL_CONFIG.set(key, yaml.get(key))
		}
		// Remove version node
		GLOBAL_CONFIG.addDefault("version", null)
	}
	
	fun reloadConfig() {
		initializeConfigList()
		combineSubConfigs()
	}
	
	fun initializeConfig() {
		initializeConfigList()
		configs.forEach { subConfig ->
			// Initialize attributes
			val file = subConfig.file
			val yaml = subConfig.yamlConfiguration
			// Save configs if needed
			if (!file.exists()) {
				AutoRestart.saveResource(file.name, false)
				Console.info("Created ${file.name} config file!")
			}
			// Get config defaults
			yaml.load(file)
			val inputStream = AutoRestart.getResource(file.name) as InputStream
			val defaultConfig = YamlConfiguration.loadConfiguration(InputStreamReader(inputStream))
			GLOBAL_CONFIG.setDefaults(defaultConfig)
			subConfig.version = yaml.getInt("version")
			/** yaml.getInt() to get local version
			 * CONFIG.getInt() to get default version
			 */
			// Update configs if needed
			if (yaml.getInt("version") != GLOBAL_CONFIG.getInt("version")) {
				// Prompt console about update
				Console.info("The config file ${file.name} has changed since the last update!")
				// Create rename file
				val cal = Calendar.getInstance()
				val time = cal.time.toString().replace(":", "_")
				val rename = File(AutoRestart.dataFolder, "($time) ${file.name}")
				// Check if already exists (should never happen)
				if (rename.exists()) rename.delete()
				// Update config file
				file.renameTo(rename)
				AutoRestart.saveResource(file.name, false)
				// Reload updated config
				yaml.load(file)
				subConfig.version = yaml.getInt("version")
				// Prompt update message
				Console.warn("Config file has been backed up to ${rename.name}!")
			}
		}
		combineSubConfigs()
	}
	
}