package org.serversmc.autorestart.utils

import org.bukkit.configuration.file.*
import org.serversmc.autorestart.core.*
import java.io.*

object Lang {
	
	private lateinit var lang: YamlConfiguration
	
	fun init() {
		lang = YamlConfiguration.loadConfiguration(InputStreamReader(PLUGIN.getResource("lang/EN.yml")!!))
	}
	
	fun getNode(node: String) = lang.getString(node)!!
	
}