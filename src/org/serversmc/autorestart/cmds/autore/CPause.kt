package org.serversmc.autorestart.cmds.autore

import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player
import org.serversmc.autorestart.core.TimerThread.PAUSED
import org.serversmc.autorestart.enums.RED
import org.serversmc.autorestart.interfaces.ICommand
import org.serversmc.autorestart.utils.Console.consoleSendMessage
import org.serversmc.autorestart.utils.Messenger.broadcastStatusPause

object CPause: ICommand {
	
	override fun execute(sender: CommandSender, args: MutableList<out String>) {
		if (PAUSED) {
			sender.sendMessage("${RED}Timer is already paused.")
			if (sender is ConsoleCommandSender) consoleSendMessage(" Tried to use command, but timer is already paused.")
		}
		else {
			PAUSED = true
			broadcastStatusPause(sender)
		}
	}
	
	override fun tabComplete(player: Player, args: MutableList<out String>): MutableList<String>? = ArrayList()
	override fun getLabel(): String = "PAUSE"
	override fun getPermission(): String? = "autorestart.pause"
	override fun getUsage(): String = "/autore pause"
	override fun getDescription(): String = "Pauses the AutoRestart timer"
	
}