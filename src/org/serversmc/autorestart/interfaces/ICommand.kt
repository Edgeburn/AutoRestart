package org.serversmc.autorestart.interfaces

import org.bukkit.command.*
import org.bukkit.entity.*
import org.bukkit.event.*
import org.serversmc.utils.ChatColor.RED
import org.serversmc.utils.Console.catchError

interface ICommand : CommandExecutor, TabCompleter, Listener {
	
	val tabNotValid: String get() = "not valid"
	val tabIgnored: String get() = "ignored"
	
	override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
		if (sender.hasPermission(getPermission())) {
			try {
				execute(sender, args.toMutableList())
			} catch (e: Exception) {
				catchError(e, "ICommand.onCommand(CommandSender, Command, String, Array<out String>): Boolean")
			}
		}
		else {
			sender.sendMessage("${RED}You don't have permission to use this command!")
		}
		return true
	}
	
	override fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<out String>): MutableList<String>? {
		if (sender is Player) return try {
			tabComplete(sender, args.toMutableList())
		} catch (e: Exception) {
			catchError(e, "ICommand.onTabComplete()")
			null
		}
		return null
	}
	
	fun execute(sender: CommandSender, args: MutableList<out String>)
	fun tabComplete(player: Player, args: MutableList<out String>): MutableList<String>?
	fun getLabel(): String
	fun getPermission(): String
	fun getUsage(): String
	fun getDescription(): String
	
}
