package io.github.tux2603.manhunt;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Team;

public class UpdateRunnable extends BukkitRunnable {

    Team hunted;
    Team hunters;

    public UpdateRunnable(Team hunted, Team hunters) {
        this.hunted = hunted;
        this.hunters = hunters;
    }

    @Override
    public void run() {
        if (hunted.getSize() == 1) {
            Player huntedPlayer = Bukkit.getPlayer(hunted.getEntries().iterator().next());

            if (huntedPlayer == null) {
                Bukkit.broadcastMessage("The hunted player left the server or something... get back here!");
                return;
            }

            Location huntedLocation = huntedPlayer.getLocation();
            huntedPlayer.setCompassTarget(huntedLocation);

            for (String hunterName : hunters.getEntries()) {

                Player p = Bukkit.getPlayer(hunterName);

                if (p != null) {
                    p.setCompassTarget(huntedLocation);
                }
            }
        }

        else if (hunters.getSize() > 1) {
            Bukkit.broadcastMessage("WARNING! Too many people on Hunted team!");
        }
    }
}