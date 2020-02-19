package org.serversmc.autorestart.enums

enum class RestartMode {
	INTERVAL, TIMESTAMP, NONE;
	
	companion object {
		fun parse(name: String): RestartMode {
			return try {
				valueOf(name.toUpperCase())
			} catch (e: IllegalArgumentException) {
				NONE
			}
		}
	}
	
}