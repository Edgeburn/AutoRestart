package org.serversmc.autorestart.utils

import net.minecraft.server.v1_15_R1.Items.*
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

private interface Format {
	fun regex(): String
	fun replace(): String
}

object Messenger {

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
	
	private fun sortWhoGetsWhatChat(playerSender: Player?, globalMsg: Message, privateMsg: Message) {
		// Check if global broadcast is enabled
		if (globalMsg.enabled) {
			// Everyone but console and/or sender gets global message
			for (player in Bukkit.getOnlinePlayers()) {
				if (player == playerSender && privateMsg.enabled) {
					continue
				}
				globalMsg.lines.forEach { player.sendMessage(getPrefix() + it) }
			}
		}
		// Check if player broadcast is enabled
		if (privateMsg.enabled) {
			// Check if player triggered event
			if (playerSender != null) {
				// send private message to player
				privateMsg.lines.forEach { playerSender.sendMessage(it) }
			}
		}
		// Check if console is sender and set console message list
		val consoleList: List<String> = if (playerSender == null) {
			privateMsg.lines
		} else {
			globalMsg.lines
		}
		// Send Message to console
		consoleList.forEach { consoleSendMessage(it) }
	}
	
	private fun sortWhoGetsWhatPopUp(playerSender: Player?, globalPopup: Popup, privatePopup: Popup, format: Array<Format>) {
		// Check if global pop up is enabled
		if (globalPopup.enabled) {
			// Everyone but sender gets global message
			for (player in Bukkit.getOnlinePlayers()) {
				if ((player == playerSender) && privatePopup.enabled) {
					continue
				}
				sendTitle(player, globalPopup, format)
			}
		}
		// Check if player pop up is enabled
		if (privatePopup.enabled) {
			// Check if player triggered event
			if (playerSender != null) {
				// send private pop up to player
				sendTitle(playerSender, privatePopup, format)
			}
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
			Bukkit.getOnlinePlayers().forEach { player: Player  ->
				try {
					sendTitle(player, popup, arrayOf(fS))
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
	
	fun broadcastStatusResume(sender: CommandSender?) {
		// Check if player executed command
		val playerSender: Player? = if (sender is Player) sender else null
		// Placeholder setups and message fetch
		val globalMsgLines = ConfigManager.getStringListColor("global_broadcast.messages.status.resume")
		val privateMsgLines = ConfigManager.getStringListColor("private_messages.messages.status.resume")
		// Boolean shortcuts
		var globalBroadcast = ConfigManager.getBoolean("global_broadcast.enabled.status.resume")
		val privateMessage = ConfigManager.getBoolean("private_messages.enabled.status.resume")
		val globalPopup = ConfigManager.getBoolean("global_popups.enabled.status.resume")
		val privatePopup = ConfigManager.getBoolean("private_popups.enabled.status.resume")
		// check if global and player pop ups are off
		if (!globalPopup && !privatePopup) {
			if (!privateMessage) globalBroadcast = true
		} else {
			try {
				sortWhoGetsWhatPopUp(
					playerSender,
					ConfigManager.getPopup("global_popups.messages.status.resume"),
					ConfigManager.getPopup("private_popups.messages.status.resume"),
					arrayOf()
				)
			} catch (e: Exception) {
				catchError(e, "Messenger.broadcastStatusResume():SortWhoGetsWhatPopUp")
			}
		}
		// Sorts who gets global broadcast and who gets player message depending on config setup
		sortWhoGetsWhatChat(globalBroadcast, privateMessage, playerSender, globalMsgLines, privateMsgLines)
	}
	
	// TODO FIX ERRORS
	fun broadcastStatusPause(sender: CommandSender?) {
		// Check if player executed command
		val playerSender: Player? = if (sender is Player) sender else null
		// Placeholder setups and message fetch
		val globalMsgLines = ConfigManager.getStringListColor("global_broadcast.messages.status.pause")
		val privateMsgLines = ConfigManager.getStringListColor("private_messages.messages.status.pause")
		// Boolean shortcuts
		var globalBroadcast = ConfigManager.getBoolean("global_broadcast.enabled.status.pause")
		val privateMessage = ConfigManager.getBoolean("private_messages.enabled.status.pause")
		val globalPopup = ConfigManager.getBoolean("global_popups.enabled.status.pause")
		val privatePopup = ConfigManager.getBoolean("private_popups.enabled.status.pause")
		// check if global and player pop ups are off
		if (!globalPopup && !privatePopup) {
			if (!privateMessage) globalBroadcast = true
		} else {
			try {
				sortWhoGetsWhatPopUp(
					playerSender,
					ConfigManager.getPopup("global_popups.messages.status.pause"),
					ConfigManager.getPopup("private_popups.messages.status.pause"),
					arrayOf()
				)
			} catch (e: Exception) {
				catchError(e, "Messenger.broadcastStatusPause():SortWhoGetsWhatPopUp")
			}
		}
		// Sorts who gets global broadcast and who gets player message depending on config setup
		sortWhoGetsWhatChat(playerSender, globalMsgLines, privateMsgLines)
	}
	
	// TODO FIX ERRORS
	fun broadcastChange(sender: CommandSender?) {
		// Check if player executed command
		val playerSender: Player? = if (sender is Player) sender else null
		// Message fetch
		val globalMsgLines = ConfigManager.getStringListColor("global_broadcast.messages.change")
		val privateMsgLines = ConfigManager.getStringListColor("private_messages.messages.change")
		// Boolean shortcuts
		var globalBroadcast = ConfigManager.getBoolean("global_broadcast.enabled.change")
		val privateMessage = ConfigManager.getBoolean("private_messages.enabled.change")
		val globalPopup = ConfigManager.getBoolean("global_popups.enabled.change")
		val privatePopup = ConfigManager.getBoolean("private_popups.enabled.change")
		// check if global and player pop ups are off
		if (!globalPopup && !privatePopup) {
			if (!privateMessage) globalBroadcast = true
		} else {
			try {
				sortWhoGetsWhatPopUp(
					playerSender,
					ConfigManager.getPopup("global_popups.messages.change"),
					ConfigManager.getPopup("private_popups.messages.change"),
					arrayOf(fH, fM, fS)
				)
			} catch (e: Exception) {
				catchError(e, "Messenger.broadcastChange():SortWhoGetsWhatPopUp")
			}
		}
		// Format message lines, before processing in sortWhoGetsWhatChat()
		for (i in globalMsgLines.indices) globalMsgLines[i] = format(globalMsgLines[i], arrayOf(fH, fM, fS))
		for (i in privateMsgLines.indices) privateMsgLines[i] = format(privateMsgLines[i], arrayOf(fH, fM, fS))
		sortWhoGetsWhatChat(playerSender, globalMsgLines, privateMsgLines)
	}
	
	fun broadcastMaxplayersAlert() {
		// Placeholder setups and message fetch
		val msg = Config.GlobalBroadcast_MaxPlayers_Alert
		val popup = Config.GlobalPopups_MaxPlayers_Alert
		// Check if pop ups are enabled
		if (popup.enabled) {
			// send pop ups
			for (player in Bukkit.getOnlinePlayers()) {
				try {
					sendTitle(player!!, popup, arrayOf(fA))
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
			for (player in Bukkit.getOnlinePlayers()) {
				try {
					sendTitle(player!!, popup, arrayOf(fD))
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
			for (player in Bukkit.getOnlinePlayers()) {
				try {
					sendTitle(player!!, popup, arrayOf())
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
			for (player in Bukkit.getOnlinePlayers()) {
				try {
					sendTitle(player!!, popup, arrayOf())
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