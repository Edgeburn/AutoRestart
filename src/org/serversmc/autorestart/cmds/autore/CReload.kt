package org.serversmc.autorestart.cmds.autore

import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.serversmc.autorestart.enums.GRAY
import org.serversmc.autorestart.interfaces.ICommand
import org.serversmc.autorestart.utils.Config.reloadConfig

object CReload: ICommand {
	
	override fun execute(sender: CommandSender, args: MutableList<out String>) {
		reloadConfig()
		sender.sendMessage("${GRAY}Config files have been reloaded!")
	}
	
	override fun tabComplete(player: Player, args: MutableList<out String>): MutableList<String>? = ArrayList()
	override fun getLabel(): String = "RELOAD"
	override fun getPermission(): String? = "autorestart.reload"
	override fun getUsage(): String = "/autore reload"
	override fun getDescription(): String = "Reloads all of AutoRestart config files"
	
}