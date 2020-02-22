package org.serversmc.autorestart.cmds.autore

import org.bukkit.command.*
import org.bukkit.entity.*
import org.serversmc.autorestart.core.TimerThread.PAUSED
import org.serversmc.autorestart.utils.*
import org.serversmc.autorestart.utils.Messenger.broadcastStatus
import org.serversmc.interfaces.*
import org.serversmc.utils.ChatColor.RED

object CPause : ICommand {
	
	override fun execute(sender: CommandSender, args: MutableList<out String>) {
		if (!PAUSED) {
			PAUSED = true
			broadcastStatus(sender, Messenger.Status.PAUSE)
		}
		else sender.sendMessage("${RED}Timer is already paused.")
	}
	
	override fun tabComplete(player: Player, args: MutableList<out String>): MutableList<String>? = ArrayList()
	override fun getLabel(): String = "PAUSE"
	override fun getPermission(): String = "autorestart.pause"
	override fun getUsage(): String = "/autore pause"
	override fun getDescription(): String = "Pauses the AutoRestart timer"
	
}