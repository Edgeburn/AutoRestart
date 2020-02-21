package org.serversmc.autorestart.utils

import org.bukkit.*
import org.bukkit.command.*
import org.bukkit.entity.*
import org.serversmc.autorestart.objects.*
import org.serversmc.protocol.*
import org.serversmc.utils.Console.catchError
import org.serversmc.utils.Console.consoleSendMessage

object Messenger {
	
	data class Format(val regex: String, val replace: String)
	
	private val formatList = ArrayList<Format>().apply {
		add(Format(Config.Format_Hours, HMS.H.toString()))
		add(Format(Config.Format_Minutes, HMS.M.toString()))
		add(Format(Config.Format_Seconds, HMS.S.toString()))
		add(Format(Config.Format_Maxplayers_Amount, Config.MaxPlayers_Amount.toString()))
		add(Format(Config.Format_Maxplayers_Delay, Config.MaxPlayers_Delay.toString()))
	}
	
	private fun format(s: String): String {
		var output = s
		formatList.forEach { output = output.replace(it.regex, it.replace) }
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
	
	private fun sendTitle(player: Player, popup: Config.Popup) {
		TitleAPI.sendTitle(player, 0, 0, 0, "", "")
		val title = popup.title
		val subtitle = popup.subtitle
		try {
			TitleAPI.sendTitle(player, title.fadeIn, title.stay, title.fadeOut, format(title.text), format(subtitle.text))
		} catch (e: Exception) {
			catchError(e, "Messenger.sendTitle(Player, Config.Popup, Array<Format>:TitleAPI.sendTitle(Player, Int, Int, Int, String?, String?))")
		}
	}
	
	enum class Status(val globalSection: Config.ConfigSection, val privateSection: Config.ConfigSection) {
		RESUME(Config.Global_Status_Resume, Config.Private_Status_Resume),
		PAUSE(Config.Global_Status_Pause, Config.Private_Status_Pause),
		CHANGE(Config.Global_Change, Config.Private_Change)
	}
	
	enum class Global(val section: Config.ConfigSection) {
		MINUTES(Config.Global_Minutes),
		SECONDS(Config.Global_Seconds),
		MAXPLAYERS_ALERT(Config.Global_MaxPlayers_Alert),
		MAXPLAYERS_PRESHUTDOWN(Config.Global_MaxPlayers_PreShutdown),
		MAXPLAYERS_TIMEOUT(Config.Global_MaxPlayers_Timeout),
		SHUTDOWN(Config.Global_Shutdown), ;
	}
	
	enum class Private(val section: Config.ConfigSection) {
		PAUSE_REMINDER(Config.Private_PauseReminder),
		TIME(Config.Private_Time)
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
					sendTitle(it, popup)
				} catch (e: Exception) {
					catchError(e, "Messenger.broadcast(Global):sendTitle(Player, Config.Popup, Array<Format>)")
				}
			}
		}
		// Check if messages are enabled
		if (msg.enabled) {
			// Send to everyone
			msg.lines.forEach { broadcastMessage(format(it)) }
			// Disable for players
			return
		}
		// Send to console only
		msg.lines.forEach { consoleSendMessage(format(it)) }
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
					sendTitle(player, popup)
				} catch (e: Exception) {
					catchError(e, "Messenger.message(CommandSender, Private):sendTitle(Player, Config.Popup, Array<Format>)")
				}
			}
			// Disables message
			if (!msg.enabled) return
		}
		// Private message lines
		msg.lines.forEach { sender.sendMessage(format(it)) }
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
					sendTitle(player, privatePopup)
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
						sendTitle(player, privatePopup)
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
			privateMsg.lines.forEach { sender.sendMessage(format(it)) }
		}
		// Is called if private message is disabled
		if (globalMsg.enabled) {
			// Broadcast global message
			if (privateMsg.enabled) {
				// Broadcast global messages excluding sender
				globalMsg.lines.forEach { broadcastMessageExclude(format(it), sender) }
			}
			else {
				// Broadcast global messages including player
				globalMsg.lines.forEach { broadcastMessage(format(it)) }
			}
		}
	}
	
}