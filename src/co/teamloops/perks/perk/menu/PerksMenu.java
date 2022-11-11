package co.teamloops.perks.perk.menu;

import co.teamloops.commons.builders.PlaceholderReplacer;
import co.teamloops.commons.builders.improved.ItemUtils;
import co.teamloops.commons.menus.MenuBuilder;
import co.teamloops.commons.menus.MenuSimple;
import co.teamloops.commons.menus.action.ClickAction;
import co.teamloops.commons.utils.ColorUtil;
import co.teamloops.perks.LoopPerks;
import co.teamloops.perks.perk.Perk;
import co.teamloops.perks.perk.menu.actions.PerkItemClickAction;
import co.teamloops.perks.player.PerkPlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class PerksMenu {

    private final LoopPerks plugin;
    private final ClickAction action;
    public PerksMenu(final LoopPerks plugin) {
        this.plugin = plugin;
        this.action = new ClickAction(new PerkItemClickAction(plugin));
    }
    

    public void openMenu(final Player player) {
        final PlaceholderReplacer loreReplacer = new PlaceholderReplacer();
        final UUID uuid = player.getUniqueId();
        final PerkPlayer perkPlayer = plugin.getPlayerRegistry().getRegistry().get(uuid);
        final FileConfiguration settingsConfig = plugin.getSettingsConfig();

        final MenuSimple menu = MenuBuilder.builder().buildSimple(settingsConfig.getString("Menus.PERKS-MENU.Title"), settingsConfig.getInt("Menus.PERKS-MENU.Size"));

        for (final String key : settingsConfig.getConfigurationSection("Menus.PERKS-MENU.Items").getKeys(false)) {

            final int slot = settingsConfig.getInt("Menus.PERKS-MENU.Items." + key + ".Slot");
            final Perk perk = plugin.getPerkRegistry().getRegistry().get(settingsConfig.getString("Menus.PERKS-MENU.Items." + key + ".Perk"));
            loreReplacer.addPlaceholder("%status%", this.getStatus(perkPlayer.getUnlockedPerks().contains(perk.getName()), perkPlayer, perk));

            menu.setItemAt(slot, ItemUtils.getItem(settingsConfig, "Menus.PERKS-MENU.Items." + key).parse(loreReplacer));

            if(settingsConfig.getBoolean("Menus.PERKS-MENU.Items." + key + ".Perk-Item")) {
                menu.addClickAction(slot, action);
            }
        }

        for (final String key : settingsConfig.getConfigurationSection("Menus.PERKS-MENU.Borders").getKeys(false)) {
            final ItemStack border = ItemUtils.getItem(settingsConfig, "Menus.PERKS-MENU.Borders." + key).parse();
            for (final int i : settingsConfig.getIntegerList("Menus.PERKS-MENU.Borders." + key + ".Slots")) menu.setItemAt(i, border);
        }

        menu.setClickable(false);
        player.openInventory(menu.getGUI());
    }

    private String getStatus(final boolean status, final PerkPlayer perkPlayer, final Perk perk) {

        if(status && perkPlayer.getEquippedPerks().contains(perk.getName())) return ColorUtil.colorize("&a&lEQUIPPED");
        if (status) return ColorUtil.colorize("&a&lUNLOCKED");
        return ColorUtil.colorize("&c&lLOCKED");

    }

}
