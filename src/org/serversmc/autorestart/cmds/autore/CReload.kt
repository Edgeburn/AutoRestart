package org.serversmc.autorestart.cmds.autore

import org.bukkit.command.*
import org.bukkit.entity.*
import org.serversmc.autorestart.interfaces.*
import org.serversmc.autorestart.utils.*
import org.serversmc.utils.ChatColor.GRAY

object CReload : ICommand {
	
	override fun execute(sender: CommandSender, args: MutableList<out String>) {
		Config.reloadConfig()
		if (Config.Main_RecalculateOnreload) Config.Main_RestartMode.calculate()
		sender.sendMessage("${GRAY}Config files have been reloaded!")
	}
	
	override fun tabComplete(player: Player, args: MutableList<out String>): MutableList<String>? = ArrayList()
	override fun getLabel(): String = "RELOAD"
	override fun getPermission(): String = "autorestart.reload"
	override fun getUsage(): String = "/autore reload"
	override fun getDescription(): String = "Reloads all of AutoRestart config files"
	
}