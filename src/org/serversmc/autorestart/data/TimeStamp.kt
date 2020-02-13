package org.serversmc.autorestart.data

import org.serversmc.autorestart.utils.Console.err
import java.lang.Integer.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.MutableList
import kotlin.collections.forEach


data class TimeStamp(val h: Int, val m: Int) {
	
	override fun toString(): String {
		return "$h:$m"
	}
	
	fun getTimeInMillis(): Long {
		var time = Calendar.getInstance().run {
			set(Calendar.HOUR_OF_DAY, h)
			set(Calendar.MINUTE, m)
			set(Calendar.SECOND, 0)
			timeInMillis
		}
		if (time - Calendar.getInstance().timeInMillis < 0) time += 86400000L
		return time
	}
	
}

object TimeStampManager {
	
	fun parseStringList(list: MutableList<String>): MutableList<TimeStamp> {
		return ArrayList<TimeStamp>().apply {
			list.forEach {
				try {
					val h = parseInt(it.split(":")[0])
					val m = parseInt(it.split(":")[1])
					add(TimeStamp(h, m))
				} catch (e: Exception) {
					err("Could not read \"$it\" please check Main.yml:main.modes.interval")
				}
			}
		}
	}
	
}