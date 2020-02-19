package org.serversmc.autorestart.utils

import org.bukkit.*
import org.bukkit.command.*
import org.bukkit.entity.*
import org.serversmc.autorestart.objects.*
import org.serversmc.title.*
import org.serversmc.utils.Console.catchError
import org.serversmc.utils.Console.consoleSendMessage

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
	private fun broadcastMessageExclude(msg: String, sender: CommandSender) {
		Bukkit.getOnlinePlayers().forEach {
			if (it != sender) it.sendMessage(getPrefix() + msg)
		}
		if (sender is Player) consoleSendMessage(getPrefix() + msg)
	}
	
	private fun sendTitle(player: Player, popup: Config.Popup, format: Array<Format>) {
		TitleAPI.sendTitle(player, 0, 0, 0, "", "")
		val title = popup.title
		val subtitle = popup.subtitle
		try {
			TitleAPI.sendTitle(player, title.fadeIn, title.stay, title.fadeOut, format(title.text, format), format(subtitle.text, format))
		} catch (e: Exception) {
			catchError(e, "Messenger.sendTitle(Player, Config.Popup, Array<Format>:TitleAPI.sendTitle(Player, Int, Int, Int, String?, String?))")
		}
	}
	
	enum class Status(val globalSection: Config.ConfigSection, val privateSection: Config.ConfigSection, val format: Array<Format>) {
		RESUME(Config.Global_Status_Resume, Config.Private_Status_Resume, arrayOf()),
		PAUSE(Config.Global_Status_Pause, Config.Private_Status_Pause, arrayOf()),
		CHANGE(Config.Global_Change, Config.Private_Change, arrayOf(fH, fM, fS))
	}
	
	enum class Global(val section: Config.ConfigSection, val format: Array<Format>) {
		MINUTES(Config.Global_Minutes, arrayOf(fM)),
		SECONDS(Config.Global_Seconds, arrayOf(fS)),
		MAXPLAYERS_ALERT(Config.Global_MaxPlayers_Alert, arrayOf(fA)),
		MAXPLAYERS_PRESHUTDOWN(Config.Global_MaxPlayers_PreShutdown, arrayOf(fD)),
		SHUTDOWN(Config.Global_Shutdown, arrayOf()), ;
	}
	
	enum class Private(val section: Config.ConfigSection, val format: Array<Format>) {
		PAUSE_REMINDER(Config.Private_PauseReminder, arrayOf()),
		TIME(Config.Private_Time, arrayOf(fH, fM, fS))
	}
	
	fun broadcast(broadcast: Global) {
		// Initialize ConfigSection
		val (msg, popup) = broadcast.section
		// Call SoundManager
		SoundManager.playBroadcast(broadcast.section)
		// Check if pop ups are enabled
		if (popup.enabled) {
			// send pop ups
			Bukkit.getOnlinePlayers().forEach {
				try {
					// Send Title
					sendTitle(it, popup, broadcast.format)
				} catch (e: Exception) {
					catchError(e, "Messenger.broadcast(Global):sendTitle(Player, Config.Popup, Array<Format>)")
				}
			}
		}
		// Check if messages are enabled
		if (msg.enabled) {
			// Send to everyone
			msg.lines.forEach { broadcastMessage(format(it, broadcast.format)) }
			// Disable for players
			return
		}
		// Send to console only
		msg.lines.forEach { consoleSendMessage(format(it, broadcast.format)) }
	}
	
	fun message(sender: CommandSender, private: Private) {
		// Message fetch
		val (msg, popup) = private.section
		// Checks if sender is a player
		if (sender is Player) {
			// Cast sender as player
			val player: Player = sender
			// Call SoundManager
			SoundManager.playPrivate(private.section, player)
			// Check if pop ups are enabled
			if (popup.enabled) {
				// send pop ups
				try {
					sendTitle(player, popup, private.format)
				} catch (e: Exception) {
					catchError(e, "Messenger.message(CommandSender, Private):sendTitle(Player, Config.Popup, Array<Format>)")
				}
			}
			// Disables message
			if (!msg.enabled) return
		}
		// Private message lines
		msg.lines.forEach { sender.sendMessage(format(it, private.format)) }
	}
	
	fun broadcastStatus(sender: CommandSender, status: Status) {
		// Initialize ConfigSection
		val (globalMsg, globalPopup) = status.globalSection
		val (privateMsg, privatePopup) = status.privateSection
		// Check if sender is a Player
		if (sender is Player) {
			// Cast Player type
			val player: Player = sender
			// Check if private popups are enabled
			if (privatePopup.enabled) {
				// Try to send title to player
				try {
					// Send title to player
					sendTitle(player, privatePopup, status.format)
				} catch (e: Exception) {
					catchError(e, "Messenger.broadcastStatus(CommandSender, Status):sendTitle(Player, Popup, Array<Format>) // Private")
				}
			}
			// Check if global popups are enabled
			if (globalPopup.enabled) {
				// Check if private popups are enabled
				for (onlinePlayer in Bukkit.getOnlinePlayers()) {
					// Cancel global pop to sender if private popups are enabled
					if ((onlinePlayer == player) && privatePopup.enabled) continue
					// Try to send popup to every player
					try {
						// Send title to every player
						sendTitle(player, privatePopup, status.format)
					} catch (e: Exception) {
						catchError(e, "Messenger.broadcastStatus(CommandSender, Status):sendTitle(Player, Popup, Array<Format>) // Global")
					}
				}
			}
			// Call SoundManager
			SoundManager.playPrivate(status.privateSection, player)
		}
		// Call SoundManager
		SoundManager.playBroadcast(status.globalSection)
		// Check if private messages are enabled
		if (privateMsg.enabled) {
			// Send private messages if sender is not console
			privateMsg.lines.forEach { sender.sendMessage(format(it, status.format)) }
		}
		// Is called if private message is disabled
		if (globalMsg.enabled) {
			// Broadcast global message
			if (privateMsg.enabled) {
				// Broadcast global messages excluding sender
				globalMsg.lines.forEach { broadcastMessageExclude(format(it, status.format), sender) }
			}
			else {
				// Broadcast global messages including player
				globalMsg.lines.forEach { broadcastMessage(format(it, status.format)) }
			}
		}
	}
	
}