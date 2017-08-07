package misat11.essentials.bungee.utils;

import java.util.ArrayList;
import java.util.List;

import misat11.essentials.bungee.BungeeEssentials;
import misat11.essentials.bungee.UserConfig;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;

public class Placeholders {

	public static BaseComponent[] replace(String basestring, Object... placeholders) {
		for (Object p : placeholders) {
			if (p instanceof Placeholder[]) {
				for (Placeholder pl : (Placeholder[]) p) {
					basestring = basestring.replaceAll("%" + pl.baseString() + "%", pl.replace());
				}
			} else if (p instanceof Placeholder) {
				Placeholder pl = (Placeholder) p;
				basestring = basestring.replaceAll("%" + pl.baseString() + "%", pl.replace());
			}
		}
		basestring = basestring.replaceAll("\\&([0-9A-Za-z]+)", "§$1");

		return TextComponent.fromLegacyText(basestring);
	}

	public static Placeholder[] getPlayerPlaceholders(ProxiedPlayer player, String prefix) {
		return getPlayerPlaceholders(player.getName(), prefix);
	}

	public static Placeholder[] getPlayerPlaceholders(String playername, String prefix) {
		List<Placeholder> list = new ArrayList<Placeholder>();
		if (prefix == null || prefix == "")
			prefix = "";
		else
			prefix = prefix + "_";

		ProxiedPlayer player = ProxyServer.getInstance().getPlayer(playername);
		pl(list, prefix + "name", playername);
		pl(list, prefix + "displayname", player != null ? player.getDisplayName() : playername);
		if (player != null) {
			Server server = player.getServer();
			if (server != null) {
				pl(list, prefix + "server", server.getInfo().getName());
				pl(list, prefix + "server_motd", server.getInfo().getName());
			} else {
				emptyPl(list, prefix + "server", prefix + "server_motd");
			}
		} else {
			emptyPl(list, prefix + "server", prefix + "server_motd");
		}
		pl(list, prefix + "customname", UserConfig.getPlayer(playername).getCustomname());
		if (BungeePermsData.isAvailable()) {
			pl(list, prefix + "BungeePerms_prefix", BungeePermsData.getPrefix(playername));
			pl(list, prefix + "BungeePerms_suffix", BungeePermsData.getSuffix(playername));
			pl(list, prefix + "BungeePerms_group", BungeePermsData.getGroup(playername));
		} else {
			emptyPl(list, prefix + "BungeePerms_prefix", prefix + "BungeePerms_suffix", prefix + "BungeePerms_group");
		}
		if (LuckPermsData.isAvailable()) {
			pl(list, prefix + "LuckPerms_prefix", LuckPermsData.getPrefix(playername));
			pl(list, prefix + "LuckPerms_suffix", LuckPermsData.getSuffix(playername));
			pl(list, prefix + "LuckPerms_group", LuckPermsData.getPrimaryGroup(playername));
		} else {
			emptyPl(list, prefix + "LuckPerms_prefix", prefix + "LuckPerms_suffix", prefix + "LuckPerms_group");
		}

		return list.toArray(new Placeholder[list.size()]);
	}

	private static void pl(List<Placeholder> list, String baseString, String replace) {
		list.add(new Placeholder(baseString, replace));
	}

	private static void emptyPl(List<Placeholder> list, String... empty) {
		for (String e : empty) {
			list.add(new Placeholder(e, "none"));
		}
	}
}
