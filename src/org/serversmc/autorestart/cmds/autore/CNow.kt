package org.serversmc.autorestart.cmds.autore

import org.bukkit.command.*
import org.bukkit.entity.*
import org.bukkit.permissions.*
import org.serversmc.autorestart.cmds.*
import org.serversmc.autorestart.core.TimerThread.TIME
import org.serversmc.autorestart.enums.*
import org.serversmc.autorestart.interfaces.*

object CNow : ICommand {
	
	override fun execute(sender: CommandSender, args: MutableList<out String>) {
		sender.sendMessage("${GRAY}Restarting server!")
		TIME = 0
	}
	
	override fun tabComplete(player: Player, args: MutableList<out String>): MutableList<String>? = ArrayList()
	override fun getLabel(): String = "NOW"
	override fun getPermString(): String = "autorestart.now"
	override fun getPermDefault(): PermissionDefault = OP
	override fun getUsage(): String = "/autore now"
	override fun getDescription(): String = "Force restart server."
	override fun hasListener(): Boolean = false
	override fun getSubCmd(): ICommand? = CAutoRestart
	
}