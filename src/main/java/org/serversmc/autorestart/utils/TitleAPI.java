package org.serversmc.autorestart.utils;

import org.bukkit.entity.Player;

public class TitleAPI {
	public static void sendTitle(Player player, int fadeIn, int stay, int fadeOut, String title, String subtitle) {
		player.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
	}
}
