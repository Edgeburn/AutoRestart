package org.serversmc.autorestart.cmds.autore

import org.bukkit.command.*
import org.bukkit.entity.*
import org.serversmc.autorestart.core.TimerThread.calculateTimer
import org.serversmc.autorestart.enums.*
import org.serversmc.autorestart.interfaces.*
import org.serversmc.autorestart.utils.*

object CReload: ICommand {
	
	override fun execute(sender: CommandSender, args: MutableList<out String>) {
		Config.reloadConfig()
		if (Config.Main_RecalculateOnreload) calculateTimer()
		sender.sendMessage("${GRAY}Config files have been reloaded!")
	}
	
	override fun tabComplete(player: Player, args: MutableList<out String>): MutableList<String>? = ArrayList()
	override fun getLabel(): String = "RELOAD"
	override fun getPermission(): String? = "autorestart.reload"
	override fun getUsage(): String = "/autore reload"
	override fun getDescription(): String = "Reloads all of AutoRestart config files"
	
}