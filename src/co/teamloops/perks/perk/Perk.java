package co.teamloops.perks.perk;

import co.teamloops.perks.perk.type.PerkType;
import lombok.Data;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

@Data
public class Perk {

    private final String name;
    private PerkType perkType;
    private List<String> activateCommand;
    private List<String> deactivateCommands;

    public Perk(final String name, final FileConfiguration config) {
        this.name = name;
        this.activateCommand = config.getStringList("Perks."+name+".Activation-Commands");
        this.deactivateCommands = config.getStringList("Perks."+name+".Deactivation-Commands");
        this.perkType = PerkType.valueOf(config.getString("Perks."+name+".Type"));
    }
}
