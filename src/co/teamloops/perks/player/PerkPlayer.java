package co.teamloops.perks.player;


import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class PerkPlayer {

    private final UUID uuid;
    private List<String> unlockedPerks;
    private List<String> equippedPerks;

    public PerkPlayer(final UUID uuid) {
        this.uuid = uuid;
        this.unlockedPerks = new ArrayList<>();
        this.equippedPerks = new ArrayList<>();
    }
}
