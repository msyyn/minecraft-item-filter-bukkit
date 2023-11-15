package com.example.demo;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.HashSet;

public class BlockPickupGUI {

  public void openInventory(Player player, HashSet<Material> selectedOres) {
    if (selectedOres == null) {
      selectedOres = new HashSet<>();
    }

    // Assuming a 9-slot inventory for simplicity. Adjust as needed.
    Inventory inv = Bukkit.createInventory(null, 18, ChatColor.WHITE + "Select Blocks");

    // Add ores and their indicators
    addOreWithIndicator(inv, 0, Material.GOLD_ORE, selectedOres.contains(Material.GOLD_ORE));
    addOreWithIndicator(inv, 1, Material.IRON_ORE, selectedOres.contains(Material.IRON_ORE));
    addOreWithIndicator(inv, 2, Material.DIAMOND_ORE, selectedOres.contains(Material.DIAMOND_ORE));
    // Add more ores as needed

    player.openInventory(inv);
  }

  private void addOreWithIndicator(Inventory inv, int slot, Material ore, boolean isSelected) {
    // Add ore item
    inv.setItem(slot, new ItemStack(ore));

    // Add indicator below the ore
    ItemStack indicator = new ItemStack(isSelected ? Material.GREEN_WOOL : Material.RED_WOOL);
    ItemMeta meta = indicator.getItemMeta();
    if (meta != null) {
      meta.setDisplayName(ChatColor.YELLOW + (isSelected ? "ON" : "OFF"));
      indicator.setItemMeta(meta);
    }
    inv.setItem(slot + 9, indicator); // Assuming 9 slots per row
  }
}
