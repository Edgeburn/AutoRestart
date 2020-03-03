package org.serversmc.autorestart.cmds.autore

import org.bukkit.command.*
import org.bukkit.entity.*
import org.bukkit.permissions.*
import org.serversmc.autorestart.cmds.*
import org.serversmc.autorestart.interfaces.*
import org.serversmc.autorestart.utils.*

object CTime : ICommand {
	
	override fun execute(sender: CommandSender, args: MutableList<out String>) = Messenger.message(sender, Messenger.Private.TIME)
	override fun tabComplete(player: Player, args: MutableList<out String>): MutableList<String>? = null
	override fun getLabel(): String = "TIME"
	override fun getPermString(): String = "autorestart.time"
	override fun getPermDefault(): PermissionDefault = TRUE
	override fun getUsage(): String = "/autore time"
	override fun getDescription(): String = "Shows when the next restart will occur."
	override fun hasListener(): Boolean = false
	override fun getSubCmd(): ICommand? = CAutoRestart
	
}