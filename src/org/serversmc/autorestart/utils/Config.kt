package org.serversmc.autorestart.utils

import org.serversmc.autorestart.enums.*
import org.serversmc.utils.*
import org.serversmc.utils.ConfigAPI.Companion.globalConfig
import org.serversmc.utils.Console.err
import org.serversmc.utils.Console.warn
import java.lang.Integer.*
import java.util.*
import kotlin.collections.ArrayList

object Config : ConfigAPI {
	
	data class TimeStamp(val h: Int, val m: Int) {
		
		override fun toString(): String {
			return "$h:$m"
		}
		
		fun getTimeInMillis(): Long {
			var time = Calendar.getInstance().run {
				set(Calendar.HOUR_OF_DAY, h)
				set(Calendar.MINUTE, m)
				set(Calendar.SECOND, 0)
				timeInMillis
			}
			if (time - Calendar.getInstance().timeInMillis < 0) time += 86400000L
			return time
		}
		
	}
	
	data class ConfigSection(val message: Message, val popup: Popup)
	
	data class Popup(val section: String) {
		
		val enabled = Config.getBoolean("$section.enabled")
		val title = Timings("$section.title")
		val subtitle = Timings("$section.subtitle")
		
		data class Timings(val section: String) {
			
			private var timing = Config.getString("$section.timing")
			
			init {
				if (timing.split(":").size != 3) {
					timing = globalConfig.defaults!!.getString("$section.timing")!!
					warn("Invalid timing format at $section.timing. Please review: $timing")
				}
				else {
					timing.split(":").forEach {
						try {
							parseInt(it)
						} catch (e: Exception) {
							timing = globalConfig.defaults!!.getString("$section.timing")!!
							err("Invalid timing format at $section.timing. Using default timing. Please review: $timing")
						}
					}
				}
			}
			
			val text = ChatColor.translate('&', Config.getString("$section.text"))
			val fadeIn = parseInt(timing.split(":")[0])
			val stay = parseInt(timing.split(":")[1])
			val fadeOut = parseInt(timing.split(":")[2])
			
		}
		
	}
	
	data class Message(val section: String) {
		val enabled = Config.getBoolean("$section.enabled")
		val lines: MutableList<String> = ArrayList<String>().apply {
			Config.getStringList("$section.message").forEach {
				add(ChatColor.translate('&', it))
			}
		}
	}
	
	override fun setupConfigs() {
		addFile("Commands")
		addFile("GlobalBroadcast")
		addFile("GlobalPopups")
		addFile("Main")
		addFile("MaxPlayers")
		addFile("PrivateMessages")
		addFile("PrivatePopups")
		addFile("Reminder")
		addFile("Sounds")
	}
	
	private fun getTimeStampList(path: String): MutableList<TimeStamp> {
		return ArrayList<TimeStamp>().apply {
			getStringList(path).forEach {
				try {
					val h = parseInt(it.split(":")[0])
					val m = parseInt(it.split(":")[1])
					add(TimeStamp(h, m))
				} catch (e: Exception) {
					err("Could not read \"$it\" please check Main.yml:main.modes.interval")
				}
			}
		}
	}
	
	private fun getGlobal(name: String): ConfigSection = ConfigSection(Message("global_broadcast.$name"), Popup("global_popups.$name"))
	private fun getPrivate(name: String): ConfigSection = ConfigSection(Message("private_messages.$name"), Popup("private_popups.$name"))
	
	val Main_RecalculateOnreload get() = getBoolean("main.recalculate_onreload")
	val Main_RestartMode get() = RestartMode.parse(Main_RestartMode_Raw)
	val Main_RestartMode_Raw get() = getString("main.restart_mode")
	val Main_Modes_Interval_Factor get() = IntervalFactor.parse(Main_Modes_Interval_Factor_Raw)
	val Main_Modes_Interval_Factor_Raw get() = getString("main.modes.interval.factor")
	val Main_Modes_Interval_Value get() = getDouble("main.modes.interval.value")
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
	
	val Sounds_Broadcast_Enabled get() = getBoolean("sounds.broadcast.enabled")
	val Sounds_Private_Enabled get() = getBoolean("sounds.private.enabled")
	val Sounds_Shutdown_Enabled get() = getBoolean("sounds.shutdown.enabled")
	val Sounds_Shutdown_Seconds get() = getInt("sounds.shutdown.seconds")
	
}