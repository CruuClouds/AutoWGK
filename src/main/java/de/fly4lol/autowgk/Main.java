package de.fly4lol.autowgk;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import de.fly4lol.autowgk.commands.ArenaCommands;
import de.fly4lol.autowgk.commands.Commands;
import de.fly4lol.autowgk.commands.SchematicCommands;
import de.fly4lol.autowgk.commands.SignCommands;
import de.fly4lol.autowgk.fightmanager.AutoArena;
import de.fly4lol.autowgk.fightmanager.Util;
import de.fly4lol.autowgk.listener.ArenaStateChangeListener;
import de.fly4lol.autowgk.listener.PlayerCommandPreprocessListener;
import de.fly4lol.autowgk.listener.PlayerQuitListener;
import de.fly4lol.autowgk.sign.SignManager;
import de.fly4lol.messenger.Messages;
import de.fly4lol.messenger.Messenger;
import de.pro_crafting.commandframework.CommandFramework;

public class Main extends JavaPlugin{
	public HashMap<String, AutoArena> loadedArenen = new HashMap<String, AutoArena>();
	public String prefix = "§7[§6AutoWGK§7] §3";
	public String noPerms = "§4Du hast keine Berechtigung!";
	private CommandFramework framework;
	private Repository repo;
	private Util util;
	private Messages messages;
	private Messenger messenger;
	private ArenaCommands arenaCommands;
	private SignManager signManager;

	
	@Override
	public void onEnable(){
		this.load();
		this.repo = new Repository(this);
		this.util = new Util(this);
		this.signManager = new SignManager(this);
		
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
	}

	private void registerListener(){
		Bukkit.getPluginManager().registerEvents(this.signManager, this);
		Bukkit.getPluginManager().registerEvents(new ArenaStateChangeListener(this), this);
		Bukkit.getPluginManager().registerEvents(new PlayerCommandPreprocessListener(this), this);
		Bukkit.getPluginManager().registerEvents(new PlayerQuitListener(this), this);
	}
		
	public SignManager getSignManager() {
		return signManager;
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
			this.loadedArenen.put( arena.getName() , arena );
			this.getLogger().info("Arena \"" + arena.getName() + "\" geladen!");
		}
	}
	
	public ArenaCommands getArenaCommands(){
		return this.arenaCommands;
	}
}
