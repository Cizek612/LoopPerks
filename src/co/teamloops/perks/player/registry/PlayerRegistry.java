package co.teamloops.perks.player.registry;

import co.teamloops.commons.patterns.Registry;
import co.teamloops.perks.player.PerkPlayer;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerRegistry implements Registry<UUID, PerkPlayer> {

    @Getter
    private final Map<UUID, PerkPlayer> registry = new HashMap<>();


}
