package org.serversmc.autorestart.utils

import org.serversmc.autorestart.core.*
import org.serversmc.autorestart.enums.*
import org.serversmc.utils.*
import org.serversmc.utils.Console.err
import java.util.*
import kotlin.collections.ArrayList

object TimeManager {
	
	fun calculateTimer() {
		when (Config.Main_RestartMode) {
			RestartMode.INTERVAL -> calculateInterval()
			RestartMode.TIMESTAMP -> calculateTimestamp()
			RestartMode.NONE -> {
				err("Restart mode '${Config.Main_RestartMode_Raw}' in 'Main.yml:main.restart_mode' was not found! Switching to 'interval' mode!")
				calculateInterval()
			}
		}
	}
	
	private fun calculateInterval() {
		when (Config.Main_Modes_Interval_Factor) {
			IntervalFactor.HOURS -> TimerThread.TIME = (Config.Main_Modes_Interval_Value * 3600.0).toInt()
			IntervalFactor.DAYS -> TimerThread.TIME = (Config.Main_Modes_Interval_Value * 86400.0).toInt()
			IntervalFactor.NONE -> {
				err("Interval factor '${Config.Main_Modes_Interval_Factor_Raw}' in 'Main.yml:main.interval.factor' was not found! Switching to 'hours' factor!")
				TimerThread.TIME = (Config.Main_Modes_Interval_Value * 3600.0).toInt()
			}
		}
	}
	
	private fun calculateTimestamp() {
		// Initialize variables
		val timestamps = Config.Main_Modes_Timestamp
		val differences = ArrayList<Long>()
		// Convert timestamps to differences in milliseconds
		timestamps.forEach {
			// Check if timestamp is valid
			if (it.h < 0 || it.h > 23) err("$it hour mark is out of range: 0 - 23")
			if (it.m < 0 || it.m > 59) err("$it minute mark is out of range: 0 - 59")
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
	
}