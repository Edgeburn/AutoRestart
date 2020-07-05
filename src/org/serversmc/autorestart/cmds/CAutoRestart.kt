package org.serversmc.autorestart.cmds

import org.bukkit.command.*
import org.bukkit.entity.*
import org.bukkit.permissions.*
import org.serversmc.autorestart.enums.*
import org.serversmc.autorestart.interfaces.*

object CAutoRestart : ICommand {
	
	override fun execute(sender: CommandSender, args: MutableList<out String>) {
		// Run Main command
		sender.sendMessage("${RED}Not enough arguments. Try: /autore help")
	}
	
	override fun tabComplete(player: Player, args: MutableList<out String>): MutableList<String>? {
		subCommands.forEach {
			if (!player.hasPermission(getPermission())) return@forEach
			if (it.getLabel().equals(args[0], true) and (args.size > 1)) return it.tabComplete(player, args.apply { removeAt(0) })
		}
		return ArrayList<String>().apply {
			subCommands.forEach {
				if (!player.hasPermission(getPermission())) return@forEach
				if (it.getLabel().toLowerCase().startsWith(args[0].toLowerCase())) {
					add(args.last() + it.getLabel().toLowerCase().substring(args.last().length))
				}
			}
		}
	}
	
	override fun getLabel(): String = "AUTORE"
	override fun getPermString(): String = "autorestart"
	override fun getPermDefault(): PermissionDefault = TRUE
	override fun getUsage(): String = "/autore <sub_command>"
	override fun getDescription(): String = "Main Autorestart command"
	override fun hasListener(): Boolean = false
	override fun getSubCmd(): ICommand? = null
	
}