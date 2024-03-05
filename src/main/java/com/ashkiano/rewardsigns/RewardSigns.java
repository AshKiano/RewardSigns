package com.ashkiano.rewardsigns;

import org.bukkit.plugin.java.JavaPlugin;

public class RewardSigns extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        new SignListener(this);

        Metrics metrics = new Metrics(this, 21007);

        this.getLogger().info("Thank you for using the RewardSigns plugin! If you enjoy using this plugin, please consider making a donation to support the development. You can donate at: https://donate.ashkiano.com");
    }

    @Override
    public void onDisable() {
        // Optionally save data when the plugin is disabled
    }
}