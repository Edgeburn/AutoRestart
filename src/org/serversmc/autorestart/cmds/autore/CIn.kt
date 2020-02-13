package org.serversmc.autorestart.cmds.autore

import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.serversmc.autorestart.core.TimerThread.TIME
import org.serversmc.autorestart.enums.RED
import org.serversmc.autorestart.interfaces.ICommand
import org.serversmc.autorestart.utils.Messenger.broadcastChange
import java.lang.Integer.parseInt

object CIn : ICommand {
	
	
	override fun execute(sender: CommandSender, args: MutableList<out String>) {
		// Check if arguments are empty
		if (args.isEmpty()) {
			sender.sendMessage("${RED}Not enough arguments! Try: /autore help in")
			return
		}
		// Variable initialization
		var time = 0
		val usedVars = ArrayList<String>()
		// Argument add/parsing loop
		args.forEach {
			// Check if format is valid ( ":" )
			if (!it.contains(":")) {
				sender.sendMessage("${RED}Please follow format! Try: /autore help in")
				return@execute
			}
			// Split Argument
			val vars = it.split(":")
			// Check if format is valid ( ":" count == 1 )
			if (vars.size != 2) {
				sender.sendMessage("${RED}Please follow format! Try: /autore help in")
				return@execute
			}
			// Parse variables
			val num = try {
				parseInt(vars[0])
			} catch (e: NumberFormatException) {
				null
			}
			val type = vars[1].toUpperCase()
			// Check if number is valid
			if (num == null) {
				sender.sendMessage("${RED}Please enter a number! Try: /autore help in")
				return@execute
			}
			// Check if number is a negative or zero
			if (num < 1) {
				sender.sendMessage("${RED}Negative numbers, and zeros are not allowed! Try: /autore help in")
				return@execute
			}
			// Check if parameter has already been used
			if (usedVars.contains(type)) {
				sender.sendMessage("${RED}Argument $type has already been used! Try: /autore help in")
				sender.spigot()
				return@execute
			}
			// Add up parsed time
			time += when (type) {
				"H" -> num * 3600
				"M" -> num * 60
				"S" -> num
				else -> {
					if (type.isBlank()) sender.sendMessage("${RED}Please enter argument name! Try: /autore help in")
					else sender.sendMessage("${RED}Unknown suffix \"$type\"! Try: /autore help in")
					return@execute
				}
			}
			// Add used parameter
			usedVars.add(type)
		}
		// Update timer thread with new time view
		TIME = time
		// Send updated time to appropriate players (Method automatically sorts who gets what message, and pop ups)
		broadcastChange(sender)
	}
	
	override fun tabComplete(player: Player, args: MutableList<out String>): MutableList<String>? {
		val params = arrayOf("h", "m", "s").toMutableList()
		val usedParams = ArrayList<String?>()
		val last = args.last()
		args.forEach {
			if (it.contains(":")) {
				val suffix = it.split(":")[1].toLowerCase()
				if (params.contains(suffix)) params.remove(suffix)
				else if (suffix.isNotEmpty()) return arrayOf(tabNotValid).toMutableList()
				val nSuffix = if (suffix.isNotEmpty()) suffix else null
				if (!usedParams.contains(nSuffix)) usedParams.add(nSuffix)
				else return arrayOf(tabNotValid).toMutableList()
			}
		}
		if (usedParams.contains(null)) if (args.size > usedParams.size || usedParams.last() != null) return arrayOf(tabNotValid).toMutableList()
		if (usedParams.isNotEmpty()) usedParams.removeAt(usedParams.size - 1)
		if (last.isNotEmpty()) {
			val num = try {
				parseInt(if (last.contains(":")) last.split(":")[0] else last)
			} catch (e: NumberFormatException) {
				null
			}
			return if (last.contains(":")) {
				val suffix = last.split(":")[1].toLowerCase()
				if (last.split(":").size == 2) {
					if (suffix.isEmpty()) arrayOf(last + params.joinToString("/")).toMutableList()
					else when {
						usedParams.contains(suffix) and usedParams.isNotEmpty() -> arrayOf(tabNotValid).toMutableList()
						arrayOf("h", "m", "s").contains(suffix) -> arrayOf(last).toMutableList()
						else -> arrayOf(tabNotValid).toMutableList()
					}
				}
				else arrayOf(tabNotValid).toMutableList()
			}
			else if (num == null) arrayOf(tabNotValid).toMutableList()
			else arrayOf(last + params.joinToString("/", ":")).toMutableList()
		}
		return if (params.isEmpty()) arrayOf(tabNotValid).toMutableList()
		else arrayOf(params.joinToString("/", "#:")).toMutableList()
	}
	
	override fun getLabel(): String = "IN"
	override fun getPermission(): String? = null
	override fun getUsage(): String = "/autore in [hours]:h [minutes]:m [seconds]:s"
	override fun getDescription(): String = "Changes restart time in minutes."
	
}