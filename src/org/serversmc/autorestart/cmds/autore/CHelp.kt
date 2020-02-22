package org.serversmc.autorestart.cmds.autore

import org.bukkit.command.*
import org.bukkit.entity.*
import org.serversmc.autorestart.cmds.CAutoRestart.subCommands
import org.serversmc.autorestart.core.Main.Companion.AutoRestart
import org.serversmc.interfaces.*
import org.serversmc.utils.ChatColor.GRAY
import org.serversmc.utils.ChatColor.RED
import java.io.*

object CHelp : ICommand {
	
	override fun execute(sender: CommandSender, args: MutableList<out String>) {
		// Check if argument number requirement meet
		if (args.isEmpty()) {
			// List Sub Commands usages and descriptions
			subCommands.forEach {
				// Checks if player has permission for this command
				if (!sender.hasPermission(it.getPermission())) return@forEach
				sender.sendMessage("${GRAY}${it.getUsage()}${RED} - ${GRAY}${it.getDescription()}")
			}
			// Prevent dictionary search
			return
		}
		// Finding sub commands dictionary
		subCommands.forEach {
			// Checks if label matches argument
			if (!it.getLabel().equals(args[0], true)) return@forEach
			// Checks if player has permission for this command
			if (!sender.hasPermission(it.getPermission())) {
				sender.sendMessage("${RED}You do not have permission to view this sub command dictionary!")
				// Prevent dictionary print
				return@execute
			}
			// Fetch and display dictionary
			// Setup buffered Reader
			val stream = InputStreamReader(AutoRestart.getResource("help_dictionary/${it.getLabel().toLowerCase()}.dict")!!)
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
		sender.sendMessage("${RED}That sub command was not found! Type \"/autore help\" to view that list of commands!")
	}
	
	override fun tabComplete(player: Player, args: MutableList<out String>): MutableList<String>? {
		return ArrayList<String>().apply {
			if (args.size > 1) add(tabIgnored)
			else subCommands.forEach {
				if (!player.hasPermission(it.getPermission())) return@forEach
				if (it.getLabel().toLowerCase().startsWith(args[0].toLowerCase())) {
					add(it.getLabel().toLowerCase().substring(args.last().length))
				}
			}
			if (isEmpty()) add(tabNotValid)
		}
	}
	
	override fun getLabel(): String = "HELP"
	override fun getPermission(): String = "autorestart.help"
	override fun getUsage(): String = "/autore help <command>"
	override fun getDescription(): String = "Shows this help screen."
	
}