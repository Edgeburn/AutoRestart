package org.serversmc.autorestart.enums

enum class IntervalFactor {
	HOURS, DAYS, NONE;
	
	companion object {
		fun parse(name: String): IntervalFactor {
			return try {
				valueOf(name.toUpperCase())
			} catch (e: IllegalArgumentException) {
				NONE
			}
		}
	}
	
}