package org.serversmc.autorestart.cmds.autore

import org.bukkit.command.*
import org.bukkit.entity.*
import org.bukkit.permissions.*
import org.serversmc.autorestart.cmds.*
import org.serversmc.autorestart.core.*
import org.serversmc.autorestart.enums.*
import org.serversmc.autorestart.interfaces.*
import org.serversmc.autorestart.utils.*
import org.serversmc.autorestart.utils.Messenger.broadcastStatus
import java.lang.Integer.*

object CIn : ICommand {
	
	override fun execute(sender: CommandSender, args: MutableList<out String>) {
		// Check if arguments are empty
		if (args.isEmpty()) {
			sender.sendMessage(RED + Lang.getNode("commands.in.not-enough-args"))
			return
		}
		// Variable initialization
		var time = 0
		val usedVars = ArrayList<String>()
		// Argument add/parsing loop
		args.forEach {
			// Check if format is valid ( ":" )
			if (!it.contains(":")) {
				sender.sendMessage(RED + Lang.getNode("commands.in.follow-format"))
				return@execute
			}
			// Split Argument
			val vars = it.split(":")
			// Check if format is valid ( ":" count == 1 )
			if (vars.size != 2) {
				sender.sendMessage(RED + Lang.getNode("commands.in.follow-format"))
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
				sender.sendMessage(RED + Lang.getNode("commands.in.enter-number"))
				return@execute
			}
			// Check if number is a negative or zero
			if (num < 1) {
				sender.sendMessage(RED + Lang.getNode("commands.in.invalid-number"))
				return@execute
			}
			// Check if parameter has already been used
			if (usedVars.contains(type)) {
				sender.sendMessage(RED + Lang.getNode("commands.in.type-in-use").replace("%t", type))
				return@execute
			}
			// Add up parsed time
			time += when (type) {
				"H" -> num * 3600
				"M" -> num * 60
				"S" -> num
				else -> {
					if (type.isBlank()) sender.sendMessage(RED + Lang.getNode("commands.in.enter-arg-name"))
					else sender.sendMessage(RED + Lang.getNode("commands.in.unknown-suffix").replace("%t", type))
					return@execute
				}
			}
			// Add used parameter
			usedVars.add(type)
		}
		// Update timer thread with new time view
		TimerThread.TIME = time
		// Send updated time to appropriate players (Method automatically sorts who gets what message, and pop ups)
		broadcastStatus(sender, Messenger.Status.CHANGE)
	}
	
	override fun tabComplete(player: Player, args: MutableList<out String>): MutableList<String>? {
		val params = arrayOf("h", "m", "s").toMutableList()
		val usedParams = ArrayList<String?>()
		val last = args.last()
		args.forEach {
			if (it.contains(":")) {
				val suffix = it.split(":")[1].toLowerCase()
				if (params.contains(suffix)) params.remove(suffix)
				else if (suffix.isNotEmpty()) return arrayOf("not valid").toMutableList()
				val nSuffix = if (suffix.isNotEmpty()) suffix else null
				if (!usedParams.contains(nSuffix)) usedParams.add(nSuffix)
				else return arrayOf("not valid").toMutableList()
			}
		}
		if (usedParams.contains(null)) if (args.size > usedParams.size || usedParams.last() != null) return arrayOf("not valid").toMutableList()
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
					if (suffix.isEmpty()) tabReturn(params, last)
					else when {
						usedParams.contains(suffix) and usedParams.isNotEmpty() -> arrayOf("not valid").toMutableList()
						arrayOf("h", "m", "s").contains(suffix) -> arrayOf(last).toMutableList()
						else -> arrayOf("not valid").toMutableList()
					}
				}
				else arrayOf("not valid").toMutableList()
			}
			else if (num == null) arrayOf("not valid").toMutableList()
			else tabReturn(params, "$last:")
		}
		return if (params.isEmpty()) arrayOf("not valid").toMutableList()
		else tabReturn(params, "#:")
	}
	
	private fun tabReturn(list: MutableList<String>, suffix: String): MutableList<String> {
		list.forEachIndexed { i, s ->
			list[i] = suffix + s
		}
		return list
	}
	
	override fun getLabel(): String = "IN"
	override fun getPermString(): String = "autorestart.in"
	override fun getPermDefault(): PermissionDefault = OP
	override fun getUsage(): String = "/autore in [hours]:h [minutes]:m [seconds]:s"
	override fun getDescription(): String = Lang.getNode("commands.in.description")
	override fun hasListener(): Boolean = false
	override fun getSubCmd(): ICommand? = CAutoRestart
	
	
}