package org.serversmc.autorestart.cmds.autore

import org.bukkit.command.*
import org.bukkit.entity.*
import org.bukkit.permissions.*
import org.serversmc.autorestart.cmds.*
import org.serversmc.autorestart.core.TimerThread.PAUSED
import org.serversmc.autorestart.enums.*
import org.serversmc.autorestart.interfaces.*
import org.serversmc.autorestart.utils.*
import org.serversmc.autorestart.utils.Messenger.broadcastStatus

object CResume : ICommand {
	
	override fun execute(sender: CommandSender, args: MutableList<out String>) {
		if (PAUSED) {
			PAUSED = false
			broadcastStatus(sender, Messenger.Status.RESUME)
		}
		else sender.sendMessage("${RED}Timer is already running")
	}
	
	override fun tabComplete(player: Player, args: MutableList<out String>): MutableList<String>? = ArrayList()
	override fun getLabel(): String = "RESUME"
	override fun getPermString(): String = "autorestart.resume"
	override fun getPermDefault(): PermissionDefault = OP
	override fun getUsage(): String = "/autore resume"
	override fun getDescription(): String = "Resumes AutoRestart timer"
	override fun hasListener(): Boolean = false
	override fun getSubCmd(): ICommand? = CAutoRestart
	
}