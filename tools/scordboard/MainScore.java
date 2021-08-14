package fr.tetemh.shop.tools.scordboard;

import java.util.HashMap;
import java.util.Map;

import fr.tetemh.shop.Main;
import fr.tetemh.shop.profile.PlayerProfile;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class MainScore implements Listener {

    public static Map<Player, ScoreboardSign> boards = new HashMap<>();

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();

        ScoreboardSign scoreboard = new ScoreboardSign(player, "§e§lPivipi");
        scoreboard.create();
        scoreboard.setLine(0, "§d");
        scoreboard.setLine(1, "§3Compte : §b" + player.getName());
        scoreboard.setLine(2, "§f");
        scoreboard.setLine(3, "§ePi : " + PlayerProfile.getMoney(player));
        scoreboard.setLine(4, "§9");


        boards.put(player, scoreboard);

        Bukkit.getScheduler().runTaskTimer(Main.getInstance(), new BukkitRunnable() {

            @Override
            public void run() {
                scoreboard.setLine(0, "§d");
                scoreboard.setLine(1, "§3Compte : §b" + player.getName());
                scoreboard.setLine(2, "§f");
                scoreboard.setLine(3, "§ePi : " + PlayerProfile.getMoney(player));
                scoreboard.setLine(4, "§9");
            }
        }, 0, 100);

    }

}
