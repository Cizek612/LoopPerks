package co.teamloops.perks.perk.registry;

import co.teamloops.commons.patterns.Registry;
import co.teamloops.perks.perk.Perk;
import lombok.Getter;

import java.util.HashMap;

public class PerkRegistry implements Registry<String, Perk> {

    @Getter private final HashMap<String, Perk> registry = new HashMap<>();
}
