package com.smp10a1.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class Main extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        
        // Cập nhật bảng mỗi giây (20 ticks)
        Bukkit.getScheduler().runTaskTimer(this, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                updateScoreboard(player);
            }
        }, 0L, 20L);
        
        getLogger().info("Plugin 10A1 SMP Scoreboard da bat!");
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        updateScoreboard(event.getPlayer());
    }

    private void updateScoreboard(Player player) {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getNewScoreboard();
        
        Objective obj = board.registerNewObjective("10A1SMP", "dummy", "§c§l10A1 SMP");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);

        int level = player.getLevel();
        int deaths = player.getStatistic(Statistic.DEATHS);
        long days = player.getWorld().getFullTime() / 24000;

        obj.getScore("§fTên: §a" + player.getName()).setScore(4);
        obj.getScore("§fKinh nghiệm: §e" + level).setScore(3);
        obj.getScore("§fNgày: §b" + days).setScore(2);
        obj.getScore("§fDie: §c" + deaths).setScore(1);

        player.setScoreboard(board);
    }
}
