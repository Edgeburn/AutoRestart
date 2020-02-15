@file:Suppress("SpellCheckingInspection", "SpellCheckingInspection")

package org.serversmc.autorestart.utils

import org.bukkit.*
import org.bukkit.configuration.*
import org.bukkit.configuration.file.*
import org.serversmc.autorestart.core.Main.Companion.AutoRestart
import java.io.*
import java.util.*
import kotlin.collections.ArrayList

data class Popup(val section: ConfigurationSection) {
	
	val enabled =  section.getBoolean("enabled")
	val title = Timings(section.getConfigurationSection("title")!!)
	val subtitle = Timings(section.getConfigurationSection("subtitle")!!)
	
	data class Timings(val section: ConfigurationSection) {
		val text = ChatColor.translateAlternateColorCodes('&', section.getString("text")!!)
		val fadeIn = section.getInt("fadein")
		val stay = section.getInt("stay")
		val fadeOut = section.getInt("fadeout")
	}
	
}

data class Message(val section: ConfigurationSection) {
	val enabled = section.getBoolean("enabled")
	val lines: MutableList<String> = ArrayList<String>().apply {
		section.getStringList("message").forEach {
			add(ChatColor.translateAlternateColorCodes('&', it))
		}
	}
}

object Config {
	
	val Main_RecalculateOnreload get() = getBoolean("main.recalculate_onreload")
	val Main_RestartMode get() = getString("interval")
	val Main_Modes_Interval get() = getDouble("main.modes.interval")
	val Main_Modes_Timestamp get() = getTimeStampList("main.modes.timestamp")
	val Main_Prefix get() = getString("main.prefix")
	val Main_KickMessage get() = getString("main.kick_message")
	
	val Reminder_Enabled_Minutes get() = getBoolean("reminder.enabled.minutes")
	val Reminder_Enabled_Seconds get() = getBoolean("reminder.enabled.seconds")
	val Reminder_Minutes get() = getIntegerList("reminder.minutes")
	val Reminder_Seconds get() = getInt("reminder.seconds")
	val Reminder_PauseReminder get() = getInt("reminder.pause_reminder")
	
	val GlobalBroadcast_Minutes get() = getMessage("global_broadcast.minutes")
	val GlobalBroadcast_Seconds get() = getMessage("global_broadcast.seconds")
	val GlobalBroadcast_Status_Resume get() = getMessage("global_broadcast.status.resume")
	val GlobalBroadcast_Status_Pause get() = getMessage("global_broadcast.status.pause")
	val GlobalBroadcast_Change get() = getMessage("global_broadcast.change")
	val GlobalBroadcast_MaxPlayers_Alert get() = getMessage("global_broadcast.max_players.alert")
	val GlobalBroadcast_MaxPlayers_PreShutdown get() = getMessage("global_broadcast.max_players.pre_shutdown")
	val GlobalBroadcast_Shutdown get() = getMessage("global_broadcast.shutdown")
	
	val PrivateMessages_Time get() = getMessage("private_messages.time")
	val PrivateMessages_Status_Resume get() = getMessage("private_messages.status.resume")
	val PrivateMessages_Status_Pause get() = getMessage("private_messages.status.pause")
	val PrivateMessages_Change get() = getMessage("private_messages.change")
	val PrivateMessages_PauseReminder get() = getMessage("private_messages.pause_reminder")
	
	val GlobalPopups_Minutes get() = getPopup("global_popups.minutes")
	val GlobalPopups_Seconds get() = getPopup("global_popups.seconds")
	val GlobalPopups_Status_Resume get() = getPopup("global_popups.status.resume")
	val GlobalPopups_Status_Pause get() = getPopup("global_popups.status.pause")
	val GlobalPopups_Change get() = getPopup("global_popups.change")
	val GlobalPopups_MaxPlayers_Alert get() = getPopup("global_popups.max_players.alert")
	val GlobalPopups_MaxPlayers_PreShutdown get() = getPopup("global_popups.max_players.preshutdown")
	val GlobalPopups_ShutDown get() = getPopup("global_popups.shutdown")
	
	val PrivatePopups_Time get() = getPopup("private_popups.time")
	val PrivatePopups_Status_Resume get() = getPopup("private_popups.status.resume")
	val PrivatePopups_Status_Pause get() = getPopup("private_popups.status.pause")
	val PrivatePopups_Change get() = getPopup("private_popups.change")
	val PrivatePopups_PauseReminder get() = getPopup("private_popups.pause_reminder")
	
	val Commands_Enabled get() = getBoolean("commands.enabled")
	val Commands_Seconds get() = getInt("commands.seconds")
	val Commands_List get() = getStringList("commands.list")
	
	val MaxPlayers_Enabled get() = getBoolean("max_players.enabled")
	val MaxPlayers_Amount get() = getInt("max_players.amount")
	val MaxPlayers_Delay get() = getInt("max_players.delay")
	
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
	
	private fun getString(path: String): String = ChatColor.translateAlternateColorCodes('&', GLOBAL_CONFIG.getString(path).toString())
	private fun getInt(path: String): Int = GLOBAL_CONFIG.getInt(path)
	private fun getDouble(path: String): Double = GLOBAL_CONFIG.getDouble(path)
	private fun getBoolean(path: String): Boolean = GLOBAL_CONFIG.getBoolean(path)
	private fun getIntegerList(path: String): MutableList<Int> = GLOBAL_CONFIG.getIntegerList(path)
	private fun getStringList(path: String): MutableList<String> = GLOBAL_CONFIG.getStringList(path)
	private fun getTimeStampList(path: String): MutableList<TimeStamp> = TimeStampManager.parseStringList(GLOBAL_CONFIG.getStringList(path))
	private fun getPopup(path: String): Popup = Popup(GLOBAL_CONFIG.getConfigurationSection(path)!!)
	private fun getMessage(path: String): Message = Message(GLOBAL_CONFIG.getConfigurationSection(path)!!)
	
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