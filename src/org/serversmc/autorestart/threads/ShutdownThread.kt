package org.serversmc.autorestart.threads

import org.bukkit.*
import org.serversmc.autorestart.core.*
import org.serversmc.autorestart.utils.*

object ShutdownThread : Thread() {
	
	private var TIME = 5
	
	override fun run() {
		// Broadcast shutdown message
		Messenger.broadcast(Messenger.Global.SHUTDOWN)
		// Player kick / restart message
		Bukkit.getScheduler().callSyncMethod(PLUGIN) {
			Bukkit.getOnlinePlayers().forEach { player ->
				player.kickPlayer(Config.Main_KickMessage)
			}
		}
		// Wait until players are successfully kicked, unless timeout is called
		while (true) {
			if ((TIME == 0) or Bukkit.getOnlinePlayers().isEmpty()) {
				Bukkit.getScheduler().scheduleSyncDelayedTask(PLUGIN) {
					Bukkit.dispatchCommand(Console.consoleSender, Config.Main_Execution)
				}
				break
			}
			// Decrement shutdown delay
			TIME--
			// Delay Runnable
			sleep(1000)
		}
	}
	
}