package co.teamloops.perks.listeners;

import co.teamloops.commons.abstracts.LoopListener;
import co.teamloops.commons.builders.PlaceholderReplacer;
import co.teamloops.perks.LoopPerks;
import co.teamloops.perks.events.PerkEquipEvent;
import co.teamloops.perks.events.PerkUnequipEvent;
import co.teamloops.perks.perk.type.PerkType;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;

@SuppressWarnings("unused")
public class PlayerListener extends LoopListener<LoopPerks> {

    public PlayerListener(final LoopPerks plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPerkEquipEvent(final PerkEquipEvent event) {
        if(event.getPerk().getPerkType()!=PerkType.COMMAND) return;
        final PlaceholderReplacer placeholderReplacer = new PlaceholderReplacer()
                .addPlaceholder("%player%", event.getPlayer().getName());

        for(final String command : event.getPerk().getActivateCommand()) {
            Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), placeholderReplacer.parse(command));
        }
    }

    @EventHandler
    public void onPerkUnequipEvent(final PerkUnequipEvent event) {
        if(event.getPerk().getPerkType()!=PerkType.COMMAND) return;
        final PlaceholderReplacer placeholderReplacer = new PlaceholderReplacer()
                .addPlaceholder("%player%", event.getPlayer().getName());

        for(final String command : event.getPerk().getDeactivateCommands()) {
            Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), placeholderReplacer.parse(command));
        }
    }


}
