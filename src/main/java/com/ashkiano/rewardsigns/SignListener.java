package com.ashkiano.rewardsigns;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.configuration.ConfigurationSection;

import java.time.LocalDate;

public class SignListener implements Listener {

    private final RewardSigns plugin;

    public SignListener(RewardSigns plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onSignClick(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getClickedBlock().getType() != Material.ACACIA_WALL_SIGN &&
                event.getClickedBlock().getType() != Material.BIRCH_WALL_SIGN &&
                event.getClickedBlock().getType() != Material.DARK_OAK_WALL_SIGN &&
                event.getClickedBlock().getType() != Material.JUNGLE_WALL_SIGN &&
                event.getClickedBlock().getType() != Material.OAK_WALL_SIGN &&
                event.getClickedBlock().getType() != Material.SPRUCE_WALL_SIGN &&
                event.getClickedBlock().getType() != Material.WARPED_WALL_SIGN &&
                event.getClickedBlock().getType() != Material.CRIMSON_WALL_SIGN &&
                event.getClickedBlock().getType() != Material.ACACIA_SIGN &&
                event.getClickedBlock().getType() != Material.BIRCH_SIGN &&
                event.getClickedBlock().getType() != Material.DARK_OAK_SIGN &&
                event.getClickedBlock().getType() != Material.JUNGLE_SIGN &&
                event.getClickedBlock().getType() != Material.OAK_SIGN &&
                event.getClickedBlock().getType() != Material.SPRUCE_SIGN &&
                event.getClickedBlock().getType() != Material.WARPED_SIGN &&
                event.getClickedBlock().getType() != Material.CRIMSON_SIGN) return;

        Location clickedLocation = event.getClickedBlock().getLocation();
        World clickedWorld = clickedLocation.getWorld();
        String worldName = clickedWorld.getName();
        String locationKey = clickedLocation.getBlockX() + "," + clickedLocation.getBlockY() + "," + clickedLocation.getBlockZ();

        ConfigurationSection signsSection = plugin.getConfig().getConfigurationSection("signs");

        if (signsSection == null || !signsSection.contains(worldName) || !signsSection.getConfigurationSection(worldName).contains(locationKey)) {
            return;
        }

        String rewardId = signsSection.getConfigurationSection(worldName).getString(locationKey);
        Player player = event.getPlayer();

        LocalDate lastReceived = LocalDate.parse(plugin.getConfig().getString("players." + player.getUniqueId() + "." + rewardId, "2000-01-01"));

        String monthlyLimitMessage = plugin.getConfig().getString("messages.monthlyLimit", "You can only receive this reward once a month!");

        if (lastReceived.plusMonths(1).isAfter(LocalDate.now())) {
            player.sendMessage(monthlyLimitMessage);
            return;
        }

        giveReward(player, rewardId);
    }

    private void giveReward(Player player, String rewardId) {
        for (String command : plugin.getConfig().getStringList("rewards." + rewardId)) {
            String commandToExecute = command.replace("%player%", player.getName());
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), commandToExecute);
        }

        plugin.getConfig().set("players." + player.getUniqueId() + "." + rewardId, LocalDate.now().toString());
        plugin.saveConfig();
    }
}