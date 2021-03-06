package org.serversmc.autorestart.enums

import org.serversmc.autorestart.core.*
import org.serversmc.autorestart.utils.*

@Suppress("unused")
enum class IntervalFactor {
	HOURS {
		
		override fun calculate() {
			TimerThread.TIME = (Config.Main_Modes_Interval_Value * 3600.0).toInt()
		}
		
	},
	DAYS {
		
		override fun calculate() {
			TimerThread.TIME = (Config.Main_Modes_Interval_Value * 86400.0).toInt()
		}
		
	},
	NONE {
		
		override fun calculate() {
			Console.err(Lang.getNode("interval-factor.error").replace("%f", Config.Main_Modes_Interval_Factor_Raw))
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