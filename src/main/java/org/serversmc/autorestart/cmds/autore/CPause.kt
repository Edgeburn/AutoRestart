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

object CPause : ICommand {
	
	override fun execute(sender: CommandSender, args: MutableList<out String>) {
		if (!TimerThread.PAUSED) {
			TimerThread.PAUSED = true
			broadcastStatus(sender, Messenger.Status.PAUSE)
		}
		else sender.sendMessage(RED + Lang.getNode("commands.pause.already-paused"))
	}
	
	override fun tabComplete(player: Player, args: MutableList<out String>): MutableList<String>? = ArrayList()
	override fun getLabel(): String = "PAUSE"
	override fun getPermString(): String = "autorestart.pause"
	override fun getPermDefault(): PermissionDefault = OP
	override fun getUsage(): String = "/autore pause"
	override fun getDescription(): String = Lang.getNode("commands.pause.description")
	override fun hasListener(): Boolean = false
	override fun getSubCmd(): ICommand? = CAutoRestart
	
}