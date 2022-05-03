package org.serversmc.autorestart.cmds.autore

import org.bukkit.command.*
import org.bukkit.entity.*
import org.bukkit.permissions.*
import org.serversmc.autorestart.cmds.*
import org.serversmc.autorestart.core.*
import org.serversmc.autorestart.enums.*
import org.serversmc.autorestart.interfaces.*
import org.serversmc.autorestart.utils.*
import java.io.*

object CHelp : ICommand {
	
	override fun execute(sender: CommandSender, args: MutableList<out String>) {
		// Check if argument number requirement meet
		if (args.isEmpty()) {
			// List Sub Commands usages and descriptions
			CAutoRestart.subCommands.forEach {
				// Checks if player has permission for this command
				if (sender.hasPermission(it.getPermission())) {
					sender.sendMessage("${AQUA}${it.getUsage()} ${GRAY}: ${WHITE}${it.getDescription()}")
				}
			}
			// Prevent dictionary search
			return
		}
		// Finding sub commands dictionary
		CAutoRestart.subCommands.forEach {
			// Checks if label matches argument
			if (!it.getLabel().equals(args[0], true)) return@forEach
			// Checks if player has permission for this command
			if (!sender.hasPermission(it.getPermission())) {
				sender.sendMessage(RED + Lang.getNode("commands.help.sub-no-permission"))
				// Prevent dictionary print
				return@execute
			}
			// Fetch and display dictionary
			// Setup buffered Reader
			val stream = InputStreamReader(PLUGIN.getResource("help_dictionary/${it.getLabel().toLowerCase()}.dict")!!)
			val reader = BufferedReader(stream)
			// Output dictionary
			sender.sendMessage("${GRAY}${reader.readLine()}")
			// Close Stream
			reader.close()
			stream.close()
			// Prevent not found prompt
			return@execute
		}
		// Sub command not found
		sender.sendMessage(RED + Lang.getNode("commands.help.sub-not-found"))
	}
	
	override fun tabComplete(player: Player, args: MutableList<out String>): MutableList<String>? {
		return ArrayList<String>().apply {
			if (args.size > 1) add("ignored")
			else CAutoRestart.subCommands.forEach {
				if (player.hasPermission(it.getPermission())) {
					if (it.getLabel().toLowerCase().startsWith(args[0].toLowerCase())) {
						add(it.getLabel().toLowerCase())
					}
				}
			}
			if (isEmpty()) add("not valid")
		}
	}
	
	override fun getLabel(): String = "HELP"
	override fun getPermString(): String = "autorestart.help"
	override fun getPermDefault(): PermissionDefault = TRUE
	override fun getUsage(): String = "/autore help <command>"
	override fun getDescription(): String = Lang.getNode("commands.help.description")
	override fun hasListener(): Boolean = false
	override fun getSubCmd(): ICommand? = CAutoRestart
	
}