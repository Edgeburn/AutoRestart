package org.serversmc.autorestart.utils

import org.serversmc.autorestart.core.TimerThread.TIME
import java.util.*
import kotlin.collections.ArrayList

object TimeManager {
	
	fun calculateInterval() {
		TIME = (Config.getDouble("main.modes.interval") * 3600.0).toInt()
	}
	
	fun calculateTimestamp() {
		// Initialize variables
		val timestamps = Config.getTimeStampList("main.modes.timestamp")
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
		TIME = time.toInt() / 1000
	}
	
}