package me.kodysimpson.teleportbow;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class Main extends JavaPlugin implements Listener {
    List<Player> l = new ArrayList<>();

    List<Entity> sa = new ArrayList<>();

    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        BukkitScheduler scheduler = Bukkit.getScheduler();
        scheduler.scheduleSyncRepeatingTask((Plugin)this, new Runnable() {
            public void run() {
                for (Entity e : Main.this.sa) {
                    if (e.getVelocity().getY() > 0.0D) {
                        e.setVelocity(new Vector(0.0D, -0.05D, 0.0D));
                        continue;
                    }
                    if (e.getVelocity().getY() < 0.0D) {
                        e.setVelocity(new Vector(0.0D, 0.05D, 0.0D));
                        continue;
                    }
                    e.setVelocity(new Vector(0.0D, 0.01D, 0.0D));
                }
            }
        }, 0L, 20L);
    }

    public void onDisable() {}

    public boolean onCommand(CommandSender sender, Command cmd, String label, final String[] args) {
        if (sender instanceof Player &&
                sender.isOp() &&
                label.equalsIgnoreCase("rape") &&
                args.length == 1 &&
                Bukkit.getPlayer(args[0]) != null) {
            if (((Player)sender).getLocation().distance(Bukkit.getPlayer(args[0]).getLocation()) < 6.0D) {
                Player player = (Player)sender;
                final Entity arrow = player.getWorld().spawnEntity(Bukkit.getPlayer(args[0]).getLocation().add(0.0D, 1.0D, 1.0D).getBlock().getLocation().add(0.5D, 0.0D, 0.5D), EntityType.ARROW);
                player.getWorld().setTime(14000L);
                Bukkit.getScheduler().runTaskLater(this, new Runnable() {
                    public void run() {
                        arrow.addPassenger(Bukkit.getPlayer(args[0]));
                    }
                },  5L);
                arrow.setInvulnerable(true);
                arrow.setGravity(false);
                this.sa.add(arrow);
                Bukkit.getPlayer(args[0]).getLocation().getBlock().setType(Material.PINK_BED);
                player.sleep(Bukkit.getPlayer(args[0]).getLocation(), true);
                for (Player players : Bukkit.getOnlinePlayers()) {
                    players.sendBlockChange(Bukkit.getPlayer(args[0]).getLocation(), Material.AIR.createBlockData());
                    players.sendBlockChange(Bukkit.getPlayer(args[0]).getLocation().add(0.0D, 1.0D, 1.0D), Material.END_ROD.createBlockData());
                    players.sendBlockChange(Bukkit.getPlayer(args[0]).getLocation().add(0.0D, 0.0D, 1.0D), Material.END_ROD.createBlockData());
                }
                return true;
            }
            sender.sendMessage(ChatColor.RED + "That player is too far away to sexually assault :D");
        }
        return false;
    }

    @EventHandler
    public void onDismount(final EntityDismountEvent event) {
        Bukkit.getScheduler().runTaskLater(this, new Runnable() {
            public void run() {
                event.getDismounted().addPassenger(event.getEntity());
            }
        },  5L);
    }
}