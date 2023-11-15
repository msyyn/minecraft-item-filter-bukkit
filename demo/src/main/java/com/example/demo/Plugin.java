package com.example.demo;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class Plugin extends JavaPlugin implements Listener {

  private HashMap<UUID, HashSet<Material>> playerBlockPreferences;

  @Override
  public void onEnable() {
    getServer().getPluginManager().registerEvents(this, this);
    playerBlockPreferences = new HashMap<>();
    getLogger().info("Plugin Enabled");
  }

  @Override
  public void onDisable() {
    getLogger().info("Plugin Disabled");
  }

  @EventHandler
  public void onPlayerInteract(PlayerInteractEvent event) {
    Player player = event.getPlayer();

    // Open the GUI when the player right-clicks with an empty hand, for example
    if (player.getInventory().getItemInMainHand().getType() == Material.AIR) {
      new BlockPickupGUI().openInventory(player, null);
    }
  }

  @EventHandler
  public void onInventoryClick(InventoryClickEvent event) {
    if (event.getView().getTitle().equals(ChatColor.GREEN + "Select Blocks") && event.getClickedInventory() != null
        && event.getClickedInventory().getType() == InventoryType.CHEST) {
      event.setCancelled(true);

      ItemStack clickedItem = event.getCurrentItem();
      if (clickedItem != null && clickedItem.getType() != Material.AIR) {
        Player player = (Player) event.getWhoClicked();
        UUID playerUUID = player.getUniqueId();

        Material ore = getOreFromItem(clickedItem.getType());
        if (ore != null) {
          toggleOreSelection(playerUUID, ore);
          new BlockPickupGUI().openInventory(player, playerBlockPreferences.get(playerUUID));
        }
      }
    }
  }

  private Material getOreFromItem(Material material) {
    switch (material) {
      case GOLD_INGOT:
      case GOLD_ORE:
        return Material.GOLD_ORE;
      case IRON_INGOT:
      case IRON_ORE:
        return Material.IRON_ORE;
      case DIAMOND:
      case DIAMOND_ORE:
        return Material.DIAMOND_ORE;
      default:
        return null;
    }
  }

  private void toggleOreSelection(UUID playerUUID, Material ore) {
    HashSet<Material> selectedOres = playerBlockPreferences.computeIfAbsent(playerUUID, k -> new HashSet<>());
    if (selectedOres.contains(ore)) {
      selectedOres.remove(ore);
    } else {
      selectedOres.add(ore);
    }
  }

  @EventHandler
  public void onBlockBreak(BlockBreakEvent event) {
    Player player = event.getPlayer();
    Material brokenBlockType = event.getBlock().getType();

    HashSet<Material> selectedOres = playerBlockPreferences.get(player.getUniqueId());

    // Check if the set is not null and not empty
    if (selectedOres != null && !selectedOres.isEmpty()) {
      // If the block broken is in the selected set, add it to the player's inventory
      if (selectedOres.contains(brokenBlockType)) {
        player.getInventory().addItem(new ItemStack(brokenBlockType));
        event.setDropItems(false); // Prevent the block from dropping on the ground
      }
    } else {
      // If the set is null or empty, the event behaves normally (blocks drop on the
      // ground)
    }
  }

  @Override
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    if (cmd.getName().equalsIgnoreCase("selectblocks")) {
      // Ensure the command sender is a player
      if (!(sender instanceof Player)) {
        sender.sendMessage("This command can only be used by a player.");
        return true;
      }

      Player player = (Player) sender;
      UUID playerUUID = player.getUniqueId();

      // Retrieve or create a new HashSet for the player
      HashSet<Material> selectedOres = playerBlockPreferences.computeIfAbsent(playerUUID, k -> new HashSet<>());

      // Open the inventory GUI for the player
      BlockPickupGUI blockPickupGUI = new BlockPickupGUI();
      blockPickupGUI.openInventory(player, selectedOres);

      return true;
    }

    // ... handle other commands ...

    return false;
  }
}
