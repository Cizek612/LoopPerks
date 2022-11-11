package co.teamloops.perks.commands.subcommands;

import co.teamloops.commons.builders.PlaceholderReplacer;
import co.teamloops.commons.chat.MessageCache;
import co.teamloops.perks.LoopPerks;
import co.teamloops.perks.commands.PerkSubCommand;
import co.teamloops.perks.events.PerkUnequipEvent;
import co.teamloops.perks.perk.registry.PerkRegistry;
import co.teamloops.perks.player.registry.PlayerRegistry;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PerksRemoveCommand extends PerkSubCommand {

    private final MessageCache messageCache;
    private final PerkRegistry perkRegistry;
    private final PlayerRegistry playerRegistry;

    public PerksRemoveCommand(final LoopPerks plugin) {
        super("remove", "perks.remove", false);

        this.messageCache = plugin.getMessageCache();
        this.perkRegistry = plugin.getPerkRegistry();
        this.playerRegistry = plugin.getPlayerRegistry();
    }

    @Override
    public void execute(final CommandSender sender, final String[] args) {

        if(args.length<3) {
            messageCache.sendMessage((Player) sender, "Messages.HELP");
            return;
        }
        final Player target = Bukkit.getPlayer(args[1]);


        if(target == null) {
            messageCache.sendMessage((Player) sender, "Messages.INVALID-ARGUMENTS");
            return;
        }

        if(!perkRegistry.getRegistry().containsKey(args[2])) {
            messageCache.sendMessage((Player) sender, "Messages.INVALID-ARGUMENTS");
            return;
        }

        final PlaceholderReplacer placeholderReplacer = new PlaceholderReplacer();
        placeholderReplacer.addPlaceholder("%player%", target.getName());
        placeholderReplacer.addPlaceholder("%perk%", args[2]);

        playerRegistry.getRegistry().get(target.getUniqueId()).getUnlockedPerks().remove(args[2]);
        messageCache.sendMessage(target, placeholderReplacer,"Messages.PERK-REMOVED");
        messageCache.sendMessage((Player) sender, placeholderReplacer,"Messages.PERK-REMOVED-ADMIN");

        playerRegistry.getRegistry().get(target.getUniqueId()).getEquippedPerks().remove(args[2]);
        final PerkUnequipEvent perkUnequipEvent = new PerkUnequipEvent(target, perkRegistry.getRegistry().get(args[2]));
        Bukkit.getPluginManager().callEvent(perkUnequipEvent);

    }
}