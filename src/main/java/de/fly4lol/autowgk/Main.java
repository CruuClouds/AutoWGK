package de.fly4lol.autowgk;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import de.fly4lol.autowgk.listener.PlayerInteractListener;
import de.fly4lol.autowgk.util.MySQLMethods;
import de.pro_crafting.commandframework.CommandFramework;
import de.pro_crafting.sql.api.Connection;
import de.pro_crafting.sql.api.ConnectionManager;
import de.pro_crafting.sql.api.Credentials;

public class Main extends JavaPlugin{
	
	public String prefix = "§8[§9AutoWGK§8] §2";
	private MySQLMethods sql;
	public List<Player> addSign = new ArrayList<Player>();
	public List<Player> removeSign = new ArrayList<Player>();
	private CommandFramework framework;
	private Commands commands;
	private MySQLMethods mysql;
	public static Connection conn;
	
	@Override
	public void onEnable(){
	this.registerListener();
	mysql = new MySQLMethods(this);
	this.load();
	this.framework = new CommandFramework(this);
	commands = new Commands(this, sql);
	this.framework.registerCommands(commands);
	this.framework.registerHelp();
		
	}
	
	@Override
	public void onDisable(){
		
	}
	
	public void MySQL(MySQLMethods sql) {
		this.sql = sql;
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
	
	private void registerListener(){
		Bukkit.getPluginManager().registerEvents(new PlayerInteractListener(this , sql), this);
	}

}
