package org.serversmc.autorestart.utils

import org.bukkit.configuration.file.YamlConfiguration
import org.serversmc.autorestart.core.Main.Companion.AutoRestart
import java.io.InputStream
import java.io.InputStreamReader
import java.net.URL

object UpdateChecker {
	
	private var LATEST_BUILD: Int? = null
	var LATEST_VERSION: String? = null
	var UPDATE_FOUND: Boolean? = null
	
	private val url = URL("https://gitlab.com/dennislysenko/AutoRestart-v3/raw/master/plugin.yml")
	private val pluginYml = InputStreamReader(AutoRestart.getResource("plugin.yml") as InputStream)
	private val yaml = YamlConfiguration.loadConfiguration(pluginYml)
	
	fun checkUpdate() {
		try {
			// Fetch latest version string
			YamlConfiguration().apply {
				load(InputStreamReader(url.openStream()))
				LATEST_BUILD = getInt("build")
				LATEST_VERSION = getString("version")?.trim() ?: "null"
			}
			// Check if latest build was fetched
			if (LATEST_BUILD == null) return
			// Check if version is up to date
			val currentBuild = yaml.getInt("build", -1)
			if (currentBuild == -1) return
			UPDATE_FOUND = LATEST_BUILD!! > currentBuild
		} catch (e: Exception) {}
	}
	
}