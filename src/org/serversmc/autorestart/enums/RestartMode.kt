package org.serversmc.autorestart.enums

import org.serversmc.autorestart.threads.*
import org.serversmc.autorestart.utils.*
import java.util.*
import kotlin.collections.ArrayList

@Suppress("unused")
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
				if (it.h < 0 || it.h > 23) Console.err("$it " + Lang.getNode("restart-mode.hour-out-of-range"))
				if (it.m < 0 || it.m > 59) Console.err("$it " + Lang.getNode("restart-mode.min-out-of-range"))
				// Add converted time to differences list
				differences.add(it.getTimeInMillis() - Calendar.getInstance().timeInMillis)
			}
			// Check if list is empty
			if (differences.isEmpty()) {
				Console.warn(Lang.getNode("restart-mode.no-timestamp"))
				return
			}
			// Get smallest difference
			val time = differences.min()!!
			// Convert milliseconds to time
			MainThread.updateTime(time.toInt() / 1000)
		}
		
	},
	NONE {
		
		override fun calculate() {
			Console.err(Lang.getNode("restart-mode.mode-not-found").replace("%m", Config.Main_RestartMode_Raw))
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