package co.teamloops.perks.commands;

import co.teamloops.commons.command.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class PerkSubCommand extends SubCommand {

    private final boolean playerOnly;

    public PerkSubCommand(final String name, final String permission, final boolean playerOnly) {
        super(name, permission);
        this.playerOnly = playerOnly;
    }

    public void onCommand(final CommandSender sender, final String[] strings) {
        if (!(sender instanceof Player) && this.playerOnly) {
            System.out.println("[Perks] This commands is player only.");
            return;
        }
        this.execute(sender, strings);
    }

    public abstract void execute(final CommandSender var1, final String[] var2);
}
