package org.serversmc.autorestart.cmds.autore

import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.serversmc.autorestart.interfaces.ICommand
import org.serversmc.autorestart.utils.Messenger.messageSenderTime

object CTime: ICommand {
	
	override fun execute(sender: CommandSender, args: MutableList<out String>) = messageSenderTime(sender)
	override fun tabComplete(player: Player, args: MutableList<out String>): MutableList<String>? = null
	override fun getLabel(): String = "TIME"
	override fun getPermission(): String? = null
	override fun getUsage(): String = "/autore time"
	override fun getDescription(): String = "Shows when the next restart will occur."
	
}