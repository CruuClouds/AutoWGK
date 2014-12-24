package de.fly4lol.autowgk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import de.fly4lol.autowgk.fightmanager.AutoArena;
import de.fly4lol.autowgk.fightmanager.Util;
import de.fly4lol.autowgk.listener.ArenaStateChangeListener;
import de.fly4lol.autowgk.listener.BlockBreakListener;
import de.fly4lol.autowgk.listener.PlayerCommandPreprocessListener;
import de.fly4lol.autowgk.listener.PlayerInteractListener;
import de.fly4lol.autowgk.listener.PlayerQuitListener;
import de.fly4lol.autowgk.messagemanager.Messages;
import de.fly4lol.autowgk.messagemanager.Messenger;
import de.fly4lol.autowgk.util.Config;
import de.fly4lol.autowgk.util.MySQLMethods;
import de.pro_crafting.commandframework.CommandFramework;
import de.pro_crafting.sql.api.Connection;
import de.pro_crafting.sql.api.ConnectionManager;
import de.pro_crafting.sql.api.Credentials;
import de.pro_crafting.wg.WarGear;

public class Main extends JavaPlugin{
	public HashMap<String, AutoArena> loadedArenen = new HashMap<String, AutoArena>();
	public String prefix = "§8[§9AutoWGK§8] §2";
	public String noPerms = "§5Du hast keine Berechtigung um dies zu tuhen!";
	private MySQLMethods sql;
	public List<Player> addSign = new ArrayList<Player>();
	public List<Player> removeSign = new ArrayList<Player>();
	private CommandFramework framework;
	private Commands commands;
	public static Connection conn;
	public WarGear wg;
	private Config config;
	private Util util;
	private Messages messages;
	private Messenger messenger;
	private AutoArena autoArena;
	

	
	@Override
	public void onEnable(){
		this.load();
		this.sql = new MySQLMethods(this);
		this.config = new Config(this);
		this.util = new Util(this);
		this.wg = WarGear.getPlugin(WarGear.class);
		
		this.registerListener();
		
		this.loadAutoArenas();
		
		this.framework = new CommandFramework(this);
		commands = new Commands(this);
		this.framework.registerCommands(commands);
		this.framework.registerHelp();
	}

	@Override
	public void onDisable(){

	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		return this.framework.handleCommand(sender, label, cmd, args);
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
		Bukkit.getPluginManager().registerEvents(new PlayerInteractListener(this), this);
		Bukkit.getPluginManager().registerEvents(new BlockBreakListener(this), this);
		Bukkit.getPluginManager().registerEvents(new ArenaStateChangeListener(this), this);
		Bukkit.getPluginManager().registerEvents(new PlayerCommandPreprocessListener(this), this);
		Bukkit.getPluginManager().registerEvents(new PlayerQuitListener(this), this);
	}
	
	public AutoArena getAutoArena(){
		return autoArena;
	}
	
	public Config getAutoWGKConfig() {
		return config;
	}
	
	public MySQLMethods getSQL() {
		return this.sql;
	}
	
	public Util getUtil(){
		return this.util;
	}
	
	public Messages getMessages(){
		return this.messages;
	}
	
	public Messenger getMessenger(){
		return this.messenger;
	}
	
	public void loadAutoArenas(){
		List<AutoArena> arenen = config.getAutoArenen();
		for(AutoArena arena : arenen){
			arena.setPlugin( this );
			arena.setWgkArena( wg.getArenaManager().getArena( arena.getName()));
			this.loadedArenen.put( arena.getName() , arena );
			
			this.getLogger().info("Arena \"" + arena.getName() + "\" geladen!");
		}
	}
}
