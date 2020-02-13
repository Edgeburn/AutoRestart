package org.serversmc.autorestart.cmds.autore

import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player
import org.serversmc.autorestart.core.TimerThread.PAUSED
import org.serversmc.autorestart.enums.RED
import org.serversmc.autorestart.interfaces.ICommand
import org.serversmc.autorestart.utils.Console.consoleSendMessage
import org.serversmc.autorestart.utils.Messenger.broadcastStatusResume

object CResume: ICommand {
	
	override fun execute(sender: CommandSender, args: MutableList<out String>) {
		if (PAUSED) {
			PAUSED = false
			broadcastStatusResume(sender)
		}
		else {
			sender.sendMessage("${RED}Timer is already running")
			if (sender is ConsoleCommandSender) consoleSendMessage(" Tried to use command, but timer is already counting down.")
		}
	}
	
	override fun tabComplete(player: Player, args: MutableList<out String>): MutableList<String>? = null
	override fun getLabel(): String = "RESUME"
	override fun getPermission(): String? = "autorestart.resume"
	override fun getUsage(): String = "/autore resume"
	override fun getDescription(): String = "Resumes AutoRestart timer"
	
}