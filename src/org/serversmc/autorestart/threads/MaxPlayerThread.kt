package org.serversmc.autorestart.threads

import org.bukkit.*
import org.serversmc.autorestart.utils.*

object MaxPlayerThread : Thread() {
	
	override fun run() {
		// Check if max_players is enabled
		if (Config.MaxPlayers_Enabled) {
			// Check if player count is over configured amount
			if (Bukkit.getOnlinePlayers().size > Config.MaxPlayers_Amount) {
				// Calculate timeout
				val timeout = System.currentTimeMillis() + (Config.MaxPlayers_Timeout * 60000)
				// Broadcast alert
				Messenger.broadcast(Messenger.Global.MAXPLAYERS_ALERT)
				while (true) {
					// Start Shutdown wait
					if ((Bukkit.getOnlinePlayers().size <= Config.MaxPlayers_Amount)) {
						if (System.currentTimeMillis() <= timeout) {
							// Delay Runnable
							sleep(1000)
							continue
						}
					}
					// Broadcast pre shutdown message
					if (System.currentTimeMillis() <= timeout) Messenger.broadcast(Messenger.Global.MAXPLAYERS_PRESHUTDOWN)
					else Messenger.broadcast(Messenger.Global.MAXPLAYERS_TIMEOUT)
					
					ShutdownThread.run()
					break
				}
			}
			// Cancel shutdown task call
			return
		}
		// Call shutdown task
		ShutdownThread.start()
	}
	
}