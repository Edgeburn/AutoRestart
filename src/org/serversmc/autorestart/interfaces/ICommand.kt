package org.serversmc.autorestart.interfaces

import org.bukkit.command.*
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.serversmc.autorestart.enums.ITALIC
import org.serversmc.autorestart.enums.RED
import org.serversmc.autorestart.utils.Console.catchError

interface ICommand : CommandExecutor, TabCompleter, Listener {
	
	val tabNotValid: String get() = "${ITALIC}not valid"
	val tabIgnored: String get() = "${ITALIC}ignored"
	
	override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
		if (hasPermission(sender)) {
			execute(sender, args.toMutableList())
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
	
	fun hasPermission(sender: CommandSender): Boolean = if (getPermission() == null) true else (sender is ConsoleCommandSender) or (sender.hasPermission(getPermission()!!))
	fun execute(sender: CommandSender, args: MutableList<out String>)
	fun tabComplete(player: Player, args: MutableList<out String>): MutableList<String>?
	fun getLabel(): String
	fun getPermission(): String?
	fun getUsage(): String
	fun getDescription(): String
	
}