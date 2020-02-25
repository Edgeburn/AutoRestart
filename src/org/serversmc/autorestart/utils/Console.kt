package org.serversmc.autorestart.utils

import org.bukkit.*
import org.serversmc.autorestart.enums.*

object Console {
	
	val consoleSender = Bukkit.getConsoleSender()
	private const val p = "[AutoRestart] "
	
	fun info(s: String) = Bukkit.getConsoleSender().sendMessage("${ChatColor.GREEN}$p$s")
	fun warn(s: String) = Bukkit.getConsoleSender().sendMessage("${ChatColor.YELLOW}$p$s")
	fun err(s: String) = Bukkit.getConsoleSender().sendMessage("${ChatColor.RED}$p$s")
	fun sendMessage(s: String) = consoleSender.sendMessage(translateChatColor('&', s))
	
	fun catchError(e: Exception, loc: String) {
		err("There was an error in $loc")
		sendMessage(e.toString())
		e.stackTrace.forEach { if (it.toString().startsWith("org.serversmc")) sendMessage("\t" + it.toString()) }
		err("End of error")
	}
	
}
