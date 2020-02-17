package org.serversmc.autorestart.cmds.autore

import org.bukkit.command.*
import org.bukkit.entity.*
import org.serversmc.autorestart.core.TimerThread.PAUSED
import org.serversmc.autorestart.enums.*
import org.serversmc.autorestart.interfaces.*
import org.serversmc.autorestart.utils.*
import org.serversmc.autorestart.utils.Messenger.broadcastStatus
import org.serversmc.console.Console.consoleSendMessage

object CPause: ICommand {
	
	override fun execute(sender: CommandSender, args: MutableList<out String>) {
		if (PAUSED) {
			sender.sendMessage("${RED}Timer is already paused.")
			if (sender is ConsoleCommandSender) consoleSendMessage(" Tried to use command, but timer is already paused.")
		}
		else {
			PAUSED = true
			broadcastStatus(sender, Messenger.Status.PAUSE)
		}
	}
	
	override fun tabComplete(player: Player, args: MutableList<out String>): MutableList<String>? = ArrayList()
	override fun getLabel(): String = "PAUSE"
	override fun getPermission(): String? = "autorestart.pause"
	override fun getUsage(): String = "/autore pause"
	override fun getDescription(): String = "Pauses the AutoRestart timer"
	
}