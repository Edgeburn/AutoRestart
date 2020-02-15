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
		override fun replace(): String = ConfigManager.getString("max_players.amount")
	}

	private val fD = object : Format {
		override fun regex(): String = "%d"
		override fun replace(): String = ConfigManager.getString("max_players.delay")
	}
	
	private fun format(s: String, vars: Array<Format>): String {
		var output = s
		vars.forEach { output = output.replace(it.regex(), it.replace()) }
		return output
	}
	
	private fun getPrefix(): String = ConfigManager.getString("main.prefix")
	private fun broadcastMessage(msg: String) = Bukkit.broadcastMessage(getPrefix() + msg)
	private fun broadcastMessage(msg: String, perm: String) = Bukkit.broadcast(getPrefix() + msg, perm)
	
	private fun sendTitle(player: Player, popup: Popup, format: Array<Format>) {
		TitleAPI.sendTitle(player, 0, 0, 0, "", "")
		val title: ConfigTitle = popup.title
		val subtitle: ConfigTitle = popup.subtitle
		try {
			TitleAPI.sendTitle(player, title.fadeIn, title.stay, title.fadeOut, format(title.text, format), null)
			TitleAPI.sendTitle(player, subtitle.fadeIn, subtitle.stay, subtitle.fadeOut, null, format(subtitle.text, format))
		} catch (e: Exception) {
			catchError(e, "Messenger.sendTitle():formatter")
		}
	}
	
	private fun sortWhoGetsWhatChat(globalBroadcast: Boolean, privateMessage: Boolean, playerSender: Player?, globalMsgLines: List<String>, privateMsgLines: List<String>) {
		// Check if global broadcast is enabled
		if (globalBroadcast) {
			// Everyone but console and/or sender gets global message
			for (player in Bukkit.getOnlinePlayers()) {
				if (player == playerSender && privateMessage) {
					continue
				}
				globalMsgLines.forEach { player.sendMessage(getPrefix() + it) }
			}
		}
		// Check if player broadcast is enabled
		if (privateMessage) {
			// Check if player triggered event
			if (playerSender != null) {
				// send private message to player
				privateMsgLines.forEach { playerSender.sendMessage(it) }
			}
		}
		// Check if console is sender and set console message list
		val consoleList: List<String> = if (playerSender == null) {
			privateMsgLines
		} else {
			globalMsgLines
		}
		// Send Message to console
		consoleList.forEach { consoleSendMessage(it) }
	}
	
	private fun sortWhoGetsWhatPopUp(globalPopup: Boolean, privatePopup: Boolean, playerSender: Player?, globalPopupConfig: Popup, privatePopupConfig: Popup, format: Array<Format>) {
		// Check if global pop up is enabled
		if (globalPopup) {
			// Everyone but sender gets global message
			for (player in Bukkit.getOnlinePlayers()) {
				if ((player == playerSender) && privatePopup) {
					continue
				}
				sendTitle(player, globalPopupConfig, format)
			}
		}
		// Check if player pop up is enabled
		if (privatePopup) {
			// Check if player triggered event
			if (playerSender != null) {
				// send private pop up to player
				sendTitle(playerSender, privatePopupConfig, format)
			}
		}
	}
	
	fun broadcastReminderMinutes() {
		val msgLines = ConfigManager.getStringListColor("global_broadcast.messages.minutes")
		// Check if pop ups are enabled
		if (ConfigManager.getBoolean("global_popups.enabled.minutes")) {
			// send pop ups
			Bukkit.getOnlinePlayers().forEach {
				try {
					sendTitle(it, ConfigManager.getPopup("global_popups.messages.minutes"), arrayOf(fM))
				} catch (e: Exception) {
					catchError(e, "Messenger.broadcastReminderMinutes():SendPopUps")
				}
			}
			if (!ConfigManager.getBoolean("global_broadcast.enabled.minutes")) {
				// Send to console only
				msgLines.forEach { consoleSendMessage(format(it, arrayOf(fM))) }
				// Disable for players
				return
			}
		}
		// Send to everyone
		msgLines.forEach { broadcastMessage(format(it, arrayOf(fM))) }
	}
	
	fun broadcastReminderSeconds() {
		val msgLines = ConfigManager.getStringListColor("global_broadcast.messages.seconds")
		// Check if pop ups are enabled
		if (ConfigManager.getBoolean("global_popups.enabled.seconds")) {
			// send pop ups
			Bukkit.getOnlinePlayers().forEach { player: Player  ->
				try {
					sendTitle(player, ConfigManager.getPopup("global_popups.messages.seconds"), arrayOf(fS))
				} catch (e: Exception) {
					catchError(e, "Messenger.broadcastReminderSeconds():SendPopUps")
				}
			}
			if (!ConfigManager.getBoolean("global_broadcast.enabled.seconds")) {
				// Send to console only
				msgLines.forEach { consoleSendMessage(format(it, arrayOf(fS))) }
				// Disable for players
				return
			}
		}
		// Send to everyone
		msgLines.forEach { broadcastMessage(format(it, arrayOf(fS))) }
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
		val privatePopup = ConfigManager.getBoolean("PRIVATE_POPUPS.ENABLED.STATUS.resume")
		// check if global and player pop ups are off
		if (!globalPopup && !privatePopup) {
			if (!privateMessage) globalBroadcast = true
		} else {
			try {
				sortWhoGetsWhatPopUp(
					globalPopup,
					privatePopup,
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
					globalPopup, privatePopup, playerSender, ConfigManager.getPopup("global_popups.messages.status.pause"), ConfigManager.getPopup("private_popups.messages.status.pause"),
					arrayOf()
				)
			} catch (e: Exception) {
				catchError(e, "Messenger.broadcastStatusPause():SortWhoGetsWhatPopUp")
			}
		}
		// Sorts who gets global broadcast and who gets player message depending on config setup
		sortWhoGetsWhatChat(globalBroadcast, privateMessage, playerSender, globalMsgLines, privateMsgLines)
	}
	
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
					globalPopup,
					privatePopup,
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
		sortWhoGetsWhatChat(globalBroadcast, privateMessage, playerSender, globalMsgLines, privateMsgLines)
	}
	
	fun broadcastMaxplayersAlert() {
		// Placeholder setups and message fetch
		val msgLines = ConfigManager.getStringListColor("global_broadcast.messages.max_players.alert")
		// Check if pop ups are enabled
		if (ConfigManager.getBoolean("global_popups.enabled.max_players.alert")) {
			// send pop ups
			for (player in Bukkit.getOnlinePlayers()) {
				try {
					sendTitle(
						player!!,
						ConfigManager.getPopup("global_popups.messages.max_players.alert"),
						arrayOf(fA)
					)
				} catch (e: Exception) {
					catchError(e, "Messenger.broadcastMaxplayersAlert():SendPopUps")
				}
			}
			if (!ConfigManager.getBoolean("global_broadcast.enabled.max_players.alert")) {
				// Send to console only
				msgLines.forEach { consoleSendMessage(format(it, arrayOf(fA))) }
				// Disable for players
				return
			}
		}
		// Send to everyone
		msgLines.forEach { broadcastMessage(format(it, arrayOf(fA))) }
	}
	
	fun broadcastMaxplayersPreShutdown() {
		// Message fetch
		val msgLines = ConfigManager.getStringListColor("global_broadcast.messages.max_players.pre_shutdown")
		// Check if pop ups are enabled
		if (ConfigManager.getBoolean("global_popups.enabled.max_players.pre_shutdown")) {
			// send pop ups
			for (player in Bukkit.getOnlinePlayers()) {
				try {
					sendTitle(
						player!!,
						ConfigManager.getPopup("global_popups.messages.max_players.pre_shutdown"),
						arrayOf(fD)
					)
				} catch (e: Exception) {
					catchError(e, "Messenger.broadcastMaxplayersPreShutdown():SendPopUps")
				}
			}
			if (!ConfigManager.getBoolean("global_broadcast.enabled.max_players.pre_shutdown")) {
				// Send to console only
				msgLines.forEach { consoleSendMessage(format(it, arrayOf(fD))) }
				// Disable for players
				return
			}
		}
		// Send to everyone
		msgLines.forEach { broadcastMessage(format(it, arrayOf(fD))) }
	}
	
	fun broadcastPauseReminder() {
		// Message fetch
		val msgLines = ConfigManager.getStringListColor("private_messages.messages.pause_reminder")
		// Send to console only
		msgLines.forEach { consoleSendMessage(it) }
		// Check if pop ups are enabled
		if (ConfigManager.getBoolean("private_popups.enabled.pause_reminder")) {
			// send pop ups
			for (player in Bukkit.getOnlinePlayers()) {
				try {
					sendTitle(
						player!!,
						ConfigManager.getPopup("private_popups.messages.pause_reminder"),
						arrayOf()
					)
				} catch (e: Exception) {
					catchError(e, "Messenger.broadcastPauseReminder():SendPopUps")
				}
			}
			// Disable for players
			if (!ConfigManager.getBoolean("private_messages.enabled.pause_reminder")) return
		}
		// Send to everyone
		msgLines.forEach { broadcastMessage(it, "autorestart.start") }
	}
	
	fun broadcastShutdown() {
		// Message fetch
		val msgLines = ConfigManager.getStringListColor("global_broadcast.messages.shutdown")
		// Check if pop ups are enabled
		if (ConfigManager.getBoolean("global_popups.enabled.shutdown")) {
			// send pop ups
			for (player in Bukkit.getOnlinePlayers()) {
				try {
					sendTitle(
						player!!,
						ConfigManager.getPopup("global_popups.messages.shutdown"),
						arrayOf()
					)
				} catch (e: Exception) {
					catchError(e, "Messenger.broadcastShutdown():SendPopUps")
				}
			}
			if (!ConfigManager.getBoolean("global_broadcast.enabled.shutdown")) {
				// Send to console only
				msgLines.forEach { consoleSendMessage(it) }
				// Disable for players
				return
			}
		}
		// Send to everyone
		msgLines.forEach { broadcastMessage(it) }
	}
	
	fun messageSenderTime(sender: CommandSender) {
		// Message fetch
		val msgLines = ConfigManager.getStringListColor("private_messages.messages.time")
		// Checks if sender is a player
		if (sender is Player) {
			// Cast sender as player
			val player: Player = sender
			// Check if pop ups are enabled
			if (ConfigManager.getBoolean("private_popups.enabled.time")) {
				// send pop ups
				try {
					sendTitle(
						player,
						ConfigManager.getPopup("private_popups.messages.time"),
						arrayOf(fH, fM, fS)
					)
				} catch (e: Exception) {
					catchError(e, "Messenger.messageSenderTime():SendPopUps")
				}
				// Disables message
				if (!ConfigManager.getBoolean("private_messages.enabled.time")) return
			}
		}
		// Private message lines
		msgLines.forEach { sender.sendMessage(format(it, arrayOf(fH, fM, fS))) }
	}
}