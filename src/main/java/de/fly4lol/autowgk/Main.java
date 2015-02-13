package de.fly4lol.autowgk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import de.fly4lol.autowgk.commands.ArenaCommands;
import de.fly4lol.autowgk.commands.Commands;
import de.fly4lol.autowgk.commands.SchematicCommands;
import de.fly4lol.autowgk.commands.SignCommands;
import de.fly4lol.autowgk.fightmanager.AutoArena;
import de.fly4lol.autowgk.fightmanager.Util;
import de.fly4lol.autowgk.listener.ArenaStateChangeListener;
import de.fly4lol.autowgk.listener.BlockBreakListener;
import de.fly4lol.autowgk.listener.PlayerCommandPreprocessListener;
import de.fly4lol.autowgk.listener.PlayerInteractListener;
import de.fly4lol.autowgk.listener.PlayerQuitListener;
import de.fly4lol.messenger.Messages;
import de.fly4lol.messenger.Messenger;
import de.pro_crafting.commandframework.CommandFramework;
import de.pro_crafting.sql.api.Connection;
import de.pro_crafting.sql.api.ConnectionManager;
import de.pro_crafting.sql.api.Credentials;
import de.pro_crafting.sql.bukkit.BukkitCredentials;
import de.pro_crafting.wg.WarGear;

public class Main extends JavaPlugin{
	public HashMap<String, AutoArena> loadedArenen = new HashMap<String, AutoArena>();
	public String prefix = "§7[§6AutoWGK§7] §3";
	public String noPerms = "§4Du hast keine Berechtigung!";
	public List<Player> addSign = new ArrayList<Player>();
	public List<Player> removeSign = new ArrayList<Player>();
	private CommandFramework framework;
	public static Connection conn;
	public WarGear wg;
	private Repository repo;
	private Util util;
	private Messages messages;
	private Messenger messenger;
	private AutoArena autoArena;
	private ArenaCommands arenaCommands;
	

	
	@Override
	public void onEnable(){
		this.load();
		this.repo = new Repository(this);
		this.util = new Util(this);
		this.wg = WarGear.getPlugin(WarGear.class);
		
		this.registerListener();
		
		this.loadAutoArenas();
		this.arenaCommands = new ArenaCommands(this);
		this.framework = new CommandFramework(this);
		this.framework.registerCommands( this.arenaCommands);
		this.framework.registerCommands(new SchematicCommands(this));
		this.framework.registerCommands(new SignCommands(this));
		this.framework.registerCommands(new Commands(this));
		this.framework.registerHelp();
		this.framework.setInGameOnlyMessage(prefix+"Der Command muss von einem Spieler ausgeführt werden.");
	}

	@Override
	public void onDisable(){

	}


	private void load() {
		this.saveDefaultConfig();
		if (!this.getConfig().isSet("database")) {
			this.getConfig().set("database", new BukkitCredentials().serialize());
			this.saveConfig();
		} else {
			Credentials cred = new BukkitCredentials(this.getConfig().getConfigurationSection("database").getValues(false));
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
		
	public Repository getRepo() {
		return this.repo;
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
		List<AutoArena> arenen = this.repo.getAutoArenen();
		for(AutoArena arena : arenen){
			arena.setPlugin( this );
			arena.setWgkArena( wg.getArenaManager().getArena( arena.getName()));
			this.loadedArenen.put( arena.getName() , arena );
			
			this.getLogger().info("Arena \"" + arena.getName() + "\" geladen!");
		}
	}
	
	public ArenaCommands getArenaCommands(){
		return this.arenaCommands;
	}
}
