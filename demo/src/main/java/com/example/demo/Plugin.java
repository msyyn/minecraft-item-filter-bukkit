package com.example.demo;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import org.bukkit.Location;

public class Plugin extends JavaPlugin implements Listener {

  private HashMap<UUID, PlayerPreferences> playerPreferences;

  @Override
  public void onEnable() {
    getServer().getPluginManager().registerEvents(this, this);
    playerPreferences = new HashMap<>();
    getLogger().info("Plugin Enabled");
  }

  @Override
  public void onDisable() {
    getLogger().info("Plugin Disabled");
  }

  @EventHandler
  public void onBlockBreak(BlockBreakEvent event) {
    //
  }

  @EventHandler
  public void onPlayerPickUpItem(EntityPickupItemEvent event) {
    if (!(event.getEntity() instanceof Player)) {
      return;
    }

    Player player = (Player) event.getEntity();
    PlayerPreferences prefs = playerPreferences.get(player.getUniqueId());

    if (prefs == null) {
      return;
    }

    if (prefs.getIgnoredItems().contains(event.getItem().getItemStack().getType())) {
      event.setCancelled(true);
    }
  }

  @Override
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    if (cmd.getName().equalsIgnoreCase("ignoreblock")) {
      // Ensure the command sender is a player
      if (!(sender instanceof Player)) {
        sender.sendMessage("This command can only be used by a player.");
        return true;
      }

      Player player = (Player) sender;
      PlayerInventory inventory = player.getInventory();
      Material itemInHand = inventory.getItemInMainHand().getType();

      if (itemInHand == Material.AIR) {
        sender.sendMessage("You are not holding any item.");
        return true;
      }

      PlayerPreferences prefs = playerPreferences.computeIfAbsent(player.getUniqueId(), k -> new PlayerPreferences());
      HashSet<Material> ignoredItems = prefs.getIgnoredItems();

      // Get the location of the player's main hand
      Location handLocation = player.getEyeLocation();
      handLocation.add(player.getEyeLocation().getDirection().multiply(1));

      if (ignoredItems.contains(itemInHand)) {
        ignoredItems.remove(itemInHand);
        sender.sendMessage("You will now pick up " + itemInHand.name() + ".");
      } else {
        ignoredItems.add(itemInHand);
        sender.sendMessage("You will no longer pick up " + itemInHand.name() + ".");
      }

      return true;
    }

    if (cmd.getName().equalsIgnoreCase("ignoreblocks")) {
      // Ensure the command sender is a player
      if (!(sender instanceof Player)) {
        sender.sendMessage("This command can only be used by a player.");
        return true;
      }

      Player player = (Player) sender;
      UUID playerUUID = player.getUniqueId();
      PlayerPreferences prefs = playerPreferences.get(playerUUID);

      if (prefs == null || prefs.getIgnoredItems().isEmpty()) {
        sender.sendMessage(
            "You have not ignored any blocks. Ignore a block by holding it in your hand and write /ignoreblock");
        return true;
      }

      sender.sendMessage(ChatColor.GOLD + "Ignored Blocks:");
      for (Material ignoredItem : prefs.getIgnoredItems()) {
        sender.sendMessage(ChatColor.YELLOW + "- " + ignoredItem.name());
      }
      sender.sendMessage(ChatColor.GOLD + "Clear all with /clearignores");

      return true;
    }

    if (cmd.getName().equalsIgnoreCase("clearignores")) {
      // Ensure the command sender is a player
      if (!(sender instanceof Player)) {
        sender.sendMessage(ChatColor.RED + "This command can only be used by a player.");
        return true;
      }

      Player player = (Player) sender;
      UUID playerUUID = player.getUniqueId();
      PlayerPreferences prefs = playerPreferences.get(playerUUID);

      if (prefs == null || prefs.getIgnoredItems().isEmpty()) {
        sender.sendMessage(ChatColor.YELLOW + "You are not ignoring any blocks.");
        return true;
      }

      prefs.getIgnoredItems().clear();
      sender.sendMessage(ChatColor.GREEN + "All ignored blocks have been cleared. You will now pick up all blocks.");

      return true;
    }

    // ... handle other commands ...

    return false;
  }
}
