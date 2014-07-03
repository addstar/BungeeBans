package au.com.addstar.bungeebans;

import redis.clients.jedis.Jedis;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

public class BungeeBans extends Plugin {
	public static ProxyServer proxy;
	private Jedis jedis;
	private boolean Debug = true;
	
	@Override
	public void onEnable() {
		getProxy().getPluginManager().registerListener(this, new BungeeBansListener(this));  
		getProxy().registerChannel("BungeeBans");
				
		jedis = new Jedis("localhost");
		if (jedis != null) {
			jedis.connect();
		}
		
		if ((jedis != null) && (jedis.isConnected())) {
			LogMsg("Successfully connected to Redis server!");
		} else {
			ErrorMsg("Unable to connect to Redis server!");
		}
	}
	
	public boolean isDebug() {
		return Debug;
	}

	public void setDebug(boolean debug) {
		Debug = debug;
	}

	public void LogMsg(String msg) {
		getLogger().info(msg);
	}

	public void WarnMsg(String msg) {
		getLogger().warning(msg);
	}

	public void ErrorMsg(String msg) {
		getLogger().severe(msg);
	}

	public void DebugMsg(String msg) {
		if (isDebug()) {
			getLogger().info(msg);
		}
	}
	
	public String getRString(String key) {
		String data = jedis.get(key);
		if ((data != null) && (data.length() > 1)) {
			data = data.substring(1, data.length()-1);  // Trim first + last char (they're always the " char)
		}
		return data;
	}
	
	public boolean setRString(String key, String value) {
		jedis.set(key, value);
		return true;
	}
}