package org.serversmc.autorestart.interfaces

import org.bukkit.*
import org.bukkit.command.*
import org.bukkit.entity.*
import org.bukkit.event.*
import org.bukkit.permissions.*
import org.serversmc.autorestart.core.*
import org.serversmc.autorestart.enums.*
import org.serversmc.autorestart.utils.*

@Suppress("warnings")
interface ICommand : CommandExecutor, TabCompleter, Listener {
	
	object SubCmdManager {
		
		private val subCmds = HashMap<ICommand, ArrayList<ICommand>>()
		
		fun getSubCommands(cmd: ICommand) = subCmds[cmd] ?: ArrayList<ICommand>()
		
		fun addCommand(parent: ICommand, child: ICommand) {
			subCmds[parent]?.apply {
				add(child)
			} ?: subCmds.set(parent, ArrayList<ICommand>().apply {
				add(child)
			})
		}
		
	}
	
	class PlayerOnlyCommand() : Exception() {
		var s: String? = null
		
		constructor(s: String) : this() {
			this.s = s
		}
		
		override val message: String? get() = s
	}
	
	val TRUE get() = PermissionDefault.TRUE
	val FALSE get() = PermissionDefault.FALSE
	val OP get() = PermissionDefault.OP
	val NOT_OP get() = PermissionDefault.NOT_OP
	
	val subCommands get() = SubCmdManager.getSubCommands(this)
	
	fun register() {
		// Register Permission
		Bukkit.getPluginManager().permissions.add(getPermission())
		// Register Listener
		if (hasListener()) Bukkit.getPluginManager().registerEvents(this, PLUGIN)
		// Check if this is a sub command
		if (getSubCmd() == null) {
			// Register command to bukkit
			PLUGIN.getCommand(getLabel())?.apply {
				setExecutor(this@ICommand)
				tabCompleter = this@ICommand
				usage = this@ICommand.getUsage()
				description = this@ICommand.getDescription()
			}
			return
		}
		// Add sub command to array in SubCmdManager
		SubCmdManager.addCommand(getSubCmd()!!, this)
	}
	
	override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
		// Display header
		sender.sendMessage("${WHITE}-{ ${AQUA}${PLUGIN.description.name}$GRAY v${PLUGIN.description.version} ${WHITE}}-")
		// Check if sender has permission
		if (!sender.hasPermission(getPermission())) {
			sender.sendMessage(RED + Lang.getNode("commands.main.no-permission"))
		}
		// Try to execute command
		try {
			// Check if command has subcommands or args are empty
			if (subCommands.isEmpty() || args.isEmpty()) {
				// Execute main command
				execute(sender, args.toMutableList())
				return true
			}
			// Iterate through subcommands if present
			subCommands.forEach {
				// Check if subcommand label matches args[0]
				if (!it.getLabel().equals(args[0], true)) return@forEach
				// Check if sender has permission to subcommand
				if (!sender.hasPermission(it.getPermission())) {
					sender.sendMessage(RED + Lang.getNode("commands.main.no-sub-permission"))
					return true
				}
				// Execute subcommand
				it.execute(sender, args.toMutableList().apply { removeAt(0) })
				return true
			}
			// Prompt subcommand not found
			sender.sendMessage(RED + Lang.getNode("commands.main.sub-not-found").replace("%u", getUsage()))
			// Catch any exceptions
		} catch (e: PlayerOnlyCommand) {
			sender.sendMessage(RED + Lang.getNode("commands.main.player-only"))
			if (e.message != null) {
				sender.sendMessage("$RED${e.message}")
			}
		} catch (e: Exception) {
			Console.catchError(e, "ICommand.onCommand(CommandSender, Command, String, Array<out String>): Boolean")
		}
		return true
	}
	
	override fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<out String>): MutableList<String>? {
		if (sender is Player) return try {
			tabComplete(sender, args.toMutableList())
		} catch (e: Exception) {
			Console.catchError(e, "ICommand.onTabComplete()")
			ArrayList<String>()
		}
		return ArrayList<String>()
	}
	
	fun execute(sender: CommandSender, args: MutableList<out String>)
	fun tabComplete(player: Player, args: MutableList<out String>): MutableList<String>?
	fun getLabel(): String
	fun getPermission() = Permission(getPermString(), getPermDefault())
	fun getPermString(): String
	fun getPermDefault(): PermissionDefault
	fun getUsage(): String
	fun getDescription(): String
	fun hasListener(): Boolean
	fun getSubCmd(): ICommand?
	
}