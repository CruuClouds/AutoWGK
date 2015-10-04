package de.fly4lol.autowgk.arena;

import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.transform.AffineTransform;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.registry.WorldData;
import de.fly4lol.autowgk.schematic.Schematic;
import org.bukkit.Location;
import org.primesoft.asyncworldedit.api.utils.IFuncParamEx;
import org.primesoft.asyncworldedit.api.worldedit.ICancelabeEditSession;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class PasteAction implements IFuncParamEx<Integer, ICancelabeEditSession, MaxChangedBlocksException> {
    private Schematic schematic;
    private File schematicFile;
    private Vector to;
    private BukkitWorld world;
    private Direction direction;

    public PasteAction(Schematic schematic, File schematicFile, Location to, Direction direction) {
        this.schematic = schematic;
        this.to = BukkitUtil.toVector(to);
        this.world = new BukkitWorld(to.getWorld());
        this.schematicFile = schematicFile;
        this.direction = direction;
    }

    public Integer execute(ICancelabeEditSession editSession) throws MaxChangedBlocksException {
        try {
            FileInputStream stream = new FileInputStream(schematicFile);
            ClipboardReader reader = ClipboardFormat.SCHEMATIC.getReader(stream);

            WorldData worldData = world.getWorldData();
            Clipboard clipboard = reader.read(worldData);
            ClipboardHolder holder  = new ClipboardHolder(clipboard, worldData);

            if (direction != schematic.getDirection()) {
                AffineTransform transform = new AffineTransform();
                transform = transform.rotateY(180);
                holder.setTransform(transform);
            }
            final Operation operation = holder
                    .createPaste(editSession, worldData)
                    .to(to)
                    .ignoreAirBlocks(false)
                    .build();
            Operations.complete(operation);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (WorldEditException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
