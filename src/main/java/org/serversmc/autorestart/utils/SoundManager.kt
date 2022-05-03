package org.serversmc.autorestart.utils

import org.bukkit.*
import org.bukkit.entity.*

object SoundManager {

	fun playBroadcast(section: Config.ConfigSection) {
		// Initialize ConfigSection
		val (msg, popup) = section
		// Check if sound should play
		if ((popup.enabled || msg.enabled) && Config.Sounds_Broadcast_Enabled) {
			// Play sound to everyone
			Bukkit.getOnlinePlayers().forEach {
				it.playNote(it.location, Instrument.PIANO, Note.natural(0, Note.Tone.C))
			}
		}
	}
	
	fun playPrivate(section: Config.ConfigSection, player: Player) {
		// Initialize ConfigSection
		val (msg, popup) = section
		// Check if sound should play
		if ((popup.enabled || msg.enabled) && Config.Sounds_Private_Enabled) {
			// Play sound to player
			if (Config.Sounds_Private_Enabled) player.playNote(player.location, Instrument.PIANO, Note.natural(0, Note.Tone.G))
		}
	}
	
	fun playShutdown() {
		// Check if sound should play
		if (Config.Sounds_Shutdown_Enabled) {
			// Play sound to everyone
			Bukkit.getOnlinePlayers().forEach { it.playSound(it.location, Sound.UI_TOAST_CHALLENGE_COMPLETE, 100.0f, 1.0f) }
		}
	}

}