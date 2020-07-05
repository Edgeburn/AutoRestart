package org.serversmc.autorestart.enums

import org.serversmc.autorestart.threads.*
import org.serversmc.autorestart.utils.*

@Suppress("unused")
enum class IntervalFactor {
	HOURS {
		
		override fun calculate() {
			MainThread.updateTime((Config.Main_Modes_Interval_Value * 3600.0).toInt())
		}
		
	},
	DAYS {
		
		override fun calculate() {
			MainThread.updateTime((Config.Main_Modes_Interval_Value * 86400.0).toInt())
		}
		
	},
	NONE {
		
		override fun calculate() {
			Console.err("Interval factor '${Config.Main_Modes_Interval_Factor_Raw}' in 'Main.yml:main.interval.factor' was not found! Switching to 'hours' factor!")
			HOURS.calculate()
		}
		
	};
	
	abstract fun calculate()
	
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