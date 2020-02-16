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
	
	enum class Status(val globalMsg: Message, val privateMsg: Message, val globalPopup: Popup, val privatePopup: Popup, val format: Array<Format>) {
		RESUME(Config.GlobalBroadcast_Status_Resume, Config.PrivateMessages_Status_Resume, Config.GlobalPopups_Status_Resume, Config.PrivatePopups_Status_Resume, arrayOf()),
		PAUSE(Config.GlobalBroadcast_Status_Pause, Config.PrivateMessages_Status_Pause, Config.GlobalPopups_Status_Pause, Config.PrivatePopups_Status_Pause, arrayOf()),
		CHANGE(Config.GlobalBroadcast_Change, Config.PrivateMessages_Change, Config.GlobalPopups_Change, Config.PrivatePopups_Change, arrayOf(fH, fM, fS))
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
	private fun broadcastMessage(msg: String, perm: String) = Bukkit.broadcast(getPrefix() + msg, perm)
	
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
	
	fun broadcastReminderMinutes() {
		val msg = Config.GlobalBroadcast_Minutes
		val popup = Config.GlobalPopups_Minutes
		// Check if pop ups are enabled
		if (popup.enabled) {
			// send pop ups
			Bukkit.getOnlinePlayers().forEach {
				try {
					sendTitle(it, popup, arrayOf(fM))
				} catch (e: Exception) {
					catchError(e, "Messenger.broadcastReminderMinutes():SendPopUps")
				}
			}
			if (!msg.enabled) {
				// Send to console only
				msg.lines.forEach { consoleSendMessage(format(it, arrayOf(fM))) }
				// Disable for players
				return
			}
		}
		// Send to everyone
		msg.lines.forEach { broadcastMessage(format(it, arrayOf(fM))) }
	}
	
	fun broadcastReminderSeconds() {
		val msg = Config.GlobalBroadcast_Seconds
		val popup = Config.GlobalPopups_Seconds
		// Check if pop ups are enabled
		if (popup.enabled) {
			// send pop ups
			Bukkit.getOnlinePlayers().forEach {
				try {
					sendTitle(it, popup, arrayOf(fS))
				} catch (e: Exception) {
					catchError(e, "Messenger.broadcastReminderSeconds():SendPopUps")
				}
			}
			if (!msg.enabled) {
				// Send to console only
				msg.lines.forEach { consoleSendMessage(format(it, arrayOf(fS))) }
				// Disable for players
				return
			}
		}
		// Send to everyone
		msg.lines.forEach { broadcastMessage(format(it, arrayOf(fS))) }
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
					catchError(e, "Messenger:broadcastStatus(CommandSender, Status)")
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
	
	fun broadcastMaxplayersAlert() {
		// Placeholder setups and message fetch
		val msg = Config.GlobalBroadcast_MaxPlayers_Alert
		val popup = Config.GlobalPopups_MaxPlayers_Alert
		// Check if pop ups are enabled
		if (popup.enabled) {
			// send pop ups
			Bukkit.getOnlinePlayers().forEach {
				try {
					sendTitle(it, popup, arrayOf(fA))
				} catch (e: Exception) {
					catchError(e, "Messenger.broadcastMaxplayersAlert():SendPopUps")
				}
			}
			if (!msg.enabled) {
				// Send to console only
				msg.lines.forEach { consoleSendMessage(format(it, arrayOf(fA))) }
				// Disable for players
				return
			}
		}
		// Send to everyone
		msg.lines.forEach { broadcastMessage(format(it, arrayOf(fA))) }
	}
	
	fun broadcastMaxplayersPreShutdown() {
		// Message fetch
		val msg = Config.GlobalBroadcast_MaxPlayers_PreShutdown
		val popup = Config.GlobalPopups_MaxPlayers_PreShutdown
		// Check if pop ups are enabled
		if (popup.enabled) {
			// send pop ups
			Bukkit.getOnlinePlayers().forEach {
				try {
					sendTitle(it, popup, arrayOf(fD))
				} catch (e: Exception) {
					catchError(e, "Messenger.broadcastMaxplayersPreShutdown():SendPopUps")
				}
			}
			if (!msg.enabled) {
				// Send to console only
				msg.lines.forEach { consoleSendMessage(format(it, arrayOf(fD))) }
				// Disable for players
				return
			}
		}
		// Send to everyone
		msg.lines.forEach { broadcastMessage(format(it, arrayOf(fD))) }
	}
	
	fun broadcastPauseReminder() {
		// Message fetch
		val msg = Config.PrivateMessages_PauseReminder
		val popup = Config.PrivatePopups_PauseReminder
		// Send to console only
		msg.lines.forEach { consoleSendMessage(it) }
		// Check if pop ups are enabled
		if (popup.enabled) {
			// send pop ups
			Bukkit.getOnlinePlayers().forEach {
				try {
					sendTitle(it, popup, arrayOf())
				} catch (e: Exception) {
					catchError(e, "Messenger.broadcastPauseReminder():SendPopUps")
				}
			}
			// Disable for players
			if (!msg.enabled) return
		}
		// Send to everyone
		msg.lines.forEach { broadcastMessage(it, "autorestart.start") }
	}
	
	fun broadcastShutdown() {
		// Message fetch
		val msg = Config.GlobalBroadcast_Shutdown
		val popup = Config.GlobalPopups_ShutDown
		// Check if pop ups are enabled
		if (popup.enabled) {
			// send pop ups
			Bukkit.getOnlinePlayers().forEach {
				try {
					sendTitle(it, popup, arrayOf())
				} catch (e: Exception) {
					catchError(e, "Messenger.broadcastShutdown():SendPopUps")
				}
			}
			if (!msg.enabled) {
				// Send to console only
				msg.lines.forEach { consoleSendMessage(it) }
				// Disable for players
				return
			}
		}
		// Send to everyone
		msg.lines.forEach { broadcastMessage(it) }
	}
	
	fun messageSenderTime(sender: CommandSender) {
		// Message fetch
		val msg = Config.PrivateMessages_Time
		val popup = Config.PrivatePopups_Time
		// Checks if sender is a player
		if (sender is Player) {
			// Cast sender as player
			val player: Player = sender
			// Check if pop ups are enabled
			if (popup.enabled) {
				// send pop ups
				try {
					sendTitle(player, popup, arrayOf(fH, fM, fS))
				} catch (e: Exception) {
					catchError(e, "Messenger.messageSenderTime():SendPopUps")
				}
				// Disables message
				if (!msg.enabled) return
			}
		}
		// Private message lines
		msg.lines.forEach { sender.sendMessage(format(it, arrayOf(fH, fM, fS))) }
	}
}