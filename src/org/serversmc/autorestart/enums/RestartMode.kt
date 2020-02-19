package org.serversmc.autorestart.enums

import org.serversmc.autorestart.core.*
import org.serversmc.autorestart.utils.*
import org.serversmc.utils.*
import java.util.*
import kotlin.collections.ArrayList

enum class RestartMode {
	INTERVAL {
		
		override fun calculate() {
			Config.Main_Modes_Interval_Factor.calculate()
		}
		
	},
	TIMESTAMP {
		
		override fun calculate() {
			// Initialize variables
			val timestamps = Config.Main_Modes_Timestamp
			val differences = ArrayList<Long>()
			// Convert timestamps to differences in milliseconds
			timestamps.forEach {
				// Check if timestamp is valid
				if (it.h < 0 || it.h > 23) Console.err("$it hour mark is out of range: 0 - 23")
				if (it.m < 0 || it.m > 59) Console.err("$it minute mark is out of range: 0 - 59")
				// Add converted time to differences list
				differences.add(it.getTimeInMillis() - Calendar.getInstance().timeInMillis)
			}
			// Check if list is empty
			if (differences.isEmpty()) {
				Console.warn("There are no accepted timestamps available! Please check config to ensure that you have followed the correct format.")
				return
			}
			// Get smallest difference
			val time = differences.min()!!
			// Convert milliseconds to time
			TimerThread.TIME = time.toInt() / 1000
		}
		
	},
	NONE {
		
		override fun calculate() {
			Console.err("Restart mode '${Config.Main_RestartMode_Raw}' in 'Main.yml:main.restart_mode' was not found! Switching to 'interval' mode!")
			INTERVAL.calculate()
		}
		
	};
	
	abstract fun calculate()
	
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