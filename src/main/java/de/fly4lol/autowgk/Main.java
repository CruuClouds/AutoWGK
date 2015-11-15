package de.fly4lol.autowgk;

import de.fly4lol.autowgk.listener.PlayerArenaChangeListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import de.fly4lol.autowgk.arena.AutoArena;
import de.fly4lol.autowgk.arena.AutoArenaManager;
import de.fly4lol.autowgk.commands.ArenaCommands;
import de.fly4lol.autowgk.commands.Commands;
import de.fly4lol.autowgk.commands.SchematicCommands;
import de.fly4lol.autowgk.commands.SignCommands;
import de.fly4lol.autowgk.listener.ArenaStateChangeListener;
import de.fly4lol.autowgk.listener.PlayerCommandPreprocessListener;
import de.fly4lol.autowgk.sign.SignManager;
import de.fly4lol.messenger.Messenger;
import de.pro_crafting.commandframework.CommandFramework;

public class Main extends JavaPlugin{
	private AutoArenaManager arenenManager;
	public String prefix = "§7[§6AutoWGK§7] §3";
	public String noPerms = "§4Du hast keine Berechtigung!";
	private CommandFramework framework;
	private Repository repo;
	private ArenaCommands arenaCommands;
	private SignManager signManager;
	private static Main instance;

	
	@Override
	public void onEnable(){
		this.load();
		this.repo = new Repository(this);
		this.signManager = new SignManager(this);
		this.arenenManager = new AutoArenaManager(this);
		
		this.startRepeatingTask();
		
		Main.instance = this;
		
		this.registerListener();
		
		Messenger.getInstance().setNoLastMessage("§2Du hast noch keine Nachrichten bekommen!");
		
		registerCommands();
	}
	
	private void registerCommands() {
		this.arenaCommands = new ArenaCommands(this);
		this.framework = new CommandFramework(this);
		this.framework.registerCommands( this.arenaCommands);
		this.framework.registerCommands(new SchematicCommands(this));
		this.framework.registerCommands(new SignCommands(this));
		this.framework.registerCommands(new Commands(this));
		this.framework.registerHelp();
		this.framework.setInGameOnlyMessage(prefix+"Der Command muss von einem Spieler ausgeführt werden.");
	}
	
	private void load() {
		this.saveDefaultConfig();
	}
	
	public static Main getInstance(){
		return instance;
	}

	private void registerListener(){
		Bukkit.getPluginManager().registerEvents(this.signManager, this);
		Bukkit.getPluginManager().registerEvents(new ArenaStateChangeListener(this), this);
		Bukkit.getPluginManager().registerEvents(new PlayerCommandPreprocessListener(this), this);
		Bukkit.getPluginManager().registerEvents(new PlayerArenaChangeListener(this), this);
	}
		
	public SignManager getSignManager() {
		return signManager;
	}

	public Repository getRepo() {
		return this.repo;
	}
	
	public ArenaCommands getArenaCommands(){
		return this.arenaCommands;
	}
	
	public AutoArenaManager getArenenManager() {
		return arenenManager;
	}
	
	public void startRepeatingTask(){
		
		this.getServer().getScheduler().scheduleSyncRepeatingTask( this , new Runnable() {
			
			public void run() {
				for(AutoArena arena : Main.getInstance().getArenenManager().getArenas()){
					Main.getInstance().getArenenManager().removeAFKPlayers( arena);
				}
				
			}
		}, 20*60, 20*60);
	}
}
