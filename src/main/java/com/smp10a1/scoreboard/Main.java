package com.smp10a1.scoreboard;

import io.papermc.paper.scoreboard.numbers.NumberFormat;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class Main extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        // Đăng ký sự kiện lắng nghe người chơi vào server
        getServer().getPluginManager().registerEvents(this, this);
        
        // Tạo tác vụ lặp lại tự động cập nhật bảng mỗi giây (20 Ticks = 1 giây)
        Bukkit.getScheduler().runTaskTimer(this, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                updateScoreboard(player);
            }
        }, 0L, 20L);
        
        getLogger().info("Plugin 10A1 SMP Scoreboard (Phien ban v1.26.20) da bat thanh cong!");
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        // Cập nhật bảng điểm ngay lập tức khi có người chơi kết nối vào game
        updateScoreboard(event.getPlayer());
    }

    private void updateScoreboard(Player player) {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getNewScoreboard();
        
        // Tạo tiêu đề bảng bằng hệ thống Adventure Component (Màu đỏ, In đậm)
        Component titleComponent = Component.text("10A1 SMP")
                .color(NamedTextColor.RED)
                .decorate(TextDecoration.BOLD);
        
        Objective obj = board.registerNewObjective("10a1_smp", Criteria.DUMMY, titleComponent);
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        
        // MẸO: Xóa bỏ hoàn toàn hàng số đỏ xấu xí ở rìa phải của bảng điểm (Chỉ Paper API mới hỗ trợ)
        obj.numberFormat(NumberFormat.blank());

        // Thu thập các thông số thống kê thực tế của người chơi
        int level = player.getLevel();
        int deaths = player.getStatistic(Statistic.DEATHS);
        // Quy đổi thời gian thế giới ra số ngày sinh tồn (1 ngày Minecraft = 24.000 Ticks)
        long days = player.getWorld().getFullTime() / 24000;

        // Đẩy thông tin lên các dòng (Số điểm cuối là thứ tự ưu tiên hiển thị từ cao xuống thấp)
        setLine(obj, "§fTên: §a" + player.getName(), 4);
        setLine(obj, "§fKinh nghiệm: §e" + level, 3);
        setLine(obj, "§fNgày: §b" + days, 2);
        setLine(obj, "§fDie: §c" + deaths, 1);

        // Áp dụng bảng điểm cá nhân hóa này lên màn hình của người chơi
        player.setScoreboard(board);
    }

    private void setLine(Objective obj, String text, int scoreValue) {
        Score score = obj.getScore(text);
        score.setScore(scoreValue);
    }
}
