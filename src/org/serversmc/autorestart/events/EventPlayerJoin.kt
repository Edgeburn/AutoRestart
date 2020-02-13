package org.serversmc.autorestart.events

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.serversmc.autorestart.enums.RED
import org.serversmc.autorestart.utils.UpdateChecker.LATEST_VERSION
import org.serversmc.autorestart.utils.UpdateChecker.UPDATE_FOUND

object EventPlayerJoin : Listener {
	
	@EventHandler
	fun onPlayerJoin(event : PlayerJoinEvent) {
		// Get Player Entity
		val player = event.player
		// Check if there is an update
		if (!(UPDATE_FOUND as Boolean)) return
		// Check if player is op
		if (!player.isOp) return
		// Check if player has permissions
		var found = false
		arrayOf(
			"autorestart.start",
			"autorestart.stop",
			"autorestart.reload",
			"autorestart.now",
			"autorestart.in"
		).forEach { if (player.hasPermission(it)) found = true }
		// Cancel if permission not found
		if (!found) return
		// Prompt update message
		player.sendMessage("${RED}AutoRestart has an update! Please update to version v$LATEST_VERSION")
	}
	
}