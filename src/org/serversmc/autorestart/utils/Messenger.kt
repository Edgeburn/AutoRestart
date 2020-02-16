package org.serversmc.autorestart.utils

import org.bukkit.*
import org.bukkit.command.*
import org.bukkit.entity.*
import org.serversmc.autorestart.core.*
import org.serversmc.autorestart.utils.Console.catchError
import org.serversmc.autorestart.utils.Console.consoleSendMessage

object HMS {
	val H get() = TimerThread.TIME / 3600
	val M get() = TimerThread.TIME / 60 - H * 60
	val S get() = TimerThread.TIME - H * 3600 - M * 60
}

object Messenger {
	
	interface Format {
		fun regex(): String
		fun replace(): String
	}

	private val fH = object : Format {
		override fun regex(): String = "%h"
		override fun replace(): String = HMS.H.toString()
	}

	private val fM = object : Format {
		override fun regex(): String = "%m"
		override fun replace(): String = HMS.M.toString()
	}

	private val fS = object : Format {
		override fun regex(): String = "%s"
		override fun replace(): String = HMS.S.toString()
	}

	private val fA = object : Format {
		override fun regex(): String = "%a"
		override fun replace(): String = Config.MaxPlayers_Amount.toString()
	}

	private val fD = object : Format {
		override fun regex(): String = "%d"
		override fun replace(): String = Config.MaxPlayers_Delay.toString()
	}
	
	private fun format(s: String, vars: Array<Format>): String {
		var output = s
		vars.forEach { output = output.replace(it.regex(), it.replace()) }
		return output
	}
	
	private fun getPrefix(): String = Config.Main_Prefix
	private fun broadcastMessage(msg: String) = Bukkit.broadcastMessage(getPrefix() + msg)
	private fun broadcastMessageExclude(msg: String, player: Player) = Bukkit.getOnlinePlayers().forEach { if (it != player) it.sendMessage(msg) }
	
	private fun sendTitle(player: Player, popup: Popup, format: Array<Format>) {
		TitleAPI.sendTitle(player, 0, 0, 0, "", "")
		val title = popup.title
		val subtitle = popup.subtitle
		try {
			TitleAPI.sendTitle(player, title.fadeIn, title.stay, title.fadeOut, format(title.text, format), null)
			TitleAPI.sendTitle(player, subtitle.fadeIn, subtitle.stay, subtitle.fadeOut, null, format(subtitle.text, format))
		} catch (e: Exception) {
			catchError(e, "Messenger.sendTitle():formatter")
		}
	}
	
	enum class Status(val globalMsg: Message, val privateMsg: Message, val globalPopup: Popup, val privatePopup: Popup, val format: Array<Format>) {
		RESUME(Config.GlobalBroadcast_Status_Resume, Config.PrivateMessages_Status_Resume, Config.GlobalPopups_Status_Resume, Config.PrivatePopups_Status_Resume, arrayOf()),
		PAUSE(Config.GlobalBroadcast_Status_Pause, Config.PrivateMessages_Status_Pause, Config.GlobalPopups_Status_Pause, Config.PrivatePopups_Status_Pause, arrayOf()),
		CHANGE(Config.GlobalBroadcast_Change, Config.PrivateMessages_Change, Config.GlobalPopups_Change, Config.PrivatePopups_Change, arrayOf(fH, fM, fS))
	}
	
	enum class Broadcast(val msg: Message, val popup: Popup, val format: Array<Format>) {
		MINUTES(Config.GlobalBroadcast_Minutes, Config.GlobalPopups_Minutes, arrayOf(fM)),
		SECONDS(Config.GlobalBroadcast_Seconds, Config.GlobalPopups_Seconds, arrayOf(fS)),
		MAXPLAYERS_ALERT(Config.GlobalBroadcast_MaxPlayers_Alert, Config.GlobalPopups_MaxPlayers_Alert, arrayOf(fA)),
		MAXPLAYERS_PRESHUTDOWN(Config.GlobalBroadcast_MaxPlayers_PreShutdown, Config.GlobalPopups_MaxPlayers_PreShutdown, arrayOf(fD)),
		SHUTDOWN(Config.GlobalBroadcast_Shutdown, Config.GlobalPopups_ShutDown, arrayOf()),
	}
	
	enum class Private(val msg: Message, val popup: Popup, val format: Array<Format>) {
		PAUSE_REMINDER(Config.PrivateMessages_PauseReminder, Config.PrivatePopups_PauseReminder, arrayOf()),
		TIME(Config.PrivateMessages_Time, Config.PrivatePopups_Time, arrayOf(fH, fM, fS))
	}
	
	fun broadcast(broadcast: Broadcast) {
		val msg = broadcast.msg
		val popup = broadcast.popup
		// Check if pop ups are enabled
		if (popup.enabled) {
			// send pop ups
			Bukkit.getOnlinePlayers().forEach {
				try {
					sendTitle(it, popup, broadcast.format)
				} catch (e: Exception) {
					catchError(e, "Messenger.broadcast(Broadcast):sendTitle(Player, Popup, Array<Format>)")
				}
			}
			if (!msg.enabled) {
				// Send to console only
				msg.lines.forEach { consoleSendMessage(format(it, broadcast.format)) }
				// Disable for players
				return
			}
		}
		// Send to everyone
		msg.lines.forEach { broadcastMessage(format(it, broadcast.format)) }
	}
	
	fun message(sender: CommandSender, private: Private) {
		// Message fetch
		val msg = private.msg
		val popup = private.popup
		// Checks if sender is a player
		if (sender is Player) {
			// Cast sender as player
			val player: Player = sender
			// Check if pop ups are enabled
			if (popup.enabled) {
				// send pop ups
				try {
					sendTitle(player, popup, private.format)
				} catch (e: Exception) {
					catchError(e, "Messenger.messageSenderTime():SendPopUps")
				}
				// Disables message
				if (!msg.enabled) return
			}
		}
		// Private message lines
		msg.lines.forEach { sender.sendMessage(format(it, private.format)) }
	}
	
	fun broadcastStatus(sender: CommandSender, status: Status) {
		// Placeholder setups and message fetch
		val globalMsg = status.globalMsg
		val privateMsg = status.privateMsg
		// Boolean shortcuts
		val globalPopup = status.globalPopup
		val privatePopup = status.privatePopup
		// Check if global popups are enabled
		if (globalPopup.enabled) {
			// Check if private popups are enabled
			if (privatePopup.enabled && sender is Player) {
				try {
					sendTitle(sender, privatePopup, arrayOf())
				} catch (e: Exception) {
					catchError(e, "Messenger.broadcastStatus(CommandSender, Status):sendTitle(Player, Popup, Array<Format>)")
				}
				Bukkit.getOnlinePlayers().forEach { if (it != sender) sendTitle(it, globalPopup, arrayOf()) }
			}
			else {
				Bukkit.getOnlinePlayers().forEach { sendTitle(it, globalPopup, status.format) }
			}
		}
		// Check if global messages are enabled
		if (globalMsg.enabled) {
			// Check if private messages are enabled
			if (privateMsg.enabled) {
				privateMsg.lines.forEach { sender.sendMessage(it) }
				globalMsg.lines.forEach { broadcastMessageExclude(it, sender as Player) }
			}
			else {
				globalMsg.lines.forEach { broadcastMessage(it) }
			}
		}
	}
	
}