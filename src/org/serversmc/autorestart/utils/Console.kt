package org.serversmc.autorestart.utils

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.serversmc.autorestart.enums.GREEN
import org.serversmc.autorestart.enums.RED
import org.serversmc.autorestart.enums.YELLOW

object Console {
    
    val consoleSender = Bukkit.getConsoleSender()
    private const val p = "[AutoRestart] "
    
    fun info(s: String) = Bukkit.getConsoleSender().sendMessage("$GREEN$p$s")
    fun warn(s: String) = Bukkit.getConsoleSender().sendMessage("$YELLOW$p$s")
    fun err(s: String) = Bukkit.getConsoleSender().sendMessage("$RED$p$s")
    fun consoleSendMessage(s: String) = consoleSender.sendMessage(ChatColor.translateAlternateColorCodes('&', s))
    
    fun catchError(e: Exception, loc: String) {
        err("There was an error in $loc")
        consoleSendMessage(e.toString())
        e.stackTrace.forEach { if (it.toString().startsWith("org.serversmc.autorestart")) consoleSendMessage("\t" + it.toString()) }
        err("End of error")
    }
    
}
