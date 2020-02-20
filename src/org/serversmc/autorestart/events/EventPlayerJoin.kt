package org.serversmc.autorestart.events

import net.md_5.bungee.api.*
import net.md_5.bungee.api.chat.*
import org.bukkit.event.*
import org.bukkit.event.player.*
import org.serversmc.autorestart.core.UpdateChecker.LATEST_VERSION
import org.serversmc.autorestart.core.UpdateChecker.UPDATE_FOUND
import org.serversmc.utils.ChatColor.RED

object EventPlayerJoin : Listener {
	
	@EventHandler
	fun onPlayerJoin(event: PlayerJoinEvent) {
		// Get Player Entity
		val player = event.player
		// Check if there is an update
		if (!(UPDATE_FOUND as Boolean)) return
		// Check if player has permissions
		if (!player.hasPermission("autorestart.admin")) return
		// Prompt update message
		player.sendMessage("${RED}AutoRestart has an update! Please update to version v$LATEST_VERSION")
		player.spigot().sendMessage(TextComponent().apply {
			text = "    "
			addExtra(TextComponent().apply {
				text = "[ Link to Resource ]"
				isUnderlined = true
				color = ChatColor.BLUE
				clickEvent = ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.spigotmc.org/resources/autorestart.2538/")
				hoverEvent = HoverEvent(HoverEvent.Action.SHOW_TEXT, arrayOf(TextComponent("http://spigotmc.org/")))
			})
		})
	}
	
}