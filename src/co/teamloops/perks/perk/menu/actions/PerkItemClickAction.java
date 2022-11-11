package co.teamloops.perks.perk.menu.actions;

import co.teamloops.commons.builders.PlaceholderReplacer;
import co.teamloops.commons.chat.MessageCache;
import co.teamloops.commons.menus.action.ClickRunnable;
import co.teamloops.perks.LoopPerks;
import co.teamloops.perks.events.PerkEquipEvent;
import co.teamloops.perks.events.PerkUnequipEvent;
import co.teamloops.perks.perk.Perk;
import co.teamloops.perks.perk.menu.PerksMenu;
import co.teamloops.perks.player.PerkPlayer;
import co.teamloops.perks.player.registry.PlayerRegistry;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public class PerkItemClickAction implements ClickRunnable {

    private final LoopPerks plugin;
    private final PlayerRegistry playerRegistry;
    private final MessageCache messageCache;
    private final Map<Integer, String> perksMap;

    public PerkItemClickAction(final LoopPerks plugin) {
        this.plugin = plugin;
        this.playerRegistry = plugin.getPlayerRegistry();
        this.messageCache = plugin.getMessageCache();
        this.perksMap = new HashMap<>();

        for(final String key : plugin.getSettingsConfig().getConfigurationSection("Menus.PERKS-MENU.Items").getKeys(false)) {
            if(!plugin.getSettingsConfig().getBoolean("Menus.PERKS-MENU.Items." + key + ".Perk-Item")) return;

            perksMap.put(plugin.getSettingsConfig().getInt("Menus.PERKS-MENU.Items." + key + ".Slot"), plugin.getSettingsConfig().getString("Menus.PERKS-MENU.Items." + key + ".Perk"));

        }
    }

    @Override
    public void click(final InventoryClickEvent inventoryClickEvent) {
        final Player player = (Player) inventoryClickEvent.getWhoClicked();
        final PerkPlayer perkPlayer = playerRegistry.getRegistry().get(inventoryClickEvent.getWhoClicked().getUniqueId());
        final Perk perk = plugin.getPerksRegistry().getRegistry().get(perksMap.get(inventoryClickEvent.getRawSlot()));
        final PlaceholderReplacer placeholderReplacer = new PlaceholderReplacer()
                .addPlaceholder("%perk%", perk.getName())
                .addPlaceholder("%limit%", Integer.toString(plugin.getSettingsConfig().getInt("Settings.Max-Perks")));


        if(!perksMap.containsKey(inventoryClickEvent.getRawSlot())) return;

        if(!perkPlayer.getUnlockedPerks().contains(perk.getName())) {
            messageCache.sendMessage(player, "Messages.NOT-UNLOCKED");
            return;
        }

        if(perkPlayer.getEquippedPerks().contains(perk.getName())) {
            perkPlayer.getEquippedPerks().remove(perk.getName());
            messageCache.sendMessage(player, placeholderReplacer, "Messages.PERK-UNEQUIPPED");
            final PerkUnequipEvent perkUnequipEvent = new PerkUnequipEvent(player, perk);
            Bukkit.getPluginManager().callEvent(perkUnequipEvent);
        } else {
            if(perkPlayer.getEquippedPerks().size()>=plugin.getSettingsConfig().getInt("Settings.Max-Perks")) {
                messageCache.sendMessage(player, placeholderReplacer, "Messages.LIMIT-REACHED");
                return;
            }
            perkPlayer.getEquippedPerks().add(perk.getName());
            messageCache.sendMessage(player, placeholderReplacer, "Messages.PERK-EQUIPPED");
            final PerkEquipEvent perkEquipEvent = new PerkEquipEvent(player, perk);
            Bukkit.getPluginManager().callEvent(perkEquipEvent);
        }

        plugin.getPerksMenu().openMenu(player);

    }
}
