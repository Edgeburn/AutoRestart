@file:Suppress("SpellCheckingInspection", "SpellCheckingInspection")

package org.serversmc.autorestart.utils

import org.bukkit.*
import org.bukkit.configuration.file.*
import org.serversmc.autorestart.core.Main.Companion.AutoRestart
import org.serversmc.console.Console.err
import org.serversmc.console.Console.info
import org.serversmc.console.Console.warn
import java.io.*
import java.lang.Integer.*
import java.util.*
import kotlin.collections.ArrayList

data class ConfigSection(val message: Message, val popup: Popup)

data class Popup(val section: String) {
	
	val enabled = getBoolean("$section.enabled")
	val title = Timings("$section.title")
	val subtitle = Timings("$section.subtitle")
	
	data class Timings(val section: String) {
		private var timing = getString("$section.timing")
		init {
			if (timing.split(":").size != 3) {
				timing = GLOBAL_CONFIG.defaults!!.getString("$section.timing")!!
				warn("Invalid timing format at $section.timing. Please review: $timing")
			}
			else {
				timing.split(":").forEach {
					try {
						parseInt(it)
					} catch (e: Exception) {
						timing = GLOBAL_CONFIG.defaults!!.getString("$section.timing")!!
						err("Invalid timing format at $section.timing. Using default timing. Please review: $timing")
					}
				}
			}
		}
		val text = ChatColor.translateAlternateColorCodes('&', getString("$section.text"))
		val fadeIn = parseInt(timing.split(":")[0])
		val stay = parseInt(timing.split(":")[1])
		val fadeOut = parseInt(timing.split(":")[2])
	}
	
}

data class Message(val section: String) {
	val enabled = getBoolean("$section.enabled")
	val lines: MutableList<String> = ArrayList<String>().apply {
		getStringList("$section.message").forEach {
			add(ChatColor.translateAlternateColorCodes('&', it))
		}
	}
}

private val GLOBAL_CONFIG = YamlConfiguration()

private fun getString(path: String): String = ChatColor.translateAlternateColorCodes('&', GLOBAL_CONFIG.getString(path).toString())
private fun getInt(path: String): Int = GLOBAL_CONFIG.getInt(path)
private fun getDouble(path: String): Double = GLOBAL_CONFIG.getDouble(path)
private fun getBoolean(path: String): Boolean = GLOBAL_CONFIG.getBoolean(path)
private fun getIntegerList(path: String): MutableList<Int> = GLOBAL_CONFIG.getIntegerList(path)
private fun getStringList(path: String): MutableList<String> = GLOBAL_CONFIG.getStringList(path)
private fun getTimeStampList(path: String): MutableList<TimeStamp> = TimeStampManager.parseStringList(GLOBAL_CONFIG.getStringList(path))
private fun getGlobal(name: String): ConfigSection = ConfigSection(Message("global_broadcast.$name"), Popup("global_popups.$name"))
private fun getPrivate(name: String): ConfigSection = ConfigSection(Message("private_messages.$name"), Popup("private_popups.$name"))

object Config {
	
	val Main_RecalculateOnreload get() = getBoolean("main.recalculate_onreload")
	val Main_RestartMode get() = getString("main.restart_mode")
	val Main_Modes_Interval get() = getDouble("main.modes.interval")
	val Main_Modes_Timestamp get() = getTimeStampList("main.modes.timestamp")
	val Main_Prefix get() = getString("main.prefix")
	val Main_KickMessage get() = getString("main.kick_message")
	
	val Reminder_Enabled_Minutes get() = getBoolean("reminder.enabled.minutes")
	val Reminder_Enabled_Seconds get() = getBoolean("reminder.enabled.seconds")
	val Reminder_Minutes get() = getIntegerList("reminder.minutes")
	val Reminder_Seconds get() = getInt("reminder.seconds")
	val Reminder_PauseReminder get() = getInt("reminder.pause_reminder")
	
	val Global_Minutes get() = getGlobal("minutes")
	val Global_Seconds get() = getGlobal("seconds")
	val Global_Status_Resume get() = getGlobal("status.resume")
	val Global_Status_Pause get() = getGlobal("status.pause")
	val Global_Change get() = getGlobal("change")
	val Global_MaxPlayers_Alert get() = getGlobal("max_players.alert")
	val Global_MaxPlayers_PreShutdown get() = getGlobal("max_players.pre_shutdown")
	val Global_Shutdown get() = getGlobal("shutdown")
	
	val Private_Time get() = getPrivate("time")
	val Private_Status_Resume get() = getPrivate("status.resume")
	val Private_Status_Pause get() = getPrivate("status.pause")
	val Private_Change get() = getPrivate("change")
	val Private_PauseReminder get() = getPrivate("pause_reminder")
	
	val Commands_Enabled get() = getBoolean("commands.enabled")
	val Commands_Seconds get() = getInt("commands.seconds")
	val Commands_List get() = getStringList("commands.list")
	
	val MaxPlayers_Enabled get() = getBoolean("max_players.enabled")
	val MaxPlayers_Amount get() = getInt("max_players.amount")
	val MaxPlayers_Delay get() = getInt("max_players.delay")
	
	private val FILE_MAIN = File(AutoRestart.dataFolder, "Main.yml")
	private val FILE_REMINDER = File(AutoRestart.dataFolder, "Reminder.yml")
	private val FILE_GLOBAL_BROADCAST = File(AutoRestart.dataFolder, "GlobalBroadcast.yml")
	private val FILE_PRIVATE_MESSAGES = File(AutoRestart.dataFolder, "PrivateMessages.yml")
	private val FILE_GLOBAL_POPUPS = File(AutoRestart.dataFolder, "GlobalPopups.yml")
	private val FILE_PRIVATE_POPUPS = File(AutoRestart.dataFolder, "PrivatePopups.yml")
	private val FILE_COMMANDS = File(AutoRestart.dataFolder, "Commands.yml")
	private val FILE_MAXPLAYERS = File(AutoRestart.dataFolder, "MaxPlayers.yml")
	
	private lateinit var configs: ArrayList<ConfigFile>
	
	private data class ConfigFile(var file: File) {
		var yamlConfiguration = YamlConfiguration.loadConfiguration(file)
		var version = 0
	}
	
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
				info("Created ${file.name} config file!")
			}
			// Get config defaults
			yaml.load(file)
			val inputStream = AutoRestart.getResource(file.name) as InputStream
			val defaultConfig = YamlConfiguration.loadConfiguration(InputStreamReader(inputStream))
			GLOBAL_CONFIG.addDefaults(defaultConfig)
			subConfig.version = yaml.getInt("version")
			/** yaml.getInt() to get local version
			 * CONFIG.getInt() to get default version
			 */
			// Update configs if needed
			if (yaml.getInt("version") != GLOBAL_CONFIG.getInt("version")) {
				// Prompt console about update
				info("The config file ${file.name} has changed since the last update!")
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
				warn("Config file has been backed up to ${rename.name}!")
			}
		}
		combineSubConfigs()
	}
	
}