package de.fly4lol.autowgk;

import org.bukkit.plugin.java.JavaPlugin;

import de.pro_crafting.sql.api.Connection;
import de.pro_crafting.sql.api.ConnectionManager;
import de.pro_crafting.sql.api.Credentials;

public class Main extends JavaPlugin{
	
	public static Connection conn;
	
	@Override
	public void onEnable(){
	
	this.load();
		
	}
	
	@Override
	public void onDisable(){
		
	}
	
	
	
	private void load() {
		this.saveDefaultConfig();
		if (!this.getConfig().isSet("database")) {
			this.getConfig().set("database", new Credentials().serialize());
			this.saveConfig();
		} else {
			Credentials cred = new Credentials(this.getConfig().getConfigurationSection("database").getValues(false));
			ConnectionManager.getInstance().addConnectionData(cred);
			conn = ConnectionManager.getInstance().get(cred.getName());
		}
	}

}
