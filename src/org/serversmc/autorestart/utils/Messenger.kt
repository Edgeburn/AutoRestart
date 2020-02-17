package org.serversmc.autorestart.utils

import org.bukkit.*
import org.bukkit.command.*
import org.bukkit.entity.*
import org.serversmc.autorestart.objects.*
import org.serversmc.console.Console.catchError
import org.serversmc.console.Console.consoleSendMessage
import org.serversmc.title.*

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
	
	enum class Status(val globalSection: ConfigSection, val privateSection: ConfigSection, val format: Array<Format>) {
		RESUME(Config.Global_Status_Resume, Config.Private_Status_Resume, arrayOf()),
		PAUSE(Config.Global_Status_Pause, Config.Private_Status_Pause, arrayOf()),
		CHANGE(Config.Global_Change, Config.Private_Change, arrayOf(fH, fM, fS))
	}
	
	enum class Global(val section: ConfigSection, val format: Array<Format>) {
		MINUTES(Config.Global_Minutes, arrayOf(fM)),
		SECONDS(Config.Global_Seconds, arrayOf(fS)),
		MAXPLAYERS_ALERT(Config.Global_MaxPlayers_Alert, arrayOf(fA)),
		MAXPLAYERS_PRESHUTDOWN(Config.Global_MaxPlayers_PreShutdown, arrayOf(fD)),
		SHUTDOWN(Config.Global_Shutdown, arrayOf()),;
	}
	
	enum class Private(val section: ConfigSection, val format: Array<Format>) {
		PAUSE_REMINDER(Config.Private_PauseReminder, arrayOf()),
		TIME(Config.Private_Time, arrayOf(fH, fM, fS))
	}
	
	fun broadcast(broadcast: Global) {
		val (msg, popup) = broadcast.section
		// Check if pop ups are enabled
		if (popup.enabled) {
			// send pop ups
			Bukkit.getOnlinePlayers().forEach {
				try {
					// Play Sound
					it.playNote(it.location, Instrument.PIANO, Note.natural(0, Note.Tone.C))
					// Send Title
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
		val (msg, popup) = private.section
		// Checks if sender is a player
		if (sender is Player) {
			// Cast sender as player
			val player: Player = sender
			
			// Play Sound
			player.playNote(player.location, Instrument.PIANO, Note.natural(0, Note.Tone.G))
			
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
		val (globalMsg, globalPopup) =  status.globalSection
		val (privateMsg, privatePopup) = status.privateSection
		// Check if global popups are enabled
		if (globalPopup.enabled) {
			// Check if private popups are enabled
			if (privatePopup.enabled && sender is Player) {
				try {
					sender.playNote(sender.location, Instrument.PIANO, Note.natural(0, Note.Tone.C))
					sendTitle(sender, privatePopup, arrayOf())
				} catch (e: Exception) {
					catchError(e, "Messenger.broadcastStatus(CommandSender, Status):sendTitle(Player, Popup, Array<Format>)")
				}
				Bukkit.getOnlinePlayers().forEach { if (it != sender) {
					it.playNote(it.location, Instrument.PIANO, Note.natural(0, Note.Tone.C))
					sendTitle(it, globalPopup, arrayOf())
				} }
			}
			else {
				Bukkit.getOnlinePlayers().forEach { sendTitle(it, globalPopup, status.format) }
			}
		}
		// Check if global messages are enabled
		if (globalMsg.enabled) {
			// Check if private messages are enabled
			if (privateMsg.enabled) {
				privateMsg.lines.forEach { sender.sendMessage(format(it, arrayOf(fH, fM, fS))) }
				if (sender is Player) globalMsg.lines.forEach { broadcastMessageExclude(format(it, arrayOf(fH, fM, fS)), sender) }
			}
			else {
				globalMsg.lines.forEach { broadcastMessage(format(it, arrayOf(fH, fM, fS))) }
			}
		}
	}
	
}