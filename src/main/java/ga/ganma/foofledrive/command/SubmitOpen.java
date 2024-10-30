package ga.ganma.foofledrive.command;

import ga.ganma.foofledrive.FileRelationUtils;
import ga.ganma.foofledrive.Listener.GetEvent;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class SubmitOpen {
    Plugin pl;

    public SubmitOpen(Plugin pl, Player p) {
        this.pl = pl;
        p.openInventory(FileRelationUtils.readFile(p).getInv());
        GetEvent.isInventoryOpen.put(p, true);
    }
}
