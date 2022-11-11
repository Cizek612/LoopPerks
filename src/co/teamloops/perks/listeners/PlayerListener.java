package co.teamloops.perks.listeners;

import co.teamloops.commons.abstracts.LoopListener;
import co.teamloops.perks.LoopPerks;
import co.teamloops.perks.events.PerkEquipEvent;
import co.teamloops.perks.events.PerkUnequipEvent;
import co.teamloops.perks.perk.Perk;
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
        final Perk perk = event.getPerk();

        if(perk.getPerkType()!=PerkType.COMMAND) return;

        for(final String command : perk.getActivateCommand()) {
            Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), command.replace("%player%", event.getPlayer().getName()));
        }
    }

    @EventHandler
    public void onPerkUnequipEvent(final PerkUnequipEvent event) {
        final Perk perk = event.getPerk();

        if(perk.getPerkType()!=PerkType.COMMAND) return;

        for(final String command : perk.getDeactivateCommands()) {
            Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), command.replace("%player%", event.getPlayer().getName()));
        }
    }


}
