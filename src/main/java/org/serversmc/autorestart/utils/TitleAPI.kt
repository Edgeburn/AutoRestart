package org.serversmc.autorestart.utils

import org.bukkit.*
import org.bukkit.entity.*

object TitleAPI {
	
	private fun getNMSClass(name: String): Class<*>? {
		val version: String = Bukkit.getServer().javaClass.`package`.name.split(".")[3]
		return Class.forName("net.minecraft.server.$version.$name")
	}
	
	private fun sendPacket(player: Player, packet: Any) {
		val handle = player.javaClass.getMethod("getHandle").invoke(player)
		val playerConnection = handle.javaClass.getField("playerConnection").get(handle)
		playerConnection.javaClass.getMethod("sendPacket", getNMSClass("Packet")).invoke(playerConnection, packet)
	}
	
	fun sendTitle(player: Player, fadeIn: Int, stay: Int, fadeOut: Int, title: String?, subtitle: String?) {
		if (title != null) {
			val text = ChatColor.translateAlternateColorCodes('&', title)
			var e = getNMSClass("PacketPlayOutTitle")!!.declaredClasses[0].getField("TIMES").get(null)
			var chatTitle = getNMSClass("IChatBaseComponent")!!.declaredClasses[0].getMethod("a", String::class.java).invoke(null, "{\"text\":\"$text\"}")
			var subtitleConstructor = getNMSClass("PacketPlayOutTitle")!!.getConstructor(
				getNMSClass("PacketPlayOutTitle")!!.declaredClasses[0],
				getNMSClass("IChatBaseComponent"),
				Integer.TYPE,
				Integer.TYPE,
				Integer.TYPE
			)
			var titlePacket: Any = subtitleConstructor.newInstance(e, chatTitle, fadeIn, stay, fadeOut)
			sendPacket(player, titlePacket)
			e = getNMSClass("PacketPlayOutTitle")!!.declaredClasses[0].getField("TITLE").get(null)
			chatTitle = getNMSClass("IChatBaseComponent")!!.declaredClasses[0].getMethod("a", String::class.java).invoke(null, "{\"text\":\"$text\"}")
			subtitleConstructor = getNMSClass("PacketPlayOutTitle")!!.getConstructor(getNMSClass("PacketPlayOutTitle")!!.declaredClasses[0], getNMSClass("IChatBaseComponent"))
			titlePacket = subtitleConstructor.newInstance(e, chatTitle)
			sendPacket(player, titlePacket)
		}
		if (subtitle != null) {
			val text = ChatColor.translateAlternateColorCodes('&', subtitle)
			var e = getNMSClass("PacketPlayOutTitle")!!.declaredClasses[0].getField("TIMES").get(null)
			var chatSubtitle = getNMSClass("IChatBaseComponent")!!.declaredClasses[0].getMethod("a", String::class.java).invoke(null, "{\"text\":\"$title\"}")
			var subtitleConstructor = getNMSClass("PacketPlayOutTitle")!!.getConstructor(
				getNMSClass("PacketPlayOutTitle")!!.declaredClasses[0],
				getNMSClass("IChatBaseComponent"),
				Integer.TYPE,
				Integer.TYPE,
				Integer.TYPE
			)
			var subtitlePacket = subtitleConstructor.newInstance(e, chatSubtitle, fadeIn, stay, fadeOut)
			sendPacket(player, subtitlePacket)
			e = getNMSClass("PacketPlayOutTitle")!!.declaredClasses[0].getField("SUBTITLE").get(null)
			chatSubtitle = getNMSClass("IChatBaseComponent")!!.declaredClasses[0].getMethod("a", String::class.java).invoke(null, "{\"text\":\"$text\"}")
			subtitleConstructor = getNMSClass("PacketPlayOutTitle")!!.getConstructor(
				getNMSClass("PacketPlayOutTitle")!!.declaredClasses[0],
				getNMSClass("IChatBaseComponent"),
				Integer.TYPE,
				Integer.TYPE,
				Integer.TYPE
			)
			subtitlePacket = subtitleConstructor.newInstance(e, chatSubtitle, fadeIn, stay, fadeOut)
			sendPacket(player, subtitlePacket)
		}
	}
	
}