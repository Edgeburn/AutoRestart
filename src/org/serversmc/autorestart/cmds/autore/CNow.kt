package org.serversmc.autorestart.cmds.autore

import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.serversmc.autorestart.core.TimerThread.TIME
import org.serversmc.autorestart.enums.GRAY
import org.serversmc.autorestart.interfaces.ICommand

object CNow: ICommand {
	
	override fun execute(sender: CommandSender, args: MutableList<out String>) {
		sender.sendMessage("${GRAY}Restarting server!")
		TIME = 0
	}
	
	override fun tabComplete(player: Player, args: MutableList<out String>): MutableList<String>? = ArrayList()
	override fun getLabel(): String = "NOW"
	override fun getPermission(): String? = "autorestart.now"
	override fun getUsage(): String = "/autore now"
	override fun getDescription(): String = "Force restarts server."
	
}