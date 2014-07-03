package au.com.addstar.bungeebans;

import java.util.UUID;

import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class BungeeBansListener implements Listener {
	private BungeeBans plugin;
	
	public BungeeBansListener(BungeeBans p) {
		plugin = p;
	}
	
	@EventHandler
	public void onLogin(LoginEvent event) {
		if (event.isCancelled()) return;
		
		PendingConnection conn = event.getConnection();
		if (conn != null) {
			UUID uuid = conn.getUniqueId();
			String id = "";
			if (uuid != null) {
				id = uuid.toString().replace("-", "");
			}
			
			plugin.DebugMsg("Player connected: " +
				conn.getName() + "/" +
				id + "/" +
				conn.getAddress().getHostString() + "/" +
				conn.getVersion()
			);
			
			String banname = plugin.getRString("storage.ban." + id + ".name");
			if ((banname != null) && (!banname.isEmpty())) {
				String banmsg = plugin.getRString("storage.ban." + id + ".msg");
				if ((banmsg == null) || (banmsg.isEmpty())) {
					banmsg = "You have been banned.";
				}

				plugin.LogMsg("Disconnected " + conn.getName() + " (" + id + " / " + conn.getAddress().getHostString() + "): " + banmsg);
				event.setCancelled(true);
				event.setCancelReason(banmsg);
			} else {
				plugin.DebugMsg("Ban record not found!");
			}
		} else {
			plugin.WarnMsg("PendingConnection is NULL!");
		}
	}
}