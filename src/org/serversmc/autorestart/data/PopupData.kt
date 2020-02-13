package org.serversmc.autorestart.data

import org.bukkit.ChatColor
import org.bukkit.configuration.ConfigurationSection

data class Popup(val section: ConfigurationSection) {
	val title = ConfigTitle(section.getConfigurationSection("title")!!)
	val subtitle = ConfigTitle(section.getConfigurationSection("subtitle")!!)
}

data class ConfigTitle(val section: ConfigurationSection) {
	val text = ChatColor.translateAlternateColorCodes('&', section.getString("text")!!)
	val fadeIn = section.getInt("fadein")
	val stay = section.getInt("stay")
	val fadeOut = section.getInt("fadeout")
}