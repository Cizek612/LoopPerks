package co.teamloops.perks.commands;

import co.teamloops.commons.command.CommandBuilder;
import co.teamloops.perks.LoopPerks;
import co.teamloops.perks.commands.subcommands.PerksGiveCommand;
import co.teamloops.perks.commands.subcommands.PerksRemoveCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class PerkCommand extends CommandBuilder {

    private final LoopPerks plugin;

    private final List<PerkSubCommand> subCommands;

    public PerkCommand(final LoopPerks plugin) {
        super("perks");

        this.plugin = plugin;

        this.subCommands = Arrays.asList(new PerksGiveCommand(this.plugin), new PerksRemoveCommand((this.plugin)));

        this.register();
    }

    @Override
    public boolean onCommand(final CommandSender sender, final String[] args) {

        final Player player = (Player) sender;

        if (args.length == 0) {
            plugin.getPerksMenu().openMenu(player);
            return false;
        }

        for (final PerkSubCommand subCommand : this.subCommands) {
            if (!subCommand.getName().equalsIgnoreCase(args[0])) continue;

            if (!player.hasPermission(subCommand.getPermission())) {
                this.plugin.getMessageCache().sendMessage(player, "Messages.NO-PERMISSION");
                return false;
            }

            subCommand.onCommand(sender, args);
            return true;
        }

        plugin.getPerksMenu().openMenu(player);
        return false;
    }
}