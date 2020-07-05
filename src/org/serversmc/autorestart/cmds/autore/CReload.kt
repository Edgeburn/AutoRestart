package org.serversmc.autorestart.cmds.autore

import org.bukkit.command.*
import org.bukkit.entity.*
import org.bukkit.permissions.*
import org.serversmc.autorestart.cmds.*
import org.serversmc.autorestart.enums.*
import org.serversmc.autorestart.interfaces.*
import org.serversmc.autorestart.utils.*

object CReload : ICommand {
	
	override fun execute(sender: CommandSender, args: MutableList<out String>) {
		Config.reloadConfig()
		if (Config.Main_RecalculateOnreload) Config.Main_RestartMode.calculate()
		sender.sendMessage(GRAY + Lang.getNode("commands.reload.reloaded"))
	}
	
	override fun tabComplete(player: Player, args: MutableList<out String>): MutableList<String>? = ArrayList()
	override fun getLabel(): String = "RELOAD"
	override fun getPermString(): String = "autorestart.reload"
	override fun getPermDefault(): PermissionDefault = CPause.OP
	override fun getUsage(): String = "/autore reload"
	override fun getDescription(): String = Lang.getNode("commands.reload.description")
	override fun hasListener(): Boolean = false
	override fun getSubCmd(): ICommand? = CAutoRestart
	
}