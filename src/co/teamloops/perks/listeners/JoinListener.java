package co.teamloops.perks.listeners;


import co.teamloops.commons.abstracts.LoopListener;
import co.teamloops.perks.LoopPerks;
import co.teamloops.perks.player.PerkPlayer;
import co.teamloops.perks.player.registry.PlayerRegistry;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener extends LoopListener<LoopPerks> {

    private final LoopPerks plugin;
    private final PlayerRegistry registry;

    public JoinListener(final LoopPerks plugin) {
        super(plugin);

        this.plugin = plugin;
        this.registry = plugin.getPlayerRegistry();

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onJoin(final PlayerJoinEvent event) {

        final Player player = event.getPlayer();

        if (this.registry.containsKey(player.getUniqueId())) return;

        this.registry.register(
                player.getUniqueId(),
                new PerkPlayer(player.getUniqueId())
        );
    }
}

