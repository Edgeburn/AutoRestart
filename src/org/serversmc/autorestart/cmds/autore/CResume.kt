package org.serversmc.autorestart.cmds.autore

import org.bukkit.command.*
import org.bukkit.entity.*
import org.serversmc.autorestart.core.TimerThread.PAUSED
import org.serversmc.autorestart.interfaces.*
import org.serversmc.autorestart.utils.*
import org.serversmc.autorestart.utils.Messenger.broadcastStatus
import org.serversmc.utils.ChatColor.RED
import org.serversmc.utils.Console.consoleSendMessage

object CResume: ICommand {
	
	override fun execute(sender: CommandSender, args: MutableList<out String>) {
		if (PAUSED) {
			PAUSED = false
			broadcastStatus(sender, Messenger.Status.RESUME)
		}
		else {
			sender.sendMessage("${RED}Timer is already running")
			if (sender is ConsoleCommandSender) consoleSendMessage(" Tried to use command, but timer is already counting down.")
		}
	}
	
	override fun tabComplete(player: Player, args: MutableList<out String>): MutableList<String>? = ArrayList()
	override fun getLabel(): String = "RESUME"
	override fun getPermission(): String? = "autorestart.resume"
	override fun getUsage(): String = "/autore resume"
	override fun getDescription(): String = "Resumes AutoRestart timer"
	
}