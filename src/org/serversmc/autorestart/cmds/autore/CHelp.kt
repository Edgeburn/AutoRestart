package org.serversmc.autorestart.cmds.autore

import org.bukkit.command.*
import org.bukkit.entity.*
import org.serversmc.autorestart.cmds.CAutoRestart.subCommands
import org.serversmc.autorestart.core.Main.Companion.AutoRestart
import org.serversmc.autorestart.enums.*
import org.serversmc.autorestart.interfaces.*
import org.serversmc.autorestart.utils.Console.consoleSendMessage
import java.io.*

object CHelp : ICommand {
	
	override fun execute(sender: CommandSender, args: MutableList<out String>) {
		// Check if argument number requirement meet
		if (args.isEmpty()) {
			// List Sub Commands usages and descriptions
			subCommands.forEach {
				// Checks if player has permission for this command
				if (!it.hasPermission(sender)) return@forEach
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
			if (!it.hasPermission(sender)) {
				sender.sendMessage("${RED}You do not have permission to view this sub command dictionary!")
				if (sender is Player) consoleSendMessage(" Not enough permissions to view that sub commands dictionary!")
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
			// Console Notify
			if (sender is Player) consoleSendMessage(" Player reading \"${it.getLabel().toLowerCase()}\" dictionary!")
			// Prevent not found prompt
			return@execute
		}
		// Sub command not found
		sender.sendMessage("${RED}That sub command was not found! Type \"/autore help\" to view that list of commands!")
		if (sender is Player) consoleSendMessage(" Entered an invalid sub command!")
	}
	
	override fun tabComplete(player: Player, args: MutableList<out String>): MutableList<String>? {
		return ArrayList<String>().apply {
			if (args.size > 1) add(tabIgnored)
			else subCommands.forEach {
				if (!it.hasPermission(player)) return@forEach
				if (it.getLabel().toLowerCase().startsWith(args[0].toLowerCase())) {
					add("$AQUA${args.last()}$RESET" + it.getLabel().toLowerCase().substring(args.last().length))
				}
			}
			if (isEmpty()) add(tabNotValid)
		}
	}
	
	override fun getLabel(): String = "HELP"
	override fun getPermission(): String? = null
	override fun getUsage(): String = "/autore help <command>"
	override fun getDescription(): String = "Shows this help screen."
	
}